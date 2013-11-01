package pl.com.morgoth.studia.semV.TW.lab4.zad1;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import org.apache.logging.log4j.LogManager;

public class MonitorDrukarek {

	private final Stack<Drukarka> dostepneDrukarki = new Stack<Drukarka>();
	private final Set<Drukarka> zajeteDrukarki = new HashSet<Drukarka>();

	public MonitorDrukarek(int iloscDrukarek) {
		for (int i = 0; i < iloscDrukarek; i++) {
			dostepneDrukarki.add(new DrukarkaImpl());
		}
	}

	public synchronized Drukarka przydzielMiDrukarke() {
		try {

			while (dostepneDrukarki.isEmpty()) {
				dostepneDrukarki.wait();
			}
			return dostepneDrukarki.pop();

		} catch (InterruptedException e) {
			LogManager.getLogger(getClass()).warn("podczas przydzielania drukarki", e);
			return null;
		}

	}

	public synchronized void zwalniamDrukarke(Drukarka zwalnianaDrukarka) {
		if (zwalnianaDrukarka == null || !zajeteDrukarki.contains(zwalnianaDrukarka)) {
			throw new IllegalArgumentException("obca nierejestrowana drukarka lub null");
		}
		zajeteDrukarki.remove(zwalnianaDrukarka);
		dostepneDrukarki.push(zwalnianaDrukarka);
	}
}
