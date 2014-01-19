-module(zad1).

-export([go/0, loop/0]).

go() ->
	Pid2 = spawn(zad1,loop, []),
	Pid2 ! {hello},
	Pid2 ! stop.

loop() ->
	receive
		{Msg} -> 
			io:format("~p~n",[Msg]),
			loop();
		stop ->
			true
	end.