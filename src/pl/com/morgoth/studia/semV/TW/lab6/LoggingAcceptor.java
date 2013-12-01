package pl.com.morgoth.studia.semV.TW.lab6;

import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

class LoggingAcceptor implements Runnable {

	private final ServerSocketChannel serverSocket;
	private final Selector selector;

	public LoggingAcceptor(ServerSocketChannel serverSocket, Selector selector) {
		this.serverSocket = serverSocket;
		this.selector = selector;
	}

	@Override
	public void run() {
		try {
			SocketChannel c = serverSocket.accept();
			if (c != null)
				new LoggingHandler(selector, c);
		} catch (IOException ex) {
			LogManager.getLogger(LoggingAcceptor.class).log(Level.ERROR, "", ex);
		}
	}
}
