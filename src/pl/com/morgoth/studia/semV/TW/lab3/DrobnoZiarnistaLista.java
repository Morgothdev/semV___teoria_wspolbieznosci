package pl.com.morgoth.studia.semV.TW.lab3;

public class DrobnoZiarnistaLista implements List {

	private final Node head = new Node(null, null);

	@Override
	public boolean add(Object newObject) {
		Node next = head;
		Node prev = next;
		prev.lock.lock();
		while (next != null) {
			next.lock.lock();
			prev.lock.unlock();
			prev = next;
		}
		prev.next = new Node(newObject, null);
		prev.lock.unlock();
		return true;
	}

	@Override
	public boolean remove(Object objectToRemove) {
		Node next = head;
		Node prev = next;
		prev.lock.lock();
		while (next != null && !next.value.equals(objectToRemove)) {
			next.lock.lock();
			prev.lock.unlock();
			prev = next;
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
	public boolean contains(Object object) {
		Node next = head;
		Node prev = next;
		prev.lock.lock();
		while (next != null && !next.value.equals(object)) {
			next.lock.lock();
			prev.lock.unlock();
			prev = next;
		}
		boolean isObjectInList = (next != null && next.value.equals(object));
		prev.lock.unlock();
		return isObjectInList;
	}
}
