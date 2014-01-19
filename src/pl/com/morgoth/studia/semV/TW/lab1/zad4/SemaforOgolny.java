package pl.com.morgoth.studia.semV.TW.lab1.zad4;

import pl.com.morgoth.studia.semV.TW.lab1.zad3.SemaforBinarny;

public class SemaforOgolny {

	private int aktualnaIloscZasobow;
	private final SemaforBinarny semaforDostepowy = new SemaforBinarny();
	private final SemaforBinarny semaforBlokujacy = new SemaforBinarny();

	public SemaforOgolny(int poczatkowaIloscZasobow) {
		aktualnaIloscZasobow = poczatkowaIloscZasobow;
	}

	public void podnies() {
		semaforDostepowy.podnies();
		if (aktualnaIloscZasobow > 0) {
			if ((--aktualnaIloscZasobow) == 0) {
				semaforBlokujacy.podnies();
			}
		}
		semaforDostepowy.opusc();
		if (aktualnaIloscZasobow == 0) {
			semaforBlokujacy.podnies();
		}
	}

	public void opusc() {
		semaforDostepowy.podnies();
		if((aktualnaIloscZasobow++)==0){
                	semaforBlokujacy.podnies();
                }
		semaforDostepowy.opusc();
	}
}
