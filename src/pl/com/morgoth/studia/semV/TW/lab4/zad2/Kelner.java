package pl.com.morgoth.studia.semV.TW.lab4.zad2;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.logging.log4j.LogManager;

public class Kelner {

	private final ZamowienieNaStolik[] pary;
	private Stolik stolik;

	private Lock lock = new ReentrantLock();
	private Condition monitorStolika = lock.newCondition();
	private int iloscSiedzacychPrzyStoliku = 0;
	private int numerSiedzacychPrzyStoiku = -1;

	public Kelner(int iloscPar) {
		pary = new ZamowienieNaStolik[iloscPar];
	}

	public Stolik chceStolik(Osoba osobaChcacaStolik) throws InterruptedException {
		osobaOczekujeNaKompletDoStolika(osobaChcacaStolik);
		osobaOczekujaNaZwolnienieStolik(osobaChcacaStolik);
		return stolik;
	}

	private void osobaOczekujeNaKompletDoStolika(Osoba osobaChcacaStolik) throws InterruptedException {
		ZamowienieNaStolik zamownienieNaStolik = pary[osobaChcacaStolik.podajNumer()];
		zamownienieNaStolik.osobaStawiaSieICzekaNaKompletOsob(osobaChcacaStolik);
	}

	private void osobaOczekujaNaZwolnienieStolik(Osoba osobaChcacaStolik) throws InterruptedException {
		lock.lock();
		while ((osobaChcacaStolik.podajNumer() != numerSiedzacychPrzyStoiku) && numerSiedzacychPrzyStoiku < 0) {
			monitorStolika.await();
		}
		if (numerSiedzacychPrzyStoiku < 0) {
			numerSiedzacychPrzyStoiku = osobaChcacaStolik.podajNumer();
		}
		++iloscSiedzacychPrzyStoliku;
		lock.unlock();
	}

	public void zwalniamStolik(Osoba osobaZwalniajacaMiejsce) {
		lock.lock();
		while (osobaZwalniajacaMiejsce.podajNumer() != numerSiedzacychPrzyStoiku) {
			LogManager.getLogger(Kelner.class).error("osoba {} próbuje zwolnić miejsce, a nie siedzi przy stoliku {}",
					osobaZwalniajacaMiejsce, stolik);
		}
		if (--iloscSiedzacychPrzyStoliku <= 0) {
			numerSiedzacychPrzyStoiku = -1;
			monitorStolika.signalAll();
		}
		lock.unlock();
	}
}
