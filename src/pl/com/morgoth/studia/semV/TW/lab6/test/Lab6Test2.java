/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.com.morgoth.studia.semV.TW.lab6.test;

import java.io.IOException;
import java.net.Socket;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import pl.com.morgoth.studia.semV.TW.lab6.InitiationDispatcher;

/**
 * 
 * @author student1
 */
public class Lab6Test2 {


	public static void main(String[] args) throws IOException {
		Socket s2 = new Socket("localhost", InitiationDispatcher.LOGGING_SERVER_CONNECTION_PORT);
		LogManager.getLogger(Lab6Test1.class).log(Level.DEBUG, "connected: {}, addr: {}, remote: {}", s2.isConnected(),
				s2.getLocalAddress(), s2.getRemoteSocketAddress());
		s2.getOutputStream().write("record from socket2 \"kot ma downa\"".getBytes());
		s2.getOutputStream().flush();
		s2.close();
	}
}
