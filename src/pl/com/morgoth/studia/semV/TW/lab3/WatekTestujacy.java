package pl.com.morgoth.studia.semV.TW.lab3;

import java.util.Random;

public class WatekTestujacy extends Thread {

	private final List lista;
	private final Random rand = new Random(432);

	public WatekTestujacy(List lista) {
		this.lista = lista;
	}

	@Override
	public void run() {
		try {
			for (int i = 0; i < 1; ++i) {
				Integer number = new Integer(rand.nextInt(20));
				// System.out.println("probuje wstawic liczbe " + number);
				lista.add(number);
			}
			for (int i = 0; i < 1; ++i) {
				Integer number = new Integer(rand.nextInt(20));
				// System.out.println("szukam liczby " + number);
				lista.contains(number);
			}
			for (int i = 0; i <1; ++i) {
				Integer number = new Integer(rand.nextInt(20));
				// System.out.println("usuwam liczbe " + number);
				lista.remove(number);
			}
		} catch (InterruptedException e) {

		}
	}
}
