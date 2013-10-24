package pl.com.morgoth.studia.semV.TW.lab3;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Node {
	private Node next;
	private Lock lock = new ReentrantLock();
	private Object value;

	public Node(Object value, Node next) {
		this.value = value;
		this.next = next;
	}

	public Node getNext() {
		return next;
	}

	public Node setNext(Node next) {
		this.next = next;
		return this;
	}

	public Lock getLock() {
		return lock;
	}

	public Node setLock(Lock lock) {
		this.lock = lock;
		return this;
	}

	public Object getValue() {
		return value;
	}

	public Node setValue(Object value) {
		this.value = value;
		return this;
	}
}
