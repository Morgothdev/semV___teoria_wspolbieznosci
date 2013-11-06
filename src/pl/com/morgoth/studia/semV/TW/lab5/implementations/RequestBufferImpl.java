package pl.com.morgoth.studia.semV.TW.lab5.implementations;

import java.util.concurrent.LinkedBlockingDeque;

import pl.com.morgoth.studia.semV.TW.lab5.MethodRequest;
import pl.com.morgoth.studia.semV.TW.lab5.RequestBuffer;

public class RequestBufferImpl implements RequestBuffer {

	private final LinkedBlockingDeque<MethodRequest> buffer = new LinkedBlockingDeque<MethodRequest>();

	@Override
	public void offer(MethodRequest methodRequest) {
		buffer.add(methodRequest);
	}

	@Override
	public MethodRequest get() throws InterruptedException {
		return buffer.poll();
	}

}
