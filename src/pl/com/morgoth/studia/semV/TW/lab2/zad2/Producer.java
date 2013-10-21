package pl.com.morgoth.studia.semV.TW.lab2.zad2;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Producer extends Thread {

	private final Element[] buffer;
	private int index = 0;
	private final int uselessElementValue;
	private final Random rand = new Random(67);

	public Producer(Element[] buffer, int uselessElementValue) {
		this.buffer = buffer;
		this.uselessElementValue = uselessElementValue;
	}

	@Override
	public void run() {
		try {
			while (!interrupted()) {
				synchronized (buffer[index]) {
					while (buffer[index].getValue() < uselessElementValue) {
						buffer[index].wait();
					}
					buffer[index].setValue(0);
					System.out.println("Producer offers at index "
							+ Integer.toString(index));
					buffer[index].notifyAll();
					index = (++index) % buffer.length;
				}
				TimeUnit.SECONDS.sleep(rand.nextInt() % 3 + 1);
			}
		} catch (InterruptedException e) {
		}
	}
}
