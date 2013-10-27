package pl.com.morgoth.studia.semV.TW.lab3;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.LinkedList;

import org.apache.logging.log4j.LogManager;

public class Main {

	public static final int[] ILOSC_WATKOW = { 10, 30, 50 };

	private static final long[] obciazeniePorownania = { 10, 30, 50 };
	private static final long aktualneObciazenie = obciazeniePorownania[0];

	public static void main(String[] args) {
		LogManager.getRootLogger().warn("DUPA");
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

	private static void uruchomTest(List lista, Integer iloscWatkow, Long czasOpoznienia) {
		LinkedList<Thread> testujace = new LinkedList<Thread>();
		for (int i = 0; i < iloscWatkow; ++i) {
			testujace.add(new WatekTestujacy(lista));
		}
		long startTime = System.currentTimeMillis();
		for (Thread thread : testujace) {
			thread.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {

				@Override
				public void uncaughtException(Thread arg0, Throwable arg1) {
					System.out.println(arg0.toString());

				}
			});
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
		System.out.println(lista.getClass().getSimpleName() + ", " + iloscWatkow + ", " + czasOpoznienia + ", "
				+ (endTime - startTime));

	}

	public static long dajOpoznieniePorownania() {
		return aktualneObciazenie;
	}
}
