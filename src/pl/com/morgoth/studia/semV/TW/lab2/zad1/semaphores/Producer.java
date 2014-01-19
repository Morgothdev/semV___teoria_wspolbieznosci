package pl.com.morgoth.studia.semV.TW.lab2.zad1.semaphores;

import java.util.Queue;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Producer extends Thread {

	private final Queue<Integer> queue;
	private final Random rand = new Random(56);
	private final Semaphore accessToQueueSemaphore, elementsCountSemaphore;

	public Producer(Queue<Integer> queue, Semaphore accessToQueueSemaphore,
			Semaphore elementsCountSemaphore) {
		this.queue = queue;
		this.elementsCountSemaphore = elementsCountSemaphore;
		this.accessToQueueSemaphore = accessToQueueSemaphore;
	}

	@Override
	public void run() {
		try {
			while (!interrupted()) {
				accessToQueueSemaphore.acquire();
				Integer product = new Integer(rand.nextInt() % 100);
				System.out.println("Producer offers " + product);
				queue.offer(product);
				elementsCountSemaphore.release();
				accessToQueueSemaphore.release();
				TimeUnit.SECONDS.sleep(rand.nextInt() % 3 + 1);
			}
		} catch (InterruptedException e) {
		}
	}
}
