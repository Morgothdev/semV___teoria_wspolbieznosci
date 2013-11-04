/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.com.morgoth.studia.semV.TW.lab4.zad1;

import static java.lang.Thread.interrupted;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Student17
 */
public class TesterThread extends Thread {

    static int count = 0;
    int id = count++;
    MonitorDrukarek monitorDrukarek;

    public TesterThread(MonitorDrukarek monitor) {
        monitorDrukarek = monitor;
    }


    @Override
    public void run() {
        try {
            while (!interrupted()) {
                Drukarka drukarka = monitorDrukarek.przydzielMiDrukarke();
                System.out.println("przydzielono "+toString()+" drukarkę " + drukarka);
                drukarka.drukuj();
                monitorDrukarek.zwalniamDrukarke(drukarka);
                System.out.println(toString() +" zwalnia drukarkę " + drukarka);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Zad1Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public String toString(){
        return "wątek"+id;
    }
}
