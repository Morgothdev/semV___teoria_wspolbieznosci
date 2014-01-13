-module(test1).
-export([do/0]).


do() ->
    zad1:start(),
    spawn(fun() -> tester_start() end),
    spawn(fun() -> tester_start() end),
    ok.

tester_start() -> 
    io:
    zad1:wait(),
    tester_waiting().
    
tester_waiting() ->
    receive
        ok -> tester_ok()
        after 1000 -> io:format("still waiting ~p~n",[self()]), tester_waiting()
    end.

tester_ok()->
    io:format("~p getted resource, working....~n"),
    receive
        ok -> ok
        after 5000 -> io:format("done ~p~n", [self()]), zad1:signal(), ok
    end.