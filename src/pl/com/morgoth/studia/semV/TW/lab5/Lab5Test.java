package pl.com.morgoth.studia.semV.TW.lab5;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

public class Lab5Test {

	public static void main(String[] args) {

		LogManager.getLogger(Lab5Test.class).log(Level.INFO, "TEST START");

		final Scheduler scheduer = SchedulerFactory.getDefaultScheduler();
		

		Runnable generatorRunnable = new Runnable() {

			@Override
			public void run() {
				Proxy proxy = scheduer.getProxy();
				try {
					while (!Thread.interrupted()) {
						for (int i = 0; i < 40; ++i) {
							switch (new Random(43).nextInt(2)) {
							case 0:
								Future<Boolean> tryPut = proxy.put(new Object());
								LogManager.getLogger(Lab5Test.class).log(Level.INFO, "Returned from put {}",
										tryPut.get());
								break;
							case 1:
								Future<Object> resultObj = proxy.get();
								LogManager.getLogger(Lab5Test.class).log(Level.INFO, "Returned from get {}",
										resultObj.get());
								break;
							default:
								break;
							}
						}
					}
				} catch (InterruptedException e) {

				} catch (ExecutionException e) {
					LogManager.getLogger(Lab5Test.class).error("run", e);
				}
			}
		};

		Thread t1 = new Thread(generatorRunnable);
		Thread t2 = new Thread(generatorRunnable);
		Thread t3 = new Thread(generatorRunnable);

		t1.start();
		t2.start();
		t3.start();

		try {
			t1.join();
			t2.join();
			t3.join();
		} catch (InterruptedException e) {
			LogManager.getLogger(Lab5Test.class).error("main", e);
		}

		LogManager.getLogger(Lab5Test.class).log(Level.INFO, "TEST END");

	}
}
