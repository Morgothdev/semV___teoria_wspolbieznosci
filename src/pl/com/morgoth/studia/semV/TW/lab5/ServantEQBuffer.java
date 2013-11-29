package pl.com.morgoth.studia.semV.TW.lab5;

import org.apache.logging.log4j.LogManager;

public class ServantEQBuffer {

	private final int BUFFER_SIZE = 10;
	private Object[] data = new Object[BUFFER_SIZE];
	private int count = 0;

	public boolean canPut() {
		return count < BUFFER_SIZE;
	}

	public boolean canGet() {
		return count > 0;
	}

	public void put(Object object) {
		LogManager.getLogger(ServantEQBuffer.class).info("putted {}", object);
		data[count++] = object;
	}

	public Object get() {
		LogManager.getLogger(ServantEQBuffer.class).info("getted {}",
				data[count - 1]);
		return data[--count];
	}
}
