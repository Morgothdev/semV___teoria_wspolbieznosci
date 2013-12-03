package pl.com.morgoth.studia.semV.TW.lab6;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

class LoggingAcceptor implements Runnable, EventHandler {

	private final ServerSocketChannel serverSocket;
	private final Selector selector;
	private final FileChannel logFileChannel;

	public LoggingAcceptor(ServerSocketChannel serverSocket, Selector selector, FileChannel logFileChannel) {
		this.serverSocket = serverSocket;
		this.selector = selector;
		this.logFileChannel = logFileChannel;
	}

	@Override
	public void run() {
		try {
			SocketChannel socketChannelForClient = serverSocket.accept();
			LogManager.getLogger(LoggingAcceptor.class).log(Level.INFO, "acceptor accepts connection: {}",
					socketChannelForClient);
			if (socketChannelForClient != null)
				new LoggingHandler(selector, socketChannelForClient, logFileChannel);
		} catch (IOException ex) {
			LogManager.getLogger(LoggingAcceptor.class).log(Level.ERROR, "", ex);
		}
		LogManager.getLogger(LoggingAcceptor.class).log(Level.INFO, "acceptor return");
	}

	@Override
	public void handleEvent() {
		run();
	}
}
