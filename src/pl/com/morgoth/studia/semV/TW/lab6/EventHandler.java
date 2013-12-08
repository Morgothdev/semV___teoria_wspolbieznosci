package pl.com.morgoth.studia.semV.TW.lab6;

import java.nio.channels.SelectableChannel;

public interface EventHandler {

	void handleEvent();

	SelectableChannel getHandle();

	boolean isDaemon();

	void shutdown();
}
