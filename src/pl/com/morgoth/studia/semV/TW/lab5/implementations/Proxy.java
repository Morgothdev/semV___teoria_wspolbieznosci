package pl.com.morgoth.studia.semV.TW.lab5.implementations;

import java.util.concurrent.Future;

import pl.com.morgoth.studia.semV.TW.lab5.MethodRequest;
import pl.com.morgoth.studia.semV.TW.lab5.Scheduler;
import pl.com.morgoth.studia.semV.TW.lab5.Servant;

public class Proxy {

	Servant servant = new ServantImpl();
	Scheduler scheduler = SchedulerFactory.getDefaultScheduler();

	public Future<Long> m1() {
		final MyFuture<Long> futureResult = new MyFuture<Long>();

		scheduler.enqueue(new MethodRequest() {

			@Override
			public boolean guard() {
				return !futureResult.isCancelled();
			}

			@Override
			public void call() {
				futureResult.done(servant.m1());
			}
		});
		return futureResult;
	}

	public Future<Object> m2() {
		final MyFuture<Object> futureResult = new MyFuture<Object>();

		scheduler.enqueue(new MethodRequest() {

			@Override
			public boolean guard() {
				return !futureResult.isCancelled();
			}

			@Override
			public void call() {
				futureResult.done(servant.m2());
			}
		});
		return futureResult;
	}

	public Future<Void> m3() {
		final MyFuture<Void> futureResult = new MyFuture<Void>();

		scheduler.enqueue(new MethodRequest() {

			@Override
			public boolean guard() {
				return !futureResult.isCancelled();
			}

			@Override
			public void call() {
				servant.m3();
			}
		});
		return futureResult;
	}

}
