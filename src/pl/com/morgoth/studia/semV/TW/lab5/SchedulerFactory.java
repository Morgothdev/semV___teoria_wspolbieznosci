package pl.com.morgoth.studia.semV.TW.lab5;


public class SchedulerFactory {

	private static Scheduler defaultScheduler;

	public static Scheduler getDefaultScheduler() {
		if (defaultScheduler == null) {
			defaultScheduler = new DefaultScheduler();
		}

		return defaultScheduler;
	}

}
