package pl.com.morgoth.studia.semV.TW.lab1;


public class Decrementator implements Runnable {

	private final MyNumber variable;

	public Decrementator(MyNumber variableToIncrementate) {
		variable = variableToIncrementate;
	}

	@Override
	public void run() {
		for (int i = 0; i < 100000000; ++i) {
			variable.dec();
		}
	}

}
