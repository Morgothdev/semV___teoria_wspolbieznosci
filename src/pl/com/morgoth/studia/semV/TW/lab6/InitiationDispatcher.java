package pl.com.morgoth.studia.semV.TW.lab6;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Set;

import org.apache.logging.log4j.LogManager;

public class InitiationDispatcher implements Runnable {

	private Selector selector;
	private ServerSocketChannel serverSocket;

	public void init(int port) throws IOException {
		selector = Selector.open();
		serverSocket = ServerSocketChannel.open();
		serverSocket.socket().bind(new InetSocketAddress(port));
		serverSocket.configureBlocking(false);
		Path logPath = FileSystems.getDefault().getPath("logFromServer.log");
		SocketChannel fileChannel = SocketChannel.open();
		FileLogAppender logAppender = new FileLogAppender(selector, logPath, fileChannel);
		SelectionKey sk = serverSocket.register(selector, SelectionKey.OP_ACCEPT);
		sk.attach(new LoggingAcceptor(serverSocket, selector, fileChannel));
	}

	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				selector.select();
				Set<SelectionKey> selectedKeys = selector.selectedKeys();
				Iterator<SelectionKey> it = selectedKeys.iterator();
				while (it.hasNext()) {
					dispatch(it.next());
				}
				selectedKeys.clear();
			}
		} catch (IOException e) {
			LogManager.getLogger(InitiationDispatcher.class).error("run", e);
		}
	}

	private void dispatch(SelectionKey key) {
		((EventHandler) (key.attachment())).handleEvent();
	}
}