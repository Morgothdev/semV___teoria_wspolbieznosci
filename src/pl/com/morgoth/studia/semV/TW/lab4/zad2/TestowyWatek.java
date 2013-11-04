/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.com.morgoth.studia.semV.TW.lab4.zad2;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Student17
 */
class TestowyWatek extends Thread {

    int numer;
    private final Kelner kelner;
    private final Osoba osoba;

    public TestowyWatek(int numerPary, Kelner kelner) {
        numer = numerPary;
        this.kelner=kelner;
        osoba = new Osoba(numer);
        
    }
   @Override
   public void run(){
        try {
            Stolik stolik = kelner.chceStolik(osoba);
            System.out.println("jeden z pary"+numer+" orzymał stolik");
            TimeUnit.SECONDS.sleep(new Random(54).nextInt(10));
            System.out.println("osoba z pary"+numer+" skonczyla przy stoliku");
            kelner.zwalniamStolik(osoba);
        } catch (InterruptedException ex) {
            Logger.getLogger(TestowyWatek.class.getName()).log(Level.SEVERE, null, ex);
        }
       
       
       
       
       
   }
}
