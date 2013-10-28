package pl.com.morgoth.studia.semV.TW.lab3;

import java.util.concurrent.TimeUnit;

public class DrobnoZiarnistaLista implements List {

	private final Node head = new Node(null, null);

	@Override
	public boolean add(Object newObject) throws InterruptedException {
		Node next = head;
		Node prev = next;
		next = prev.next;
		prev.lock.lock();
		while (next != null) {
			TimeUnit.MILLISECONDS.sleep(Main.dajOpoznieniePorownania());
			next.lock.lock();
			prev.lock.unlock();
			prev = next;
			next = next.next;
		}
		prev.next = new Node(newObject, null);
		prev.lock.unlock();
		return true;
	}

	@Override
	public boolean remove(Object objectToRemove) throws InterruptedException {
		Node next = head;
		Node prev = next;
		next = prev.next;
		prev.lock.lock();
		while (next != null && !next.value.equals(objectToRemove)) {
			TimeUnit.MILLISECONDS.sleep(Main.dajOpoznieniePorownania());
			next.lock.lock();
			prev.lock.unlock();
			prev = next;
			next = next.next;
		}
		boolean wereThisObjectInList = false;
		if (next != null) {
			prev.next = next.next;
			wereThisObjectInList = true;
		}
		prev.lock.unlock();
		return wereThisObjectInList;
	}

	@Override
	public boolean contains(Object object) throws InterruptedException {
		Node next = head;
		Node prev = next;
		next = prev.next;
		prev.lock.lock();
		while (next != null && !next.value.equals(object)) {
			TimeUnit.MILLISECONDS.sleep(Main.dajOpoznieniePorownania());
			next.lock.lock();
			prev.lock.unlock();
			prev = next;
			next = next.next;
		}
		boolean isObjectInList = (next != null && next.value.equals(object));
		prev.lock.unlock();
		return isObjectInList;
	}
}