package pl.com.morgoth.studia.semV.TW.lab1.zad1;

import pl.com.morgoth.studia.semV.TW.lab1.MyNumber;

public class MyDoubleNumber implements MyNumber {

	private double variable = 0;

	@Override
	public void inc() {
		++variable;
	}

	@Override
	public void dec() {
		--variable;
	}

	@Override
	public String toString() {
		return Double.toString(variable);
	}
}