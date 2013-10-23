package pl.com.morgoth.studia.semV.TW.lab2.zad2;

public class Zad2Main {

	private static final int MAX_INTERMEDIARY_THREADS = 3;

	public static void main(String[] args) {

		Element[] buffer = new Element[100];
		for (int i = 0; i < 100; ++i) {
			buffer[i] = new Element();
			buffer[i].setValue(MAX_INTERMEDIARY_THREADS + 2);
		}

		Producer producer = new Producer(buffer, MAX_INTERMEDIARY_THREADS + 1);
		producer.start();

		for (int i = 0; i < MAX_INTERMEDIARY_THREADS; ++i) {
			IntermediaryThread intermediary = new IntermediaryThread(buffer, i);
			intermediary.start();
		}
		IntermediaryThread consumer = new IntermediaryThread(buffer,
				MAX_INTERMEDIARY_THREADS);
		consumer.start();

	}
}
