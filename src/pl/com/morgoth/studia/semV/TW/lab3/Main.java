package pl.com.morgoth.studia.semV.TW.lab3;

import java.util.LinkedList;

public class Main {

	public static final int[] ILOSC_WATKOW = { 10, 30, 50 };

	private static final long[] obciazeniePorownania = { 100, 300, 600 };
	private static final long aktualneObciazenie = obciazeniePorownania[0];

	public static void main(String[] args) {
		uruchomTestZDrobnoziarnistaLista();
		uruchomTestZGruboziarnistaLista();
	}

	private static void uruchomTestZDrobnoziarnistaLista() {
		uruchomTestZLista(new DrobnoZiarnistaLista());
	}

	private static void uruchomTestZGruboziarnistaLista() {
		uruchomTestZLista(new GruboZiarnistaLista());
	}

	private static void uruchomTestZLista(List listaDoTestowania) {
		for (Integer iloscWatkow : ILOSC_WATKOW) {
			for (Long czasOpoznienia : obciazeniePorownania) {
				uruchomTest(listaDoTestowania, iloscWatkow, czasOpoznienia);
			}
		}
	}

	private static void uruchomTest(List lista, Integer iloscWatkow,
			Long czasOpoznienia) {
		LinkedList<Thread> testujace = new LinkedList<Thread>();
		for (int i = 0; i < iloscWatkow; ++i) {
			testujace.add(new WatekTestujacy(lista));
		}
		long startTime = System.currentTimeMillis();
		for (Thread thread : testujace) {
			thread.start();
		}
		for (Thread thread : testujace) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		long endTime = System.currentTimeMillis();
		System.out.println("czas działania dla "
				+ lista.getClass().getSimpleName() + " ilosci watkow "
				+ iloscWatkow + " i czasu opoznieni " + czasOpoznienia
				+ "ms wyniósł: " + (endTime - startTime));

	}

	public static long dajOpoznieniePorownania() {
		return aktualneObciazenie;
	}
}
