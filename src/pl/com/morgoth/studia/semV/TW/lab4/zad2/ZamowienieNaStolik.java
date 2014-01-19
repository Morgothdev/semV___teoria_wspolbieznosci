package pl.com.morgoth.studia.semV.TW.lab4.zad2;


public class ZamowienieNaStolik {

	private static final int KOMPLET_OSOB = 2;

	private int iloscObecnych = 0;

	public boolean czyJestKomplet() {
		return iloscObecnych == KOMPLET_OSOB;
	}

	public void osobaStawiaSieICzekaNaKompletOsob(Osoba osobaChcacaStolik) {
		iloscObecnych++;
	}

}
