package pl.com.morgoth.studia.semV.TW.lab4.zad1;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class DrukarkaImpl implements Drukarka {

	private static long count = 0;
	private long id = count++;

	public void drukuj() throws InterruptedException {
		System.out.println("Drukuję na " + toString());
		TimeUnit.SECONDS.sleep(new Random(45).nextInt(10));
	}

	@Override
	public String toString() {
		return "drukarka" + id;
	}
}
