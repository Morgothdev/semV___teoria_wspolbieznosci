-module(manager).
-export([start/1, start/2, stop/1]).
-export([register_handler/3, unregister_handler/2]).
-export([publish_event/2]).


start(Name, HandlersList) ->
    register(Name, spawn( fun() -> init(HandlersList) end)),
    started.

start(Name) ->
    start(Name, []).

stop(Name) ->
    Name ! {stop, self()},
    receive {reply, Reply} -> Reply end.

register_handler(Name, Handler, InitData) ->
    call(Name,{register_handler, Handler,InitData}).

unregister_handler(Name, Handler) ->
    call(Name, {unregister_handler, Handler}).

publish_event(Name, Event) ->
    call(Name, {publish, Event}).

call(Name, Data) ->
    Name ! {request, self(), Data},
    receive {reply, Reply} -> Reply end.

reply(From, Reply) ->
    From ! {reply, Reply}.

init(HandlersList) ->
    loop(initialize(HandlersList)).

initialize([]) -> [];
initialize([{Handler, InitData} | Rest]) ->
    [{Handler, Handler:init(InitData)} | initialize(Rest)].

terminate([]) -> [];
terminate([{Handler, Data} | Rest]) ->
    [{Handler, Handler:terminate(Data)} | terminate(Rest)].

loop(HandlersList) ->
    receive
        {request, From, Data} ->
            {Reply, NewHandlersList} = handle_request(Data, HandlersList),
            reply(From, Reply),
            loop(NewHandlersList);
        {stop, From} -> reply(From, terminate(HandlersList))
    end.

handle_request({register_handler, Handler, InitData}, HandlersList) ->
    {ok, [{Handler, Handler:init(InitData)} | HandlersList]};

handle_request({unregister_handler, Handler}, HandlersList) ->
    case lists:keysearch(Handler, 1, HandlersList) of
        false -> {{error, instance}, HandlersList};
        {value, {Handler, Data}} -> 
            Reply = {data, Handler:terminate(Data)},
            NewHandlersList = lists:keydelete(Handler, 1, HandlersList),
            {Reply, NewHandlersList}
        end;

handle_request({publish, Event}, HandlersList) ->
    {ok, send_event(Event, HandlersList)}.

send_event(_Event, []) -> [];
send_event(Event, [{Handler, Data} | Rest]) ->
    [{Handler, Handler:handle_event(Event,Data)} | send_event(Event, Rest)].