-module(zad4).
-export([start/2, kopnij/1, stop/1]).


start(Kraj, Partner) ->
    Pid = spawn(fun() -> graj(Partner) end),
    register(Kraj, Pid),
    ok.

kopnij(Kraj) -> Pid = whereis(Kraj), Pid ! kopnij, ok.

stop(Kraj) -> Pid = whereis(Kraj), Pid ! stop, stop.

graj(Partner) ->
    {A1,A2,A3} = now(),
    random:seed(A1, A2, A3),
    receive
        stop -> ok;
        kopnij -> io:format("pilka leci od ~s~n",[Partner]),
                case random:uniform(3) of
                    1 -> whereis(Partner) ! kopnij;
                    2 -> whereis(Partner) ! bramka;
                    3 -> whereis(Partner) ! obroniona
                 end,
                graj(Partner);
        bramka -> io:format("bramka dla ~s~n",[Partner]), graj(Partner);
        obroniona -> io:format("~s obronil bramke~n",[Partner]), graj(Partner)
    end.
