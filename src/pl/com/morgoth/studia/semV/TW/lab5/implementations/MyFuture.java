package pl.com.morgoth.studia.semV.TW.lab5.implementations;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MyFuture<V> implements Future<V> {

	private enum State {
		WAITING, DONE, CANCELLED
	};

	private V result;
	private State state = State.WAITING;
	private boolean mayInterruptIfIsComputing;

	public synchronized void done(V result) {
		state = State.DONE;
		this.result = result;
		notifyAll();
	}

	@Override
	public synchronized boolean cancel(boolean mayInterruptIfIsComputing) {
		if (state == State.WAITING) {
			state = State.CANCELLED;
			this.mayInterruptIfIsComputing = mayInterruptIfIsComputing;
			notifyAll();
			return true;
		}
		return false;
	}

	@Override
	public synchronized V get() throws InterruptedException, ExecutionException {
		while (!isDone() && !isCancelled()) {
			wait();
		}
		if (isDone()) {
			return result;
		} else {
			throw new CancellationException();
		}
	}

	@Override
	public synchronized V get(long timeout, TimeUnit timeoutUnit) throws InterruptedException, ExecutionException,
			TimeoutException {
		throw new UnsupportedOperationException();
	}

	@Override
	public synchronized boolean isCancelled() {
		return state == State.CANCELLED;
	}

	@Override
	public synchronized boolean isDone() {
		return state == State.DONE;
	}

	public boolean isMayInterruptIfIsComputing() {
		return mayInterruptIfIsComputing;
	}
}
