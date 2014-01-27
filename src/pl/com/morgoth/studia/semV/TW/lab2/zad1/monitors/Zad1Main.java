package pl.com.morgoth.studia.semV.TW.lab2.zad1.monitors;

import java.util.LinkedList;
import java.util.Queue;


public class Zad1Main {

	private static int MAX_ELEMENTS = 20;

	public static void main(String[] args) {
		Queue<Integer> queue = new LinkedList<Integer>();

		Thread producer = new Producer(queue, MAX_ELEMENTS);
		Thread consumer = new Consumer(queue);

		producer.start();
		consumer.start();
	}
}