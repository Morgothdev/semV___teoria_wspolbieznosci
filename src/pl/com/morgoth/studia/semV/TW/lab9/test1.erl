-module(test1).
-export([do/0]).


do() ->
    zad1:start(),
    spawn(fun() -> tester_start() end),
	receive ok -> ok after 1000 -> ok end,
    spawn(fun() -> tester_start() end),
    ok.

tester_start() -> 
    io:format("~p wola wait~n",[self()]),
    zad1:wait(),
    tester_ok().

tester_ok()->
    io:format("~p getted resource, working....~n",[self()]),
    receive
        ok -> ok
        after 5000 -> io:format("done ~p~n", [self()]), zad1:signal(), ok
    end.