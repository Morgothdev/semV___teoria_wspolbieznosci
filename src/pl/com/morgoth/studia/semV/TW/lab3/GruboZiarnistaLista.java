package pl.com.morgoth.studia.semV.TW.lab3;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GruboZiarnistaLista implements List {

	private final Node head = new Node(null, null);
	private final Lock lock = new ReentrantLock();

	@Override
	public boolean add(Object newObject) throws InterruptedException {
		lock.lock();
		Node next = head;
		Node prev = next;
		next = prev.next;
		while (next != null) {
			TimeUnit.MILLISECONDS.sleep(Main.dajOpoznieniePorownania());
			prev = next;
			next = next.next;
		}
		prev.next = new Node(newObject, null);
		lock.unlock();
		return true;
	}

	@Override
	public boolean remove(Object objectToRemove) throws InterruptedException {
		lock.lock();
		Node next = head;
		Node prev = next;
		next = prev.next;
		while (next != null && !next.value.equals(objectToRemove)) {
			TimeUnit.MILLISECONDS.sleep(Main.dajOpoznieniePorownania());
			prev = next;
			next = next.next;
		}
		boolean wereThisObjectInList = false;
		if (next != null) {
			prev.next = next.next;
			wereThisObjectInList = true;
		}
		lock.unlock();
		return wereThisObjectInList;
	}

	@Override
	public boolean contains(Object object) throws InterruptedException {
		lock.lock();
		Node actual = head.next;
		while (actual != null && !actual.value.equals(object)) {
			TimeUnit.MILLISECONDS.sleep(Main.dajOpoznieniePorownania());
			actual = actual.next;
		}
		boolean isObjectInList = (actual != null && actual.value.equals(object));
		lock.unlock();
		return isObjectInList;
	}
}
