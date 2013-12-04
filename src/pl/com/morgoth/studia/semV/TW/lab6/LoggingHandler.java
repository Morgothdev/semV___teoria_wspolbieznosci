package pl.com.morgoth.studia.semV.TW.lab6;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.logging.Logger;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

public class LoggingHandler implements Runnable, EventHandler {

	private final SocketChannel socket;
	private final ByteBuffer dst = ByteBuffer.allocate(500);
	private final FileChannel fileChannel;
	private InitiationDispatcher dispatcher;

	public LoggingHandler(SocketChannel socketChannelForClient,
			FileChannel logFileChannel, InitiationDispatcher dispatcher)
			throws IOException {
		this.socket = socketChannelForClient;
		this.fileChannel = logFileChannel;
		this.dispatcher = dispatcher;
		this.socket.configureBlocking(false);
		dispatcher.register(this, SelectionKey.OP_READ);
	}

	@Override
	public void handleEvent() {
		run();
	}

	@Override
	public void run() {
		try {
			int readed, wrote;
			while ((readed = socket.read(dst)) != 0) {
				if (readed > 0) {
					if (readed < dst.capacity()) {
						dst.put(System.lineSeparator().getBytes());
					}
					dst.flip();
					wrote = fileChannel.write(dst);
					LogManager.getLogger(LoggingHandler.class).log(Level.INFO,
							"log handler reads {}, wrote {}", readed, wrote);
					dst.clear();
				} else {
					closeConnection();
					return;
				}
			}
		} catch (IOException e) {
			LogManager.getLogger(LoggingHandler.class).error("run", e);
		}
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

}