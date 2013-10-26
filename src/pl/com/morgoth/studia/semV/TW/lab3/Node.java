package pl.com.morgoth.studia.semV.TW.lab3;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Node {
	public Node next;
	public final Lock lock = new ReentrantLock();
	public Object value;

	public Node(Object value, Node next) {
		this.value = value;
		this.next = next;
	}

}
