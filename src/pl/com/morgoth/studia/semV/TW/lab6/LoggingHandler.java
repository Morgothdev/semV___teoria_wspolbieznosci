package pl.com.morgoth.studia.semV.TW.lab6;

import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class LoggingHandler implements Runnable, EventHandler {

	private final SocketChannel socket;

	public LoggingHandler(Selector selector, SocketChannel socket) throws IOException {
		this.socket = socket;
		this.socket.configureBlocking(false);
	}

	@Override
	public void run() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void handleEvent() {
		throw new UnsupportedOperationException();
	}

}