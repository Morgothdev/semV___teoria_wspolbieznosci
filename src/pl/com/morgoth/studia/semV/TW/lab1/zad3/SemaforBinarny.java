package pl.com.morgoth.studia.semV.TW.lab1.zad3;

public class SemaforBinarny {

	private boolean zablokowany = false;

	public void podnies() {
		synchronized (this) {
			try {
				while (zablokowany) {
					this.wait();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			zablokowany = true;
		}

	}

	public void opusc() {
		synchronized (this) {
			zablokowany = false;
			this.notifyAll();
		}
	}

	public boolean czyZablokowany() {
		synchronized (this) {
			return zablokowany;
		}
	}
}
