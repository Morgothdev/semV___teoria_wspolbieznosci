package pl.com.morgoth.studia.semV.TW.lab6;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

public class LoggingHandler implements EventHandler {

	private final SocketChannel socket;
	private final ByteBuffer dst = ByteBuffer.allocate(500);
	private InitiationDispatcher dispatcher;
	private Pipe.SinkChannel pipe;
	private ExecutorService executor = Executors.newFixedThreadPool(1);

	public LoggingHandler(SocketChannel socketChannelForClient,
			InitiationDispatcher dispatcher, Pipe.SinkChannel pipe)
			throws IOException {
		this.socket = socketChannelForClient;
		this.dispatcher = dispatcher;
		this.pipe = pipe;
		this.socket.configureBlocking(false);
		dispatcher.register(this, SelectionKey.OP_READ);
	}

	void process() {
		try {
			TimeUnit.MILLISECONDS.sleep(1000);
		} catch (InterruptedException e) {
		}
	}

	@Override
	public void handleEvent() {
		executor.execute(new Runnable() {

			@Override
			public void run() {
				if (socket.isConnected()) {
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
								LogManager
										.getLogger(LoggingHandler.class)
										.log(Level.INFO,
												"log handler reads {} from {}, wrote {}",
												readed,
												socket.socket().getLocalPort(),
												wrote);
								dst.clear();
							} else {
								LogManager.getLogger(LoggingHandler.class).log(
										Level.INFO,
										"log handler reads {} from {}", readed,
										socket.socket().getLocalPort());
								closeConnection();
								return;
							}
						}
					} catch (IOException e) {
						LogManager.getLogger(LoggingHandler.class).error("run",
								e);
					}
				}
			}
		});
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