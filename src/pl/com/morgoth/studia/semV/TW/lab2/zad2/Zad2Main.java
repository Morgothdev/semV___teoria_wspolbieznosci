package pl.com.morgoth.studia.semV.TW.lab2.zad2;

public class Zad2Main {

	private static final int MAX_INTERMEDIARY_THREADS = 0;

	public static void main(String[] args) {

		Element[] buffer = new Element[100];

		Producer producer = new Producer(buffer, MAX_INTERMEDIARY_THREADS + 1);
		producer.start();
		for (int i = 0; i < MAX_INTERMEDIARY_THREADS; ++i) {
			IntermediaryThread intermediary = new IntermediaryThread(buffer, i);
			intermediary.start();
		}
		Consumer consumer = new Consumer(buffer, MAX_INTERMEDIARY_THREADS);
		consumer.start();

	}
}
