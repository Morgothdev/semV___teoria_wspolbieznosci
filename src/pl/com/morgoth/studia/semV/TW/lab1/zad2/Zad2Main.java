package pl.com.morgoth.studia.semV.TW.lab1.zad2;

import pl.com.morgoth.studia.semV.TW.lab1.Checker;

public class Zad2Main {

	public static void main(String[] args) {
		Checker.check(new DoubleSynchronizedByWordMyNumber());
		Checker.check(new DoubleSynchronizedByLocksMyNumber());
	}
}
