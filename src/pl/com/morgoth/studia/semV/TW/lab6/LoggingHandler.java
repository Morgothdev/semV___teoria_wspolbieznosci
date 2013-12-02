package pl.com.morgoth.studia.semV.TW.lab6;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import org.apache.logging.log4j.LogManager;

public class LoggingHandler implements Runnable, EventHandler {

	private final SocketChannel socket;
	private final ByteBuffer dst = ByteBuffer.allocate(5000);
	private final SocketChannel fileSocket;

	public LoggingHandler(Selector selector, SocketChannel socket, SocketChannel fileSocket) throws IOException {
		this.socket = socket;
		this.fileSocket = fileSocket;
		this.socket.configureBlocking(false);
		SelectionKey registerKey = socket.register(selector, SelectionKey.OP_READ);
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
				fileSocket.write(dst);
			}
		} catch (IOException e) {
			LogManager.getLogger(LoggingHandler.class).error("run", e);
		}
	}

}