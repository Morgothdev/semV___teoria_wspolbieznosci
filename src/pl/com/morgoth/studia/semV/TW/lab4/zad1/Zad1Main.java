/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.com.morgoth.studia.semV.TW.lab4.zad1;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Student17
 */
public class Zad1Main {

    public static void main(String[] args) {
        final MonitorDrukarek monitorDrukarek = new MonitorDrukarek(3);
        Thread[] threads = new Thread[5];
        for (int i = 0; i < 5; ++i) {
            threads[i] = new TesterThread(monitorDrukarek);
        }
        for(Thread t : threads){
            t.start();
        }
    }
}
