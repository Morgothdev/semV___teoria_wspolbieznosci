%% -*- coding: utf-8 -*-

-module(girl).
-export([wakeGirl/0, giveHerChocolate/0, takeHerForWalk/0, giveHerRose/0, putHerToSleep/0]).

wakeGirl() -> register(girl, spawn(fun() -> is(20) end)), girlWakeUp.

giveHerChocolate() -> girl ! chocolate, gave.

takeHerForWalk() -> girl ! walk, taken.

giveHerRose() -> girl ! rose, gave.

putHerToSleep() -> girl ! sleep, girlIsSleeping.

is(HappyLevel) ->
    io:format("~p happy~n",[HappyLevel]),
    if HappyLevel < -30 -> divorce(); true -> ok end,
    receive
        chocolate -> eatingChocolate(HappyLevel+5, fun(X) -> is(X) end);
        rose -> gettedRose(HappyLevel+6, fun(X) -> is(X) end);
        walk -> areOnWalk(HappyLevel+3);
        sleep -> io:format("girlIsSleeping~n")
        after 5000 ->
            is(HappyLevel - 3)
    end.

eatingChocolate(HappyLevel, ReturnFunction) ->
    io:format("~p happy~n",[HappyLevel]),
    receive
        chocolate -> io:format("Jak możesz! Bede gruba! "),
        if
            HappyLevel < 0 -> io:format("Tego za wiele! Koniec z nami!~n"), divorce();
            HappyLevel >= 0 -> io:format("Foch!~n"), foched(-10)
        end
        after 1000 -> ReturnFunction(HappyLevel)
    end.

gettedRose(HappyLevel, ReturnFunction) ->
    io:format("~p Happy~n",[HappyLevel]),
    receive
        rose -> io:format("Tyle kwiatow? "),
        if
            HappyLevel >= 0 -> io:format("Cos masz na sumieniu.... Wiem! Zdradzasz mnie! *&^%$#@ !~n"), foched(-25);
            HappyLevel < 0 -> io:format("Daj sobie spokoj z tymi badylami! Koniec z nami!~n"), divorce()
        end
        after 1000 -> ReturnFunction(HappyLevel)
    end.

areOnWalk(HappyLevel) ->
    io:format("~p Happy~n",[HappyLevel]),
    receive
        chocolate -> io:format("Czekolada na spacerze, cudownie!~n"), eatingChocolate(HappyLevel+10, fun(X) -> areOnWalk(X) end);
        rose -> io:format("Kwiat na spacerze, genialnie!~n"), gettedRose(HappyLevel+11, fun(X) -> areOnWalk(X) end)
        after 6000 -> is(HappyLevel)
    end.
        
foched(AngryLevel) ->
    io:format("~p angry~n",[AngryLevel]),
    if AngryLevel < -30 -> divorce(); true -> ok end,
    receive
        chocolate -> io:format("Hmm...~n"), eatingChocolate(AngryLevel+3, fun(X) -> foched(X) end);
        rose -> io:format("Mhmm...~n"), gettedRose(AngryLevel +4, fun(X) -> foched(X) end)
        after 3000 -> foched(AngryLevel-1)
    end.

divorce() ->
    receive
        chocolate -> divorce();
        rose -> divorce();
        walk -> divorce()
        after 1 -> stop
    end.