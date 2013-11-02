package pl.com.morgoth.studia.semV.TW.lab4.zad2;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ZamowienieNaStolik {

	private static final int KOMPLET_OSOB = 2;

	private final Lock lock = new ReentrantLock();
	private final Condition monitorOczekujacych = lock.newCondition();
	private int iloscObecnych = 0;

	public void osobaStawiaSieICzekaNaKompletOsob(Osoba osobaChcacaStolik) {
		lock.lock();
		iloscObecnych++;
		if (iloscObecnych == KOMPLET_OSOB) {
			monitorOczekujacych.signalAll();
		}
		lock.unlock();
	}
}
