package pl.com.morgoth.studia.semV.TW.lab2.zad3;

import java.util.Queue;
import java.util.concurrent.TimeUnit;

public class Producent extends User {

	public Producent(int count, Queue<Integer> queue) {
		super(count, queue);
	}

	@Override
	public void doInRun() throws InterruptedException {
		synchronized (queue) {
			int amountOfNewProducts = rand.nextInt(count) + 1;
			System.out.println("Producent chce " + amountOfNewProducts);
			while ((2 * count - queue.size()) < amountOfNewProducts) {
				queue.wait();
			}
			for (int i = 0; i < amountOfNewProducts; ++i) {
				queue.offer(new Integer(rand.nextInt(400)));
			}
			System.out.println("producent włożył do kolejki "
					+ amountOfNewProducts);
			queue.notifyAll();
			TimeUnit.SECONDS.sleep(rand.nextInt(2));
		}
	}

}
