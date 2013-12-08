-module(zad4).
-export([start/2, kopnij/1, stop/1]).


start(Kraj, Partner) ->
    Pid = spawn(fun() -> graj(Partner) end),
    register(Kraj, Pid),
    ok.

kopnij(Kraj) -> Pid = whereis(Kraj), Pid ! kopnij, ok.

stop(Kraj) -> Pid = whereis(Kraj), Pid ! stop, stop.

graj(Partner) ->
    receive
        stop -> ok;
        kopnij -> io:format("pilka leci od ~s~n",[Partner]), whereis(Partner) ! kopnij, graj(Partner);
        bramka -> io:format("bramka dla ~s~n",[Partner]), graj(Partner);
        obroniona -> io:format("~s obronil bramke~n",[Partner]), graj(Partner)
    end.
