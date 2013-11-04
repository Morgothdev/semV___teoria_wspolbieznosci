package pl.com.morgoth.studia.semV.TW.lab4.zad2;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ZamowienieNaStolik {

	private static final int KOMPLET_OSOB = 2;

        private int iloscObecnych = 0;

	public void osobaStawiaSieICzekaNaKompletOsob(Osoba osobaChcacaStolik) {
		iloscObecnych++;
	}
        
        public boolean czyJestKomplet(){
            return iloscObecnych==KOMPLET_OSOB;
        }
        
}
