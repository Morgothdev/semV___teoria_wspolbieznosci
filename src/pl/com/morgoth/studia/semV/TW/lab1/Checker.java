package pl.com.morgoth.studia.semV.TW.lab1;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Checker {

	public static void check(MyNumber variable) {
		for (int i = 0; i < 20; ++i) {

			Thread decrementator = new Thread(new Decrementator(variable));
			Thread incrementator = new Thread(new Incrementator(variable));

			decrementator.start();
			incrementator.start();
                        try {                        
                            incrementator.join();
                            decrementator.join();
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Checker.class.getName()).log(Level.SEVERE, null, ex);
                        }
			
			System.out.println(i + " round: " + variable);
		}
	}

	public Checker() {
		super();
	}

}