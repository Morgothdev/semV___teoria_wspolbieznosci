-module(lab8).
-export([start/0, write/2, write/3, stop/0, truncate/1, truncate/2, delete/1, delete/2, echo/0]).


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Funkcje startowe

start() -> 
    register(server, spawn(fun() -> init() end)), started.

init() ->
    server_loop(dict:new()).


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Funkcje klienta

echo() -> sync_call({check}).
stop() -> async_call(stop).   

write(File, Data, Confirmation) -> call({write, File, Data}, Confirmation).
write(File, Data) -> write(File, Data, no).

truncate(File, Confirmation) ->call({truncate, File}, Confirmation).
truncate(File) -> truncate(File, no).

delete(File, Confirmation) -> call({delete, File}, Confirmation).
delete(File) -> delete(File, no).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%Intejfejs ukrywajacy komunikacje klient-serwer 

%Zwykle uzywamy wywolan synchronicznych, gdy klient potrzebuje odpowiedzi 
%od serwera

call(Request, Confirmation) -> 
    if 
        Confirmation =/= no -> sync_call(Request);
        Confirmation =:= no -> async_call(Request)
    end.

sync_call(Request)->
   server ! {request, Request, self()},
   receive
       {reply, Reply} -> Reply
   end.

%Moze byc uzyte, gdy klient nie potrzebuje odpowiedzi od serwera 
async_call(Request)->
   server! {request, Request},
   ok.

%funkcja serwera wysylajaca odpowiedz do klienta

reply(Pid, Message)->
   Pid! {reply, Message}.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%glowna petla
server_loop(Files) ->
    receive
        {request, stop} -> io:format("server stopping...~n"), dict:map(fun(_, V) -> V ! stop, ok end, Files), io:format("server stopped.~n"), ok;
        {request, {check}, Pid} -> io:format("echo from ~p~n",[Pid]), reply(Pid, echo), server_loop(Files);
        {request, {write, FileName, Data}} -> io:format("rec~n"), server_loop(write(Files, FileName, Data, no));
        {request, {write, FileName, Data}, Pid} -> io:format("rec with confirm~n"), server_loop(write(Files, FileName,Data, Pid));
        {request, {truncate, FileName}} -> io:format("truncate file ~s~n",[FileName]), server_loop(truncate(Files, FileName, no));
        {request, {truncate, FileName}, Pid} -> io:format("truncate file ~s~n",[FileName]), server_loop(truncate(Files, FileName, Pid));
        {request, {delete, FileName}} -> io:format("delete file ~s~n",[FileName]), server_loop(delete(Files, FileName, no));
        {request, {delete, FileName}, Pid} -> io:format("delete file ~s~n",[FileName]), server_loop(delete(Files, FileName, Pid))
    end.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% funkcje wewnetrzne implementujace dzialalnosc serwera
% moga byc w osobnym module

truncate(Files, FileName, Confirm) -> send_message_to(Files, FileName, Confirm, {truncate, Confirm}).

write(Files, FileName, Data, Confirm) -> send_message_to(Files, FileName, Confirm, {write, Data, Confirm}).

delete(Files, FileName, Confirm) -> send_message_to(Files, FileName, Confirm, {delete, Confirm}).

send_message_to(Files, FileName, Confirm, Message) ->
    {Was, Pid} = filePid(FileName, Files),
    if (Was =/= error) -> Pid ! Message; true -> ok end,
    if 
        Was =:= yes -> Files;
        Was =:= no -> dict:store(FileName, Pid, Files);
        (Was =:= error) and (Confirm =/= no) -> reply(Confirm, {error, Pid}), Files;
        true -> Files
    end.

filePid(FileName, Files) ->
    Is = dict:is_key(FileName, Files),
    if
        Is =:= true -> {yes, dict:fetch(FileName, Files)};
        Is =:= false -> 
            FileType = filename:pathtype(FileName), 
            if 
                (FileType =:= absolute) or (FileType =:= volumerelative) ->
                    {error, "FilePath is not relative, should be"};
                FileType =:= relative ->
                    {no, spawn(fun() -> file_loop(FileName) end) }
            end                
    end.

file_loop(FileName) ->
    receive 
        stop -> io:format("FileProcess ~s closed~n",[FileName]), ok;
        {write, Data, Confirm} -> 
            {Succ, Result} = write_into(FileName, Data),
            if Confirm =/= no -> reply(Confirm, {Succ, Result}); true -> ok end,
            file_loop(FileName);
        {truncate, Confirm} -> 
            case file:open(FileName, write) of
                {ok, IoDev} -> file:close(IoDev), if Confirm=/=no -> reply(Confirm, {truncated}); true -> ok end;
                {error, Reason} -> if Confirm=/=no -> reply(Confirm, {error, Reason}); true -> ok end
            end,
            file_loop(FileName);
        {delete, Confirm} ->
            case file:delete(FileName) of
                ok -> if Confirm=/=no -> reply(Confirm, {deleted}); true -> ok end;
                {error, Reason} -> if Confirm=/=no -> reply(Confirm, {error, Reason}); true -> ok end
            end,
            file_loop(FileName)
    end.


write_into(FileName, Data) ->
    case file:open(FileName, [append, write]) of
        {ok, IoDev} ->
            io:format(IoDev,"~s~n",[Data]), {writed, {file_closed, file:close(IoDev)}};
        {error, Reason} -> {error, Reason}
    end.