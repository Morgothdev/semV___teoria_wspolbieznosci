package pl.com.morgoth.studia.semV.TW.lab3;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.LinkedList;

import org.apache.logging.log4j.LogManager;

public class Main {

	public static final int[] ILOSC_WATKOW = { 5, 10, 30 };

	private static final long[] obciazeniePorownania = { 10, 20, 40 };
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
					LogManager.getLogger(Main.class).error("uncaught exception", arg1);
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
		LogManager.getLogger(Main.class).info(
				lista.getClass().getSimpleName() + ", " + iloscWatkow + ", " + czasOpoznienia + ", "
						+ (endTime - startTime));

	}

	public static long dajOpoznieniePorownania() {
		return aktualneObciazenie;
	}
}
