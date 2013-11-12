package pl.com.morgoth.studia.semV.TW.lab5;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

public class Lab5Test {

	public static void main(String[] args) {

		LogManager.getLogger(Lab5Test.class).log(Level.INFO, "TEST START");

		final Scheduler scheduer = SchedulerFactory.getDefaultScheduler();
		final Servant servant = new ServantImpl();

		Runnable generatorRunnable = new Runnable() {

			@Override
			public void run() {
				Proxy proxy = new Proxy(servant, scheduer);
				try {
					while (!Thread.interrupted()) {
						for (int i = 0; i < 40; ++i) {
							switch (i % 3) {
							case 0:
								Future<Long> resultLong = proxy.m1();
								LogManager.getLogger(Lab5Test.class).log(Level.INFO, "Returned from m1 {}",
										resultLong.get());
								break;
							case 1:
								Future<Object> resultObj = proxy.m2();
								LogManager.getLogger(Lab5Test.class).log(Level.INFO, "Returned from m2 {}",
										resultObj.get());
								break;
							case 2:
								Future<Void> resultVoid = proxy.m3();
								LogManager.getLogger(Lab5Test.class).log(Level.INFO, "m3 executed");
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
