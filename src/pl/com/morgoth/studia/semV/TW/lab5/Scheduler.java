package pl.com.morgoth.studia.semV.TW.lab5;

import static java.lang.Thread.interrupted;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Scheduler {

	private final LinkedBlockingDeque<MethodRequest> buffer = new LinkedBlockingDeque<MethodRequest>();
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
		buffer.addLast(methodRequest);
	}

	public Proxy getProxy() {
		return new Proxy(servant, this);
	}

	public void dispatch() {
		try {
			MethodRequest methodToExecute = buffer.take();
			if (methodToExecute.guard()) {
				methodToExecute.call();
			} else {
				buffer.addLast(methodToExecute);
			}
		} catch (InterruptedException ex) {
			Logger.getLogger(Scheduler.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
