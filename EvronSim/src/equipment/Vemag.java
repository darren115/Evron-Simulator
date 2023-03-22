package equipment;

public class Vemag implements Runnable{
	
	//private int clip;
	private int speed;
	private int currentVolume;
	private int unitSize;
	private boolean switchedOn = false;
	
	public Vemag(int speed, int unitSize) {
		this.speed = speed;
		this.unitSize = unitSize;
		switchedOn = true;
	}

	@Override
	public synchronized void run() {
		while(switchedOn) {
			if(currentVolume < unitSize) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			currentVolume -= unitSize;
			
			
			try {
				Thread.sleep((60/speed) * unitSize);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public void tipMix(int size) {
		this.currentVolume += size;
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
