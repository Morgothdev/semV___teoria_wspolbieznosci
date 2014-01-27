-module(zad2).

-export([go/0, loop/0]).

go() ->
    Pid2 = spawn(zad2,loop, []),
    Pid2 ! hello.

loop() ->
    receive
        goodbye -> 
            io:format("Goodbye!~n"),
            loop();
        hello ->
            io:format("Hello~n"),
            loop()
        after (1000) ->
            io:format("Interrupted!~n"),
            loop()
    end.


%proces A się nie zatrzymał, wysłał komunikat i poszedł
%proces B zignorował komunikat i czeka na kolejne