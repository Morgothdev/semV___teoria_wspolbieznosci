package pl.com.morgoth.studia.semV.TW.lab6;

import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.Pipe;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

public class InitiationDispatcher implements Runnable {

	private Selector selector;
	public static final int LOGGING_SERVER_CONNECTION_PORT = 6000;
	private Map<EventHandler, SelectionKey> registerHandlers = new HashMap<>();
	Set<SelectionKey> deativatedHandlers = Collections
			.newSetFromMap(new ConcurrentHashMap<SelectionKey, Boolean>());

	volatile long startingTime = -1;
	long endTime;
	int count = 0;
	private ExecutorService executor = Executors
			.newFixedThreadPool(R.EXECUTORS_COUNT);

	public void init(int port) throws IOException {
		selector = Selector.open();
		Path logPath = FileSystems.getDefault().getPath("logFromServer.log");

		Pipe pipe = Pipe.open();

		pipe.sink().configureBlocking(false);
		new LoggingAcceptor(this, LOGGING_SERVER_CONNECTION_PORT, pipe.sink());

		new FileAppender(logPath, this, pipe);

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

				selector.select();
				if (startingTime < 0) {
					startingTime = System.currentTimeMillis();
					LogManager.getRootLogger()
							.log(Level.FATAL, "starting time");
				}
				Set<SelectionKey> selectedKeys = selector.selectedKeys();
				Set<EventHandler> selectedHandlers = new HashSet<>();
				for (SelectionKey key : selectedKeys) {
					selectedHandlers.add((EventHandler) key.attachment());
				}
				selectedKeys.removeAll(deativatedHandlers);
				if (selectedKeys.size() == 0) {
					continue;
				}
				LogManager.getLogger(InitiationDispatcher.class).log(
						Level.DEBUG, "selected handlers {}", selectedKeys);

				Iterator<SelectionKey> it = selectedKeys.iterator();
				while (it.hasNext()) {
					SelectionKey nextKey = it.next();
					if (R.USE_EXECUTORS) {
						deactivate(nextKey);
					}
					dispatch(nextKey);
					it.remove();

				}

			}
		} catch (IOException e) {
			LogManager.getLogger(InitiationDispatcher.class).error("run", e);
		}
		LogManager.getLogger(InitiationDispatcher.class).log(Level.DEBUG,
				"initiation dispatcher stopped");
	}

	private void dispatch(final SelectionKey key) {
		if (R.USE_EXECUTORS) {
			executor.execute(new Runnable() {
				@Override
				public void run() {
					((EventHandler) (key.attachment())).handleEvent();
					activate(key);
				}
			});
		} else {
			((EventHandler) (key.attachment())).handleEvent();
		}
		LogManager.getLogger(InitiationDispatcher.class).info("dispached {}",
				key.attachment());
	}

	public static void main(String[] args) throws IOException,
			InterruptedException {
		InitiationDispatcher dispatcher = new InitiationDispatcher();
		dispatcher.init(LOGGING_SERVER_CONNECTION_PORT);
	}

	Lock lock = new ReentrantLock();

	public void register(EventHandler eventHandler, int key)
			throws ClosedChannelException {
		lock.lock();
		try {
			SelectionKey sk = eventHandler.getHandle().register(selector, key);
			sk.attach(eventHandler);
			registerHandlers.put(eventHandler, sk);
			selector.wakeup();
			LogManager.getLogger(InitiationDispatcher.class).info(
					"registered " + eventHandler);
		} finally {
			lock.unlock();
		}
	}

	public void removeHander(EventHandler handler) {
		SelectionKey handlerKey = registerHandlers.remove(handler);
		if (handlerKey != null) {
			handlerKey.cancel();
		}
		// na potrzeby testów
		if (++count == R.SENDERS_COUNT) {
			endTime = System.currentTimeMillis();
			LogManager.getRootLogger().log(Level.FATAL, "time of service {} ",
					endTime - startingTime);
		}
	}

	void deactivate(SelectionKey selectionKey) {
		deativatedHandlers.add(selectionKey);
	}

	void activate(SelectionKey key) {
		deativatedHandlers.remove(key);
	}
}