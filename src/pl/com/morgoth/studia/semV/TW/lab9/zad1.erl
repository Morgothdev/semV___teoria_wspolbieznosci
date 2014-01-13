-module(zad1).


init() -> 
    register(mutex, spawn(fun() -> free() end)).


stop() -> mutex ! stop.


wait() ->
    mutex ! {wait, self()},
    receive ok -> ok end.


free() ->
    receive
        {wait, Pid} ->
            Pid ! ok, busy(Pid);
        stop -> exit()
    end.


busy(Pid) ->
    receive
        {signal, Pid}
