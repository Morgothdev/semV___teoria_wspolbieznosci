/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.com.morgoth.studia.semV.TW.lab6.test;

import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.Socket;
import java.util.Random;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import pl.com.morgoth.studia.semV.TW.lab6.InitiationDispatcher;
import pl.com.morgoth.studia.semV.TW.lab6.R;

/**
 * 
 * @author student1
 */
public class Lab6Test3 implements Runnable {

	public static void main(String[] args) throws IOException {
		Thread[] threads = new Thread[R.SENDERS_COUNT];
		for (int i = 0; i < R.SENDERS_COUNT; ++i) {
			Lab6Test3 runnable = new Lab6Test3();
			threads[i] = new Thread(runnable);
			threads[i].setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
				
				@Override
				public void uncaughtException(Thread t, Throwable e) {
					e.printStackTrace();
				}
			});
			threads[i].start();
		}
		for (int i = 0; i < threads.length; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				LogManager.getLogger(UncaughtExceptionHandler.class).log(
						Level.ERROR, e);
			}
		}
	}

	public void run() {
		Socket s2;
		try {
			s2 = new Socket("localhost",
					InitiationDispatcher.LOGGING_SERVER_CONNECTION_PORT);

			LogManager.getLogger(Lab6Test1.class).log(Level.DEBUG,
					"connected: {}, addr: {}, remote: {}", s2.isConnected(),
					s2.getLocalAddress(), s2.getRemoteSocketAddress());
			for (long i = 0; i < R.REQUESTS_COUNT; ++i) {
				s2.getOutputStream().write(randomString(400).getBytes());
				s2.getOutputStream().flush();
			}

			s2.close();
		} catch (IOException e) {
		}
	}
		
	public String randomString(int length){
		char[] chars = "abcdefghijklmnopqrstuvwxyz!@$%^&*()_+}{:|?><,./;'123456789`~]['=-".toCharArray();
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
		    char c = chars[random.nextInt(chars.length)];
		    sb.append(c);
		}
		return sb.toString();
	}
}
