package pl.com.morgoth.studia.semV.TW.lab1;


public class Incrementator implements Runnable {

	private final MyNumber variable;

	public Incrementator(MyNumber variableToIncrementate) {
		variable = variableToIncrementate;
	}

	@Override
	public void run() {
		for (int i = 0; i < 100000000; ++i) {
			variable.inc();
		}
	}

}
