package pl.com.morgoth.studia.semV.TW.lab5.implementations;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import pl.com.morgoth.studia.semV.TW.lab5.Servant;

public class ServantImpl implements Servant {

	@Override
	public Long m1() {
		return new Long(new Random(546).nextLong());
	}

	@Override
	public Object m2() {
		return new Object();
	}

	@Override
	public void m3() {
		try {
			LogManager.getLogger(ServantImpl.class).log(Level.DEBUG, "sleep troche");
			TimeUnit.SECONDS.sleep(new Random(543).nextInt(5));
		} catch (InterruptedException e) {
			LogManager.getLogger(ServantImpl.class).log(Level.DEBUG, "interrupted");
		}
	}
}
