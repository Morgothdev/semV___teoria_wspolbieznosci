package pl.com.morgoth.studia.semV.TW.lab4.zad2;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//import org.apache.logging.log4j.LogManager;
public class Kelner {

    private final int[] pary;
    private Stolik stolik = new StolikImpl();
    private Lock lock = new ReentrantLock();
    private Condition monitorStolika = lock.newCondition();
    private int iloscSiedzacychPrzyStoliku = 0;
    private int numerSiedzacychPrzyStoiku = -1;

    public Kelner(int iloscPar) {
        pary = new int[iloscPar];
        for(int i=0;pary.length>i;++i){
            pary[i]=0;
        }
    }

    public Stolik chceStolik(Osoba osobaChcacaStolik) throws InterruptedException {
        lock.lock();
        try {
            ++pary[osobaChcacaStolik.podajNumer()];
            int iloscCzekajacychZDanejPary = pary[osobaChcacaStolik.podajNumer()];
            
            while (iloscCzekajacychZDanejPary<2) {
                monitorStolika.await();
            }
            
            monitorStolika.signalAll();
            //stolik jest wolny

            boolean osobaMozeUsiascJesliStolikJestZajety = numerSiedzacychPrzyStoiku >= 0 && (osobaChcacaStolik.podajNumer() == numerSiedzacychPrzyStoiku);
            boolean stolikJestPusty = numerSiedzacychPrzyStoiku < 0;
            
            while (!(osobaMozeUsiascJesliStolikJestZajety || stolikJestPusty)) {
                System.out.println("zasypouiam");
                monitorStolika.await();
                osobaMozeUsiascJesliStolikJestZajety = numerSiedzacychPrzyStoiku >= 0 && (osobaChcacaStolik.podajNumer() == numerSiedzacychPrzyStoiku);
                stolikJestPusty = numerSiedzacychPrzyStoiku < 0;
            }
            if (numerSiedzacychPrzyStoiku < 0) {
                numerSiedzacychPrzyStoiku = osobaChcacaStolik.podajNumer();
            }
            ++iloscSiedzacychPrzyStoliku;
            return stolik;
            
        } finally {
            lock.unlock();
        }
    }

    public void zwalniamStolik(Osoba osobaZwalniajacaMiejsce) {
        lock.lock();
        try {
            while (osobaZwalniajacaMiejsce.podajNumer() != numerSiedzacychPrzyStoiku) {
                System.out.println("coś nie tak");
            }
            pary[osobaZwalniajacaMiejsce.podajNumer()]--;
            if (--iloscSiedzacychPrzyStoliku <= 0) {
                numerSiedzacychPrzyStoiku = -1;
                monitorStolika.signalAll();
            }
        } finally {
            lock.unlock();
        }
    }
}
