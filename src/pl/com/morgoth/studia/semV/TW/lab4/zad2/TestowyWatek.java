/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.com.morgoth.studia.semV.TW.lab4.zad2;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author Student17
 */
class TestowyWatek extends Thread {

	int numer;
	private final Kelner kelner;
	private final Osoba osoba;
	private final int numerOsoby;

	public TestowyWatek(int numerPary, int numerOsoby, Kelner kelner) {
		numer = numerPary;
		this.numerOsoby = numerOsoby;
		this.kelner = kelner;
		osoba = new Osoba(numer, numerOsoby);

	}

	@Override
	public void run() {
		try {
			while (!interrupted()) {
				Stolik stolik = kelner.chceStolik(osoba);
				System.out.println("jeden z pary" + numer + " orzymał stolik");
				TimeUnit.SECONDS.sleep(new Random(54).nextInt(5) + 20);
				System.out.println("osoba z pary" + numer + " skonczyla przy stoliku");
				kelner.zwalniamStolik(osoba);
				TimeUnit.SECONDS.sleep(4);
			}
		} catch (InterruptedException ex) {
			Logger.getLogger(TestowyWatek.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	public String toString() {
		return "watek" + numer + " osoba" + numerOsoby;
	}
}
