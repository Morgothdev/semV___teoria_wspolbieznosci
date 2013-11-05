package pl.com.morgoth.studia.semV.TW.lab5.implementations;

import pl.com.morgoth.studia.semV.TW.lab5.MethodRequest;
import pl.com.morgoth.studia.semV.TW.lab5.RequestBuffer;
import pl.com.morgoth.studia.semV.TW.lab5.Scheduler;

public class DefaultScheduler implements Scheduler {

	private static final int EXECUTORS_COUNT = 1;
	RequestBuffer requestBuffer = new RequestBufferImpl();
	ThreadGroup executors = new ThreadGroup(getClass().getName());

	public DefaultScheduler() {
		for (int i = 0; i < EXECUTORS_COUNT; ++i) {
			new Executor(requestBuffer, executors, "thread" + i);
		}
	}

	@Override
	public void enqueue(MethodRequest methodRequest) {
		requestBuffer.offer(methodRequest);
	}

}
