package pl.com.morgoth.studia.semV.TW.lab2.zad2;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class IntermediaryThread extends Thread {
	private final Element[] buffer;
	private final int index = 0;
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
				// aa
				TimeUnit.SECONDS.sleep(rand.nextInt() % 3 + 1);
			}
		} catch (InterruptedException e) {
		}
	}

}
