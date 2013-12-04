package pl.com.morgoth.studia.semV.TW.lab6;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.Pipe;
import java.nio.channels.Pipe.SinkChannel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

class LoggingAcceptor implements Runnable, EventHandler {

	private final ServerSocketChannel serverSocket;
	private InitiationDispatcher initiationDispatcher;
	private	 Pipe.SinkChannel pipe;
	
	public LoggingAcceptor(InitiationDispatcher initiationDispatcher, int portToListening, SinkChannel sinkChannel) throws IOException {
		this.initiationDispatcher = initiationDispatcher;
		pipe = sinkChannel;
		serverSocket = ServerSocketChannel.open();
		serverSocket.socket().bind(new InetSocketAddress(portToListening));
		serverSocket.configureBlocking(false);
		
		this.initiationDispatcher.register(this,SelectionKey.OP_ACCEPT);

	}

	@Override
	public void run() {
		try {
			SocketChannel socketChannelForClient = serverSocket.accept();
			LogManager.getLogger(LoggingAcceptor.class).log(Level.INFO, "acceptor accepts connection: {}",
					socketChannelForClient);
			if (socketChannelForClient != null)
				new LoggingHandler(socketChannelForClient, initiationDispatcher,pipe);
		} catch (IOException ex) {
			LogManager.getLogger(LoggingAcceptor.class).log(Level.ERROR, "", ex);
		}
		LogManager.getLogger(LoggingAcceptor.class).log(Level.INFO, "acceptor return");
	}

	@Override
	public void handleEvent() {
		run();
	}

	@Override
	public SelectableChannel getHandle() {
		return serverSocket;
	}
}
