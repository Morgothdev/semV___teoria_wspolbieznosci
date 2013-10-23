package pl.com.morgoth.studia.semV.TW.lab2.zad3;

import java.util.Queue;
import java.util.Random;

public abstract class User extends Thread {

	protected final int count;
	protected final Queue<Integer> queue;
	protected Random rand = new Random(23);

	public User(int count, Queue<Integer> queue) {
		this.count = count;
		this.queue = queue;
	}

	@Override
	public void run() {
		try {
			while (!interrupted()) {
				doInRun();
			}
		} catch (InterruptedException e) {

		}
	}

	public abstract void doInRun() throws InterruptedException;

}