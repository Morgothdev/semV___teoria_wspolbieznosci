package pl.com.morgoth.studia.semV.TW.lab6;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.Pipe;
import java.nio.channels.Pipe.SinkChannel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

class LoggingAcceptor implements EventHandler {

	private final ServerSocketChannel serverSocket;
	private InitiationDispatcher dispatcher;
	private Pipe.SinkChannel pipe;

	public LoggingAcceptor(InitiationDispatcher initiationDispatcher,
			int portToListening, SinkChannel sinkChannel) throws IOException {
		this.dispatcher = initiationDispatcher;
		pipe = sinkChannel;
		serverSocket = ServerSocketChannel.open();
		serverSocket.socket().bind(new InetSocketAddress(portToListening));
		serverSocket.configureBlocking(false);

		this.dispatcher.register(this, SelectionKey.OP_ACCEPT);
	}

	@Override
	public void handleEvent() {
		LogManager.getLogger(LoggingAcceptor.class).info("handle event");
		try {
			SocketChannel socketChannelForClient = serverSocket.accept();
			LogManager.getLogger(LoggingAcceptor.class).log(Level.INFO,
					"acceptor accepts connection: {}", socketChannelForClient);
			if (socketChannelForClient != null) {
				new LoggingHandler(socketChannelForClient, dispatcher, pipe);
			}
		} catch (IOException ex) {
			LogManager.getLogger(LoggingAcceptor.class)
					.log(Level.ERROR, "", ex);
		}
		// if (R.USE_EXECUTORS) {
		// try {
		// dispatcher.register(this, SelectionKey.OP_ACCEPT);
		// } catch (ClosedChannelException e) {
		// e.printStackTrace();
		// }
		// }
		LogManager.getLogger(LoggingAcceptor.class).log(Level.INFO,
				"acceptor return");
	}

	@Override
	public SelectableChannel getHandle() {
		return serverSocket;
	}

	@Override
	public boolean isDaemon() {
		return false;
	}

	@Override
	public void shutdown() {
		try {
			serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dispatcher.removeHander(this);
	}
}
