package pl.com.morgoth.studia.semV.TW.lab5;

public interface RequestBuffer {

	public void offer(MethodRequest methodRequest);

	public MethodRequest get() throws InterruptedException;

}
