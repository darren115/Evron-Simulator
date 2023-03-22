package equipment;

public class PoweredMixer implements Runnable {

	private boolean isMixing = false;
	private Bowl bowl;
	private int setTime;
	private int currentTime;
	private boolean running;

	public PoweredMixer(int setTime, Bowl bowl) {
		this.setTime = setTime;
		this.bowl = bowl;
	}

	// producer consumer with the bowl?
	@Override
	public void run() {

		
			if (!bowl.isHasWater() || bowl.isEmpty()) {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				isMixing = true;

				System.out.println("Start mixing");

				try {
					Thread.sleep(setTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				isMixing = false;
				System.out.println("Finished mixing");

			
		}
	}
	
	public boolean isMixing() {
		return isMixing;
	}

}
