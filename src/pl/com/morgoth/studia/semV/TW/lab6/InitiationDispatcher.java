package pl.com.morgoth.studia.semV.TW.lab6;

import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

public class InitiationDispatcher implements Runnable {

	private Selector selector;
	private ServerSocketChannel serverSocket;
	public static final int LOGGING_SERVER_CONNECTION_PORT = 6000;

	public void init(int port) throws IOException {
		selector = Selector.open();
		serverSocket = ServerSocketChannel.open();
		serverSocket.socket().bind(new InetSocketAddress(port));
		serverSocket.configureBlocking(false);
		Path logPath = FileSystems.getDefault().getPath("logFromServer.log");

		FileChannel fileChannel = FileChannel.open(logPath, StandardOpenOption.WRITE,
				StandardOpenOption.TRUNCATE_EXISTING);
		LogManager.getLogger(InitiationDispatcher.class).log(Level.ERROR, "filechannel: {}", fileChannel);

		SelectionKey sk = serverSocket.register(selector, SelectionKey.OP_ACCEPT);
		LogManager.getLogger(InitiationDispatcher.class).log(Level.DEBUG, "accpetor key {}", sk);
		sk.attach(new LoggingAcceptor(serverSocket, selector, fileChannel));
		LogManager.getLogger(InitiationDispatcher.class).log(Level.INFO, "configured");

		Thread worker = new Thread(this);
		worker.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {

			@Override
			public void uncaughtException(Thread arg0, Throwable arg1) {
				LogManager.getLogger(UncaughtExceptionHandler.class).log(Level.ERROR, arg1);
			}
		});
		worker.start();
	}

	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				LogManager.getLogger(InitiationDispatcher.class).log(Level.INFO, "dispatcher select");
				selector.select();
				Set<SelectionKey> selectedKeys = selector.selectedKeys();
				LogManager.getLogger(InitiationDispatcher.class).log(Level.DEBUG, "selected keys {}", selectedKeys);
				Iterator<SelectionKey> it = selectedKeys.iterator();
				while (it.hasNext()) {
					dispatch(it.next());
					it.remove();
				}
			}
		} catch (IOException e) {
			LogManager.getLogger(InitiationDispatcher.class).error("run", e);
		}
		LogManager.getLogger(InitiationDispatcher.class).log(Level.DEBUG, "initiation dispatcher stopped");
	}

	private void dispatch(SelectionKey key) {
		((EventHandler) (key.attachment())).handleEvent();
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		InitiationDispatcher dispatcher = new InitiationDispatcher();
		dispatcher.init(LOGGING_SERVER_CONNECTION_PORT);
		TimeUnit.MINUTES.sleep(5);
	}
}