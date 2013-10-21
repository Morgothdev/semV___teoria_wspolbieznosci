package pl.com.morgoth.studia.semV.TW.lab2.zad1.semaphores;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class Zad1MainSemaphores {

	public static void main(String[] args) {
		Queue<Integer> queue = new LinkedList<Integer>();

		Semaphore queueAccessSemaphore = new Semaphore(1);
		Semaphore elementsCountSemaphore = new Semaphore(10);

		Thread producer = new Producer(queue, queueAccessSemaphore,
				elementsCountSemaphore);
		Thread consumer = new Consumer(queue, queueAccessSemaphore,
				elementsCountSemaphore);

		producer.start();
		consumer.start();
	}
}
