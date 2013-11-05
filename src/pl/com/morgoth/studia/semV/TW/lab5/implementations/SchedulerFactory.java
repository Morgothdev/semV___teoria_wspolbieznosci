package pl.com.morgoth.studia.semV.TW.lab5.implementations;

import pl.com.morgoth.studia.semV.TW.lab5.Scheduler;

public class SchedulerFactory {

	private static Scheduler defaultScheduler;

	public static Scheduler getDefaultScheduler() {
		if (defaultScheduler == null) {
			defaultScheduler = new DefaultScheduler();
		}

		return defaultScheduler;
	}

}
