package pl.com.morgoth.studia.semV.TW.lab3;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GruboZiarnistaLista implements List {

	private final Node head = new Node(null, null);
	private final Lock lock = new ReentrantLock();

	@Override
	public boolean add(Object newObject) {
		lock.lock();
		Node next = head;
		Node prev = next;
		while (next != null) {
			prev = next;
		}
		prev.next = new Node(newObject, null);
		lock.unlock();
		return true;
	}

	@Override
	public boolean remove(Object objectToRemove) {
		lock.lock();
		Node next = head;
		Node prev = next;
		while (next != null && !next.value.equals(objectToRemove)) {
			prev = next;
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
	public boolean contains(Object object) {
		lock.lock();
		Node actual = head;
		while (actual != null && !actual.value.equals(object)) {
			actual = actual.next;
		}
		boolean isObjectInList = (actual != null && actual.value.equals(object));
		lock.unlock();
		return isObjectInList;
	}
}
