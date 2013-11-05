package pl.com.morgoth.studia.semV.TW.lab5.implementations;

import pl.com.morgoth.studia.semV.TW.lab5.MethodRequest;
import pl.com.morgoth.studia.semV.TW.lab5.RequestBuffer;

public class Executor extends Thread {

	private final RequestBuffer requestBuffer;

	public Executor(RequestBuffer requestBuffer, ThreadGroup threadGroup, String name) {
		super(threadGroup, name);
		this.requestBuffer = requestBuffer;
	}

	@Override
	public void run() {
		try {
			while (!interrupted()) {
				MethodRequest methodToExecute = requestBuffer.get();
				if (methodToExecute.guard()) {
					methodToExecute.call();
				}
			}
		} catch (InterruptedException e) {

		}
	}
}
