package pl.com.morgoth.studia.semV.TW.lab2.zad2;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class IntermediaryThread extends Thread {
	private final Element[] buffer;
	private int index = 0;
	private final Random rand = new Random(67);
	private final int valueToChanging;

	public IntermediaryThread(Element[] buffer, int valueToChanging) {
		this.buffer = buffer;
		this.valueToChanging = valueToChanging;
	}

	@Override
	public void run() {
		try {
			while (!interrupted()) {
				synchronized (buffer[index]) {
					while (buffer[index].getValue() < valueToChanging) {
						buffer[index].wait();
					}
					if (buffer[index].getValue() > valueToChanging) {
						System.out.println("WTF");
					}
					buffer[index].inc();
					buffer[index].notifyAll();
					System.out.println("zmiana na "
							+ Integer.toString(valueToChanging + 1)
							+ " na pozycji " + Integer.toString(index));
					index = (++index) % buffer.length;
				}
				TimeUnit.SECONDS.sleep(rand.nextInt() % 5 + 5);
			}
		} catch (InterruptedException e) {
		}
	}

}
