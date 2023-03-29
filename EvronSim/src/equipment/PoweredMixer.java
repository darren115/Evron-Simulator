package equipment;

public class PoweredMixer implements Runnable {

	private boolean isMixing = false;
	private Bowl bowl;
	private int setTime;
	private int currentTime;
	private boolean running;
	private int id;
	private boolean finishedMixing = false;
	
	private Thread mixer;

	public PoweredMixer(int id) {
		this.id = id;
	}
	
	public synchronized void start(Bowl bowl, int time) {
		this.bowl = bowl;
		this.setTime = time;
		finishedMixing = false;
		isMixing = true;
		mixer = new Thread(this);
		mixer.start();
	}

	// producer consumer with the bowl?
	@Override
	public void run() {

		
		

		System.out.println("Start mixing " + id + " bowl " + bowl.getId());

		try {
			Thread.sleep(setTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		isMixing = false;
		finishedMixing = true;
		bowl.setFinishedMixing();
		System.out.println("Finished mixing " + id);

	}

	public boolean isMixing() {
		return isMixing;
	}

	public int getId() {
		return id;
	}

	public boolean isFinishedMixing() {
		System.out.println("called");
		return finishedMixing;
	}
	
	

}
