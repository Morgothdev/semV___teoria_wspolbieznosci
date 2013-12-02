package pl.com.morgoth.studia.semV.TW.lab6;

import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

class LoggingAcceptor implements Runnable, EventHandler {

	private final ServerSocketChannel serverSocket;
	private final Selector selector;
	private SocketChannel logFileAppender;

	public LoggingAcceptor(ServerSocketChannel serverSocket, Selector selector, SocketChannel logFileAppender) {
		this.serverSocket = serverSocket;
		this.selector = selector;
	}

	@Override
	public void run() {
		try {
			SocketChannel socketChannelForClient = serverSocket.accept();
			if (socketChannelForClient != null)
				new LoggingHandler(selector, socketChannelForClient, logFileAppender);
		} catch (IOException ex) {
			LogManager.getLogger(LoggingAcceptor.class).log(Level.ERROR, "", ex);
		}
	}

	@Override
	public void handleEvent() {
		run();
	}
}
