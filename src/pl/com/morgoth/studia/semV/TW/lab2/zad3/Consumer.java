package pl.com.morgoth.studia.semV.TW.lab2.zad3;

import java.util.Queue;
import java.util.concurrent.TimeUnit;

public class Consumer extends User {

	public Consumer(int count, Queue<Integer> queue) {
		super(count, queue);
	}

	@Override
	public void doInRun() throws InterruptedException {
		synchronized (queue) {
			int amountOfProductsToGet = rand.nextInt(count) + 1;
			System.out.println("Konsument chce " + amountOfProductsToGet);
			while (queue.size() < amountOfProductsToGet) {
				queue.wait();
			}
			for (int i = 0; i < amountOfProductsToGet; ++i) {
				queue.poll();
			}
			System.out.println("konsument zdjął z kolejki "
					+ amountOfProductsToGet);
			queue.notifyAll();
			TimeUnit.SECONDS.sleep(rand.nextInt(5) + 2);
		}
	}
}
