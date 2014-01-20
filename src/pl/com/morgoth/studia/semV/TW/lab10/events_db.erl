-module(events_db).
-export([init/1, handle_event/2, terminate/1]).

init(Data) -> Data.

terminate(Data) -> {collected_events, Data}.

handle_event({Event, EventData}, Data) ->
    [{Event, EventData} | Data].