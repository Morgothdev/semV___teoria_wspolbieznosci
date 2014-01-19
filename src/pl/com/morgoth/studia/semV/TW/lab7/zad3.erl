-module(zad3).
-export([zad1/0, zad2a/0, zad2b/0, zad3a/0, zad3b/0, zad4/0]).

zad1() ->
	C = spawn(fun() -> c1a() end),
	A = spawn(fun()-> send(C, aaa) end),
	B = spawn(fun()-> send(C, bbb) end),
	receive
		after (5000) ->
			A!stop, B!stop, C!stop
	end.

zad2a() ->	
	C = spawn(fun() -> c2a() end),
	A = spawn(fun()-> send(C, aaa) end),
	B = spawn(fun()-> send(C, bbb) end),
	receive
		after (5000) ->
			A!stop, B!stop, C!stop
	end.	

zad2b()->
	C = spawn(fun() -> c2b() end),
	A = spawn(fun()-> send(C, aaa) end),
	B = spawn(fun()-> send(C, bbb) end),
	receive
		after (5000) ->
			A!stop, B!stop, C!stop
	end.

zad3a() ->
	C = spawn(fun() -> c2a() end), 
	A = spawn(fun() -> send(C, aaa, 0) end),
	B = spawn(fun() -> send(C, bbb, 0) end),
	receive
		after (5000) ->
			A!stop, B!stop, C!stop
	end.

zad3b() ->
	C = spawn(fun() -> c2b() end),
	A = spawn(fun() -> send(C, aaa, 0) end),
	B = spawn(fun() -> send(C, bbb, 0) end),
	receive
		after (5000) ->
			A!stop, B!stop, C!stop
	end.

zad4() ->
	C = spawn(fun() -> c3() end),
	A = spawn(fun() -> send(C, aaa) end),
	B = spawn(fun() -> send(C, bbb) end),
	receive
		after (5000) ->
			A!stop, B!stop, C!stop
	end.

send(PID, Message) ->
	receive
		stop -> true
		after (1) ->
			PID ! Message,
			send(PID, Message)
	end.

send(PID, Message, Counter) ->
	receive
		stop -> true
		after (1) ->
			PID ! {Message, Counter},
			send(PID, Message, Counter+1)
	end.

c3() ->
	receive
		aaa -> io:format("aa~n"), c3();
		bbb -> io:format("bb~n"), c3();
		stop -> ok
	end.

c1a() ->
	receive 
		aaa -> io:format("aa~n"), c1b();
		stop -> ok
	end.

c1b() ->
	receive
		bbb ->io:format("bb~n"), c1a();
		stop -> ok
	end.

c2a() ->
	receive
		aaa ->io:format("aa~n"), c2a();
		{aaa, Iteration} -> io:format("aa ~B~n", [Iteration]), c2a();
		stop -> ok
	end.

c2b() ->
	receive
		bbb ->io:format("bb~n"), c2b();
		{bbb, Iteration} -> io:format("bb ~B~n", [Iteration]), c2b();
		stop -> ok
	end.