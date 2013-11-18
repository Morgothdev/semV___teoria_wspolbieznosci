package pl.com.morgoth.studia.semV.TW.lab4.zad2;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Kelner {

	private final int[] pary;
	private final Stolik stolik = new StolikImpl();
	private final Lock lock = new ReentrantLock();
	private final Condition monitorStolika = lock.newCondition();
	private final Condition[] monitorPar;
	private int iloscSiedzacychPrzyStoliku = 0;
	private int numerSiedzacychPrzyStoiku = -1;
	private final Logger log = LogManager.getLogger(getClass());

	public Kelner(int iloscPar) {
		pary = new int[iloscPar];
		monitorPar = new Condition[iloscPar];
		for (int i = 0; pary.length > i; ++i) {
			pary[i] = 0;
			monitorPar[i] = lock.newCondition();
		}

	}

	public Stolik chceStolik(Osoba osobaChcacaStolik) throws InterruptedException {
		lock.lock();
		try {
			log.info("osoba numer {} z pary {} wchodzi", osobaChcacaStolik.podajNumerOsoby(),
					osobaChcacaStolik.podajNumer());
			++pary[osobaChcacaStolik.podajNumer()];

			while (pary[osobaChcacaStolik.podajNumer()] < 2) {
				log.info("osoba numer {} z pary {} zasypia czekając na komplet", osobaChcacaStolik.podajNumerOsoby(),
						osobaChcacaStolik.podajNumer());
				monitorPar[osobaChcacaStolik.podajNumer()].await();
				log.info("osoba numer {} z pary {} budzi się czekając na komplet", osobaChcacaStolik.podajNumerOsoby(),
						osobaChcacaStolik.podajNumer());
			}

			monitorPar[osobaChcacaStolik.podajNumer()].signalAll();

			boolean osobaMozeUsiascJesliStolikJestZajety = numerSiedzacychPrzyStoiku >= 0
					&& (osobaChcacaStolik.podajNumer() == numerSiedzacychPrzyStoiku);
			boolean stolikJestPusty = numerSiedzacychPrzyStoiku < 0;
			log.info("osoba numer {} z pary {} próbuje usiaść przy stoliku", osobaChcacaStolik.podajNumerOsoby(),
					osobaChcacaStolik.podajNumer());
			while (!(osobaMozeUsiascJesliStolikJestZajety || stolikJestPusty)) {
				log.info("osoba numer {} z pary {} zasypia czekając na stolik", osobaChcacaStolik.podajNumerOsoby(),
						osobaChcacaStolik.podajNumer());
				monitorStolika.await();
				osobaMozeUsiascJesliStolikJestZajety = numerSiedzacychPrzyStoiku >= 0
						&& (osobaChcacaStolik.podajNumer() == numerSiedzacychPrzyStoiku);
				stolikJestPusty = numerSiedzacychPrzyStoiku < 0;
				log.info("osoba numer {} z pary {} próbuje usiaść przy stoliku", osobaChcacaStolik.podajNumerOsoby(),
						osobaChcacaStolik.podajNumer());
			}
			if (numerSiedzacychPrzyStoiku < 0) {
				log.info("osoba numer {} z pary {} zajęła stolik", osobaChcacaStolik.podajNumerOsoby(),
						osobaChcacaStolik.podajNumer());
				numerSiedzacychPrzyStoiku = osobaChcacaStolik.podajNumer();
			}
			++iloscSiedzacychPrzyStoliku;
                        log.info("osoba numer {} z pary {} jest {} przy stoliku",osobaChcacaStolik.podajNumerOsoby(),osobaChcacaStolik.podajNumer(),(iloscSiedzacychPrzyStoliku==1)?"pierwsza":"druga");
			return stolik;

		} finally {
			lock.unlock();
		}
	}

	public void zwalniamStolik(Osoba osobaZwalniajacaMiejsce) {
		lock.lock();
		try {
			while (osobaZwalniajacaMiejsce.podajNumer() != numerSiedzacychPrzyStoiku) {
				System.out.println("coś nie tak");
			}
			log.info("osoba numer {} z pary {} wstaje od stolika i wychodzi",
					osobaZwalniajacaMiejsce.podajNumerOsoby(), osobaZwalniajacaMiejsce.podajNumer());
			pary[osobaZwalniajacaMiejsce.podajNumer()]--;
			if (--iloscSiedzacychPrzyStoliku <= 0) {
				numerSiedzacychPrzyStoiku = -1;
				monitorStolika.signalAll();
			}
		} finally {
			lock.unlock();
		}
	}
}
