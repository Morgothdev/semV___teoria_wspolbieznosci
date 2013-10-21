package pl.com.morgoth.studia.semV.TW.lab1.zad2;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import pl.com.morgoth.studia.semV.TW.lab1.zad1.MyDoubleNumber;

public class DoubleSynchronizedByLocksMyNumber extends MyDoubleNumber {

	private final Lock lock = new ReentrantLock();

	@Override
	public void inc() {
		lock.lock();
		super.inc();
		lock.unlock();
	}

	@Override
	public void dec() {
		lock.lock();
		super.dec();
		lock.unlock();
	}
}
