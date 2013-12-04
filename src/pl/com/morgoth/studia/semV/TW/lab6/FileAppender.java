package pl.com.morgoth.studia.semV.TW.lab6;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.Pipe;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

public class FileAppender implements EventHandler {

	private Pipe pipe;
	private ExecutorService executor = Executors.newFixedThreadPool(1);
	private final ByteBuffer dst = ByteBuffer.allocate(500);
	private final FileChannel fileChannel;

	public FileAppender(Path path, InitiationDispatcher dispatcher, Pipe pipe)
			throws IOException {
		this.pipe = pipe;
		this.pipe.source().configureBlocking(false);

		dispatcher.register(this, SelectionKey.OP_READ);

		fileChannel = FileChannel.open(path, StandardOpenOption.CREATE,
				StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
		
		LogManager.getLogger(FileAppender.class).log(Level.ERROR,
				"filechannel: {}", fileChannel);

	}

	@Override
	public void handleEvent() {
		executor.execute(new Runnable() {
			
			@Override
			public void run() {
				try {
					int readed, wrote;
					while ((readed = pipe.source().read(dst)) != 0) {
						if (readed > 0) {
							if (readed < dst.capacity()) {
								dst.put(System.lineSeparator().getBytes());
							}
							dst.flip();
							wrote = fileChannel.write(dst);
							LogManager.getLogger(FileAppender.class).log(Level.INFO,
									"file appender reads {}, wrote {}", readed, wrote);
							dst.clear();
						} else {
							LogManager.getLogger(FileAppender.class).error("end of input?");
							return;
						}
					}
				} catch (IOException e) {
					LogManager.getLogger(FileAppender.class).error("run", e);
				}
			}
		});
	}

	@Override
	public SelectableChannel getHandle() {
		return pipe.source();
	}

}
