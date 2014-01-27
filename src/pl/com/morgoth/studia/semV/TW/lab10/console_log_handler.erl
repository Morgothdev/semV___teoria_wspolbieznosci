-module(console_log_handler).
-export([init/1, handle_event/2, terminate/1]).

init(Data) -> Data.

terminate(_) -> ok.

handle_event({Event, What}, Data) ->
    io:format("console_log_handler: event ~p, data with event ~p~n", [Event, What]),
    Data.