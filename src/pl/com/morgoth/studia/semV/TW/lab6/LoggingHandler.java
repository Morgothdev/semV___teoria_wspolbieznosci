package pl.com.morgoth.studia.semV.TW.lab6;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.Pipe;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

public class LoggingHandler implements EventHandler {

	private final SocketChannel socket;
	private final ByteBuffer dst = ByteBuffer.allocate(500);
	private InitiationDispatcher dispatcher;
	private Pipe.SinkChannel pipe;

	public LoggingHandler(SocketChannel socketChannelForClient,
			InitiationDispatcher dispatcher, Pipe.SinkChannel pipe)
			throws IOException {
		LogManager.getLogger(LoggingHandler.class).info("costr start");
		this.socket = socketChannelForClient;
		this.dispatcher = dispatcher;
		this.pipe = pipe;
		this.socket.configureBlocking(false);
		dispatcher.register(this, SelectionKey.OP_READ);
		LogManager.getLogger(LoggingHandler.class).info("contructor ends");
	}

	void process() {
		try {
			TimeUnit.MILLISECONDS.sleep(R.PROCESS_DELAY);
		} catch (InterruptedException e) {
		}
	}

	@Override
	public void handleEvent() {
		LogManager.getLogger(LoggingHandler.class).info("handle event");
		if (socket.isConnected()) {

			if (dst.position() > 0) {
				try {
					pipe.write(dst);
					dst.compact();
					if (dst.position() != 0) {
						LogManager.getLogger(LoggingHandler.class).log(
								Level.ERROR, "head wrote not enought");
						return;
					} else {
						LogManager.getLogger(LoggingHandler.class).log(
								Level.ERROR, "remaining data wrote!");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				LogManager.getLogger(LoggingHandler.class).log(Level.ERROR,
						"wrote not enought");
				return;
			}

			process();
			try {
				int readed, wrote;
				while ((readed = socket.read(dst)) != 0) {
					if (readed > 0) {
						if (readed < dst.capacity()) {
							dst.put(System.lineSeparator().getBytes());
						}
						dst.flip();
						wrote = pipe.write(dst);
						dst.compact();
						if (dst.position() != 0) {
							LogManager.getLogger(LoggingHandler.class).log(
									Level.ERROR, "wrote not enought");
							break;
						}
						LogManager
								.getLogger(LoggingHandler.class)
								.log(Level.INFO,
										"log handler reads {} from {}, wrote {}, remaining {}",
										readed, socket.socket().getPort(),
										wrote, dst.position());
					} else {
						LogManager.getLogger(LoggingHandler.class).log(
								Level.INFO, "log handler reads {} from {}",
								readed, socket.socket().getLocalPort());
						closeConnection();
						return;
					}
				}
			} catch (IOException e) {
				LogManager.getLogger(LoggingHandler.class).error("run", e);
			}
		} else {
			LogManager.getLogger(LoggingHandler.class).error(
					"socket is not connected");
			closeConnection();
			return;
		}
		// if (R.USE_EXECUTORS) {
		// try {
		// dispatcher.register(this, SelectionKey.OP_READ);
		// } catch (ClosedChannelException e) {
		// e.printStackTrace();
		// }
		// }
	}

	private void closeConnection() {
		dispatcher.removeHander(this);
		try {
			socket.close();
		} catch (IOException ex) {
			Logger.getLogger(LoggingHandler.class.getName()).log(
					java.util.logging.Level.SEVERE, null, ex);
		}
		LogManager.getLogger(LoggingHandler.class).log(Level.DEBUG,
				"connection to {} closed", socket);
	}

	@Override
	public SelectableChannel getHandle() {
		return socket;
	}

	@Override
	public boolean isDaemon() {
		return false;
	}

	@Override
	public void shutdown() {
		closeConnection();
		dispatcher.removeHander(this);
	}

}