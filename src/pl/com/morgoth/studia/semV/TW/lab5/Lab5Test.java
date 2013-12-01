package pl.com.morgoth.studia.semV.TW.lab5;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

public class Lab5Test {

	public static void main(String[] args) {

		LogManager.getLogger(Lab5Test.class).log(Level.INFO, "TEST START");

		final Scheduler scheduer = new Scheduler();

		Runnable generatorRunnable = new Runnable() {

			@Override
			public void run() {
				Proxy proxy = scheduer.getProxy();
				Random rand = new Random(System.currentTimeMillis());
				try {
					for (int i = 0; i < 40; ++i) {
						int random = Math.abs(rand.nextInt());
						switch (random % 2) {
						case 0:
							Future<Boolean> tryPut = proxy.put(new Object());
							LogManager.getLogger(Lab5Test.class).log(Level.INFO, "Returned from put {}", tryPut.get());
							break;
						case 1:
							LogManager.getLogger(Lab5Test.class).log(Level.INFO, "Try get");
							Future<Object> resultObj = proxy.get();
							LogManager.getLogger(Lab5Test.class).log(Level.INFO, "Returned from get {}",
									resultObj.get());
							break;
						default:
							break;
						}
						if (Thread.interrupted()) {
							break;
						}
					}

				} catch (InterruptedException e) {

				} catch (ExecutionException e) {
					LogManager.getLogger(Lab5Test.class).error("run", e);
				}
			}
		};

		try {
			Thread t1 = new Thread(generatorRunnable);
			TimeUnit.SECONDS.sleep(1);
			Thread t2 = new Thread(generatorRunnable);
			TimeUnit.SECONDS.sleep(1);
			Thread t3 = new Thread(generatorRunnable);

			t1.start();
			t2.start();
			t3.start();

			t1.join();
			t2.join();
			t3.join();
		} catch (InterruptedException e) {
			LogManager.getLogger(Lab5Test.class).error("main", e);
		}

		LogManager.getLogger(Lab5Test.class).log(Level.INFO, "TEST END");

	}
}
