package pl.com.morgoth.studia.semV.TW.lab4.zad2;

public class Osoba {

	private final int numer;
	private final int numerOsoby;

	public Osoba(int numerPary, int numerOsoby) {
		this.numer = numerPary;
		this.numerOsoby = numerOsoby;

	}

	public int podajNumer() {
		return numer;
	}

	public int podajNumerOsoby() {
		return numerOsoby;
	}

}
