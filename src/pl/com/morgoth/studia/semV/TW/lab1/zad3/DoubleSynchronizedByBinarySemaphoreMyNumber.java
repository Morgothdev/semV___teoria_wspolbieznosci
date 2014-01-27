package pl.com.morgoth.studia.semV.TW.lab1.zad3;

import pl.com.morgoth.studia.semV.TW.lab1.zad1.MyDoubleNumber;

public class DoubleSynchronizedByBinarySemaphoreMyNumber extends MyDoubleNumber {

	private final SemaforBinarny semafor = new SemaforBinarny();

	@Override
	public void inc() {
		semafor.podnies();
		super.inc();
		semafor.opusc();
	}

	@Override
	public void dec() {
		semafor.podnies();
		super.dec();
		semafor.opusc();
	}
}