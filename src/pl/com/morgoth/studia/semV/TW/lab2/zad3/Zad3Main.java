package pl.com.morgoth.studia.semV.TW.lab2.zad3;

import java.util.LinkedList;
import java.util.Queue;

public class Zad3Main {

	public static final int M = 10;

	public static void main(String[] args) {
		Queue<Integer> queue = new LinkedList<Integer>();
		Producent[] producents = { new Producent(M, queue),
				new Producent(M, queue), new Producent(M, queue),
				new Producent(M, queue), new Producent(M, queue) };

		Consumer[] consumers = { new Consumer(M, queue),
				new Consumer(M, queue), new Consumer(M, queue),
				new Consumer(M, queue), new Consumer(M, queue),
				new Consumer(M, queue) };

		for (Producent p : producents) {
			p.start();
		}
		for (Consumer c : consumers) {
			c.start();
		}

	}
}
