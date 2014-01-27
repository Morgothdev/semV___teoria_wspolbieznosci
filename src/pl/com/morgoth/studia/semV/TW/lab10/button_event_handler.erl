-module(button_event_handler).
-export([init/1, handle_event/2, terminate/1]).

init(Data) -> Data.

terminate(_) -> ok.

handle_event({button_pressed, Button}, Data) ->
    io:format("button_event_handler: button ~p pressed~n", [Button]),
    Data;

handle_event({button_clicked, Button}, Data) ->
    io:format("button_event_handler: button ~p clicked~n", [Button]),
    Data;

handle_event({button_released, Button}, Data) ->
    io:format("button_event_handler: button ~p released~n", [Button]),
    Data.