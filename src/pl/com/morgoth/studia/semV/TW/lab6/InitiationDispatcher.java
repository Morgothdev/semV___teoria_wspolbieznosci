package pl.com.morgoth.studia.semV.TW.lab6;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

public class InitiationDispatcher {

	private Selector selector;
	private ServerSocketChannel serverSocket;

	public void init(int port) throws IOException {
		selector = Selector.open();
		serverSocket = ServerSocketChannel.open();
		serverSocket.socket().bind(new InetSocketAddress(port));
		serverSocket.configureBlocking(false);
		SelectionKey sk = serverSocket.register(selector, SelectionKey.OP_ACCEPT);
		sk.attach(new LoggingAcceptor(serverSocket, selector));
	}
}
