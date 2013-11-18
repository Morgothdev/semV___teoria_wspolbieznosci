package pl.com.morgoth.studia.semV.TW.lab5;

import static java.lang.Thread.interrupted;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.logging.log4j.LogManager;

public class DefaultScheduler implements Scheduler {
    
    private LinkedBlockingDeque<MethodRequest> buffer = new LinkedBlockingDeque<MethodRequest>();
    private final ServantEQBuffer servant = new ServantEQBuffer();
    private Thread executor;
    
    public DefaultScheduler() {
        executor = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!interrupted()) {
                    dispatch();
                }
                
            }
        });
        executor.start();
    }
    
    @Override
    public void enqueue(MethodRequest methodRequest) {
        buffer.offer(methodRequest);
    }
    
    @Override
    public Proxy getProxy() {
        return new Proxy(servant, this);
    }
    
    public void dispatch() {
        try {
            MethodRequest methodToExecute = buffer.take();
            LogManager.getLogger(DefaultScheduler.class).info("method request guard {}",methodToExecute.guard());
            if (methodToExecute.guard()) {
                methodToExecute.call();
            } else {
                buffer.add(methodToExecute);   
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(DefaultScheduler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
