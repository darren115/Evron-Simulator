package equipment;

public class Vemag implements Runnable{
	
	//private int clip;
	private int speed;
	private int currentVolume=0;
	private int unitSize;
	private boolean switchedOn = true;
	
	public Vemag(int speed, int unitSize) {
		this.speed = speed;
		this.unitSize = unitSize;
		switchedOn = true;
	}

	@Override
	public synchronized void run() {
		while(switchedOn) {
			while(currentVolume < unitSize) {
				try {
					System.out.println("---------------WAITING VEMAG " + currentVolume);
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			System.out.println("Running VEMAG" + currentVolume);
			currentVolume = currentVolume - unitSize;
			
			
			try {
				Thread.sleep((60/speed) * unitSize);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public synchronized void tipMix(int size) {
		this.currentVolume += size;
		System.out.println("------Vemag " + currentVolume);
		notifyAll();
	}

	public int getSpeed() {
		return speed;
	}

	public int getCurrentVolume() {
		return currentVolume;
	}

	public int getUnitSize() {
		return unitSize;
	}

	public boolean isSwitchedOn() {
		return switchedOn;
	}

	
}
