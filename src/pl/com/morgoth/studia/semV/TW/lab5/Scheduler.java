package pl.com.morgoth.studia.semV.TW.lab5;


public interface Scheduler {

	public Proxy getProxy();
        public void enqueue(MethodRequest methodRequest);
        
}
