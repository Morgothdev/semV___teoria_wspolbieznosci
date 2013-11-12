package pl.com.morgoth.studia.semV.TW.lab5;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;


public class ServantImpl implements Servant {

	@Override
	public Long m1() {
		try {
			TimeUnit.SECONDS.sleep(new Random(543).nextInt(5));
		} catch (InterruptedException e) {
			LogManager.getLogger(ServantImpl.class).log(Level.DEBUG, "interrupted");
		}
		LogManager.getLogger(ServantImpl.class).log(Level.DEBUG, "servant.m1 called");
		return new Long(new Random(54).nextLong());
	}

	@Override
	public Object m2() {
		try {
			LogManager.getLogger(ServantImpl.class).log(Level.DEBUG, "sleep troche");
			TimeUnit.SECONDS.sleep(new Random(543).nextInt(5));
		} catch (InterruptedException e) {
			LogManager.getLogger(ServantImpl.class).log(Level.DEBUG, "interrupted");
		}
		LogManager.getLogger(ServantImpl.class).log(Level.DEBUG, "servant.m2 called");
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
		LogManager.getLogger(ServantImpl.class).log(Level.DEBUG, "servant.m3 called");
	}
}
