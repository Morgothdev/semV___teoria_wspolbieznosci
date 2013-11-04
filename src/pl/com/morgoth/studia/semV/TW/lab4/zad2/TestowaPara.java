/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.com.morgoth.studia.semV.TW.lab4.zad2;

/**
 *
 * @author Student17
 */
public class TestowaPara {
    
    
    public TestowaPara(int numerPary, Kelner kelner){
        Thread pierwszaOsoba =new TestowyWatek(numerPary,kelner);
        Thread drugaOsoba =  new TestowyWatek(numerPary,kelner);
        
        
        pierwszaOsoba.start();
        drugaOsoba.start();
        
        
    }
}
