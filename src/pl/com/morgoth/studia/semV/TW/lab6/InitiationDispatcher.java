package pl.com.morgoth.studia.semV.TW.lab6;

import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

public class InitiationDispatcher implements Runnable {

	private Selector selector;
	public static final int LOGGING_SERVER_CONNECTION_PORT = 6000;
	private Map<EventHandler, SelectionKey> registerHandlers = new HashMap<>();
	
	public void init(int port) throws IOException {
		selector = Selector.open();
		Path logPath = FileSystems.getDefault().getPath("logFromServer.log");

		FileChannel fileChannel = FileChannel.open(logPath,
				StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
		LogManager.getLogger(InitiationDispatcher.class).log(Level.ERROR,
				"filechannel: {}", fileChannel);

		new LoggingAcceptor(this, LOGGING_SERVER_CONNECTION_PORT, fileChannel,
				selector);

		LogManager.getLogger(InitiationDispatcher.class).log(Level.INFO,
				"configured");

		Thread worker = new Thread(this);
		worker.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {

			@Override
			public void uncaughtException(Thread arg0, Throwable arg1) {
				LogManager.getLogger(UncaughtExceptionHandler.class).log(
						Level.ERROR, arg1);
			}
		});
		worker.start();
	}

	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				LogManager.getLogger(InitiationDispatcher.class).log(
						Level.INFO, "dispatcher select");
				selector.select();
				Set<SelectionKey> selectedKeys = selector.selectedKeys();
				LogManager.getLogger(InitiationDispatcher.class).log(
						Level.DEBUG, "selected keys {}", selectedKeys);
				Iterator<SelectionKey> it = selectedKeys.iterator();
				while (it.hasNext()) {
					dispatch(it.next());
					it.remove();
				}
			}
		} catch (IOException e) {
			LogManager.getLogger(InitiationDispatcher.class).error("run", e);
		}
		LogManager.getLogger(InitiationDispatcher.class).log(Level.DEBUG,
				"initiation dispatcher stopped");
	}

	private void dispatch(SelectionKey key) {
		((EventHandler) (key.attachment())).handleEvent();
	}

	public static void main(String[] args) throws IOException,
			InterruptedException {
		InitiationDispatcher dispatcher = new InitiationDispatcher();
		dispatcher.init(LOGGING_SERVER_CONNECTION_PORT);
		TimeUnit.MINUTES.sleep(5);
	}

	public void register(EventHandler eventHandler, int key) throws ClosedChannelException {
		SelectionKey sk = eventHandler.getHandle().register(selector, key);
		sk.attach(eventHandler);
		registerHandlers.put(eventHandler, sk);
	}

	public void removeHander(EventHandler handler) {
		registerHandlers.remove(handler).cancel();
	}
}