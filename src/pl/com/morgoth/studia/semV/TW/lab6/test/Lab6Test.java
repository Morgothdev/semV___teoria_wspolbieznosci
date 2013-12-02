package pl.com.morgoth.studia.semV.TW.lab6.test;

import java.net.Socket;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import pl.com.morgoth.studia.semV.TW.lab6.InitiationDispatcher;

public class Lab6Test {

	private static final int LOGGING_SERVER_CONNECTION_PORT = 6000;

	public static void main(String[] args) {
		try {
			InitiationDispatcher dispatcher = new InitiationDispatcher();
			dispatcher.init(LOGGING_SERVER_CONNECTION_PORT);

			Socket s1 = new Socket("localhost", LOGGING_SERVER_CONNECTION_PORT);
			LogManager.getLogger(Lab6Test.class).log(Level.DEBUG, "connected: {}, addr: {}, remote: {}",
					s1.isConnected(), s1.getLocalAddress(), s1.getRemoteSocketAddress());
			s1.getOutputStream().write("record from socket1 \"ala ma kota\"".getBytes());
			s1.getOutputStream().flush();

			s1.getOutputStream().write("record from socket1 \"ala ma kota, a kot ma ale\"".getBytes());
			s1.getOutputStream().flush();

                        s1.close();
		} catch (Exception e) {
			LogManager.getLogger(Lab6Test.class).log(Level.ERROR, e);
		}
	}
}
