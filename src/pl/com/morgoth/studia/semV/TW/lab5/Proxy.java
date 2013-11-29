package pl.com.morgoth.studia.semV.TW.lab5;

import java.util.concurrent.Future;

public class Proxy {

	ServantEQBuffer servant;
	Scheduler scheduler;

	public Proxy(ServantEQBuffer servant, Scheduler scheduler) {
		this.servant = servant;
		this.scheduler = scheduler;
	}

	public Future<Boolean> put(final Object object) {
		final MyFuture<Boolean> futureResult = new MyFuture<Boolean>();
		scheduler.enqueue(new MethodRequest() {

			@Override
			public boolean guard() {
				return (servant.canPut());
			}

			@Override
			public void call() {
				servant.put(object);
				futureResult.setDone(true);
			}
		});
		return futureResult;
	}

	public Future<Object> get() {
		final MyFuture<Object> futureResult = new MyFuture<Object>();

		scheduler.enqueue(new MethodRequest() {

			@Override
			public boolean guard() {
				return (servant.canGet());
			}

			@Override
			public void call() {
				futureResult.setDone(servant.get());
			}
		});
		return futureResult;
	}
}
