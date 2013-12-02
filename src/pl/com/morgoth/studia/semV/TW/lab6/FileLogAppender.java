package pl.com.morgoth.studia.semV.TW.lab6;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

public class FileLogAppender implements EventHandler {

	private static final int LOG_BUFFER_SIZE = 5000;
	private final ByteBuffer dst;
	private final FileChannel fileChannel;
	private final SocketChannel socketChannel;
	private final SelectionKey registerKey;

	public FileLogAppender(Selector selector, Path path, SocketChannel socketChannel) throws IOException {
		fileChannel = FileChannel.open(path);
		dst = ByteBuffer.allocate(LOG_BUFFER_SIZE);
		this.socketChannel = socketChannel;
		registerKey = this.socketChannel.register(selector, SelectionKey.OP_WRITE);
		registerKey.attach(this);
	}

	@Override
	public void handleEvent() {
		try {
			int readedBytes = 0, writedBytes = -2;
			while ((readedBytes = socketChannel.read(dst)) > 0) {
				writedBytes = fileChannel.write(dst);
				LogManager.getLogger(FileLogAppender.class).log(Level.DEBUG,
						"readed {} bytes, wroted {} bytes in file appender", readedBytes, writedBytes);
			}

		} catch (IOException e) {
			LogManager.getLogger(FileLogAppender.class).error("handleEvent", e);
		}
	}
}
