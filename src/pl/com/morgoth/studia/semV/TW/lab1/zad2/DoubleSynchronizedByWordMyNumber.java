package pl.com.morgoth.studia.semV.TW.lab1.zad2;

import pl.com.morgoth.studia.semV.TW.lab1.zad1.MyDoubleNumber;

public class DoubleSynchronizedByWordMyNumber extends MyDoubleNumber {

	@Override
	public void inc() {
		synchronized (this) {
			super.inc();
		}
	}

	@Override
	public void dec() {
		synchronized (this) {
			super.dec();
		}
	}
}
