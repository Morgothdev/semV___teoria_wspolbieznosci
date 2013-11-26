package pl.com.morgoth.studia.semV.TW.lab5;

import static java.lang.Thread.interrupted;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.logging.log4j.LogManager;

public class Scheduler {

	private final LinkedBlockingDeque<MethodRequest> requestQueue = new LinkedBlockingDeque<MethodRequest>();
	private final ServantEQBuffer servant = new ServantEQBuffer();
	private final Thread executor;

	public Scheduler() {
		executor = new Thread(new Runnable() {

			@Override
			public void run() {
				while (!interrupted()) {
					dispatch();
				}

			}
		});
		executor.start();
	}

	public void enqueue(MethodRequest methodRequest) {
		requestQueue.offer(methodRequest);
	}

	public Proxy getProxy() {
		return new Proxy(servant, this);
	}

	public void dispatch() {
		try {
			MethodRequest methodToExecute = requestQueue.take();
			LogManager.getLogger(Scheduler.class).info("method request guard {}", methodToExecute.guard());
			if (methodToExecute.guard()) {
				methodToExecute.call();
			} else {
				requestQueue.add(methodToExecute);
			}
		} catch (InterruptedException ex) {
			Logger.getLogger(Scheduler.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
