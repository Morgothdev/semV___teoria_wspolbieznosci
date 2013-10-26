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
			for (int i = 0; i < 10; ++i) {
				lista.add(new Integer(rand.nextInt(20)));
			}
			for (int i = 0; i < 8; ++i) {
				lista.contains(new Integer(rand.nextInt(20)));
			}
			for (int i = 0; i < 20; ++i) {
				lista.remove(new Integer(rand.nextInt(20)));
			}
			for (int i = 0; i < 10; ++i) {
				lista.add(new Integer(rand.nextInt(20)));
			}
			for (int i = 0; i < 20; ++i) {
				lista.remove(new Integer(rand.nextInt(20)));
			}
			for (int i = 0; i < 8; ++i) {
				lista.contains(new Integer(rand.nextInt(20)));
			}
		} catch (InterruptedException e) {

		}
	}

}
