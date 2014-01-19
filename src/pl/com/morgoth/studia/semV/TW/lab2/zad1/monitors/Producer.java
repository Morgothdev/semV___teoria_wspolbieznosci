package pl.com.morgoth.studia.semV.TW.lab2.zad1.monitors;

import java.util.Queue;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Producer extends Thread {

	private final Queue<Integer> queue;
	private final Random rand = new Random(56);
	private final int maxElements;

	public Producer(Queue<Integer> queue, int maxElements) {
		this.queue = queue;
		this.maxElements = maxElements;
	}

	@Override
	public void run() {
		try {
			while (!interrupted()) {
				synchronized (queue) {
					while (queue.size() >= maxElements) {
						queue.wait();
					}
					Integer product = new Integer(rand.nextInt() % 100);
					System.out.println("Producer offers " + product);
					queue.offer(product);
					queue.notifyAll();

				}
				TimeUnit.SECONDS.sleep(rand.nextInt() % 3 + 1);
			}
		} catch (InterruptedException e) {
		}
	}
}
