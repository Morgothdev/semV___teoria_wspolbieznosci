package pl.com.morgoth.studia.semV.TW.lab6;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

public class LoggingHandler implements Runnable, EventHandler {

	private final SocketChannel socket;
	private final ByteBuffer dst = ByteBuffer.allocate(5000);
	private final FileChannel fileChannel;

	public LoggingHandler(Selector selector, SocketChannel socket, FileChannel fileChannel) throws IOException {
		this.socket = socket;
		this.fileChannel = fileChannel;
		this.socket.configureBlocking(false);
		SelectionKey registerKey = this.socket.register(selector, SelectionKey.OP_READ);
		registerKey.attach(this);
	}

	@Override
	public void handleEvent() {
		run();
	}

	@Override
	public void run() {
		try {
			while (socket.read(dst) > 0) {
				LogManager.getLogger(LoggingHandler.class).log(Level.INFO, "log handler reads {}", dst);
				fileChannel.write(dst);
			}
		} catch (IOException e) {
			LogManager.getLogger(LoggingHandler.class).error("run", e);
		}
	}

}