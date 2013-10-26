package pl.com.morgoth.studia.semV.TW.lab3;

public interface List {

	public boolean add(Object newObject) throws InterruptedException;

	public boolean remove(Object objectToRemove) throws InterruptedException;

	public boolean contains(Object object) throws InterruptedException;

}