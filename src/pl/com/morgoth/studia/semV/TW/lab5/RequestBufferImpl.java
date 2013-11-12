package pl.com.morgoth.studia.semV.TW.lab5;

import java.util.concurrent.LinkedBlockingDeque;

public class RequestBufferImpl implements RequestBuffer {

	private final LinkedBlockingDeque<MethodRequest> buffer = new LinkedBlockingDeque<MethodRequest>();

	@Override
	public void offer(MethodRequest methodRequest) {
		buffer.add(methodRequest);
	}

	@Override
	public MethodRequest get() throws InterruptedException {
		return buffer.take();
	}

}
