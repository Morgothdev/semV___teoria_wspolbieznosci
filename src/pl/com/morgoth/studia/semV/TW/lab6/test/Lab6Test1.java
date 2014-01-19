package pl.com.morgoth.studia.semV.TW.lab6.test;

import java.net.Socket;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import pl.com.morgoth.studia.semV.TW.lab6.InitiationDispatcher;

public class Lab6Test1 {

	public static void main(String[] args) {
		try {

			Socket s1 = new Socket("localhost", InitiationDispatcher.LOGGING_SERVER_CONNECTION_PORT);
			LogManager.getLogger(Lab6Test1.class).log(Level.DEBUG, "connected: {}, addr: {}, remote: {}",
					s1.isConnected(), s1.getLocalAddress(), s1.getRemoteSocketAddress());
			s1.getOutputStream().write("record from socket1 \"ala ma kota\"".getBytes());
			s1.getOutputStream().flush();

			s1.getOutputStream().write("record from socket1 \"ala ma kota, a kot ma ale\"".getBytes());
			s1.getOutputStream().flush();
			s1.getOutputStream().close();
			s1.close();
		} catch (Exception e) {
			LogManager.getLogger(Lab6Test1.class).log(Level.ERROR, e);
		}
	}
}
