package pl.com.morgoth.studia.semV.TW.lab2.zad1.monitors;

import java.util.Queue;

public class Consumer extends Thread {

	private final Queue<Integer> queue;

	public Consumer(Queue<Integer> queue) {
		this.queue = queue;
	}

	@Override
	public void run() {
		try {
			while (!interrupted()) {
				synchronized (queue) {
					while (queue.isEmpty()) {
						queue.wait();
					}
					Integer product = queue.poll();
					System.out.println("Consumer consumes " + product);
					queue.notifyAll();
				}
			}
		} catch (InterruptedException e) {
		}
	}
}
