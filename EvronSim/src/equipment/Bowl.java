package equipment;

public class Bowl {

	private volatile boolean isEmpty = true;
	private boolean hasWater = false;
	private boolean hasWetMix = false;
	private boolean hasDryMix = false;
	private volatile boolean finishedMixing = false;
	private volatile boolean isMixing = false;
	private int id = 0;

	public Bowl(int id) {
		this.id = id;
	}


	public synchronized void setEmpty() {
		hasDryMix = false;
		hasWetMix = false;
		hasWater = false;
		isMixing = false;
		finishedMixing = false;
	}

	public synchronized boolean isHasDryMix() {
		return hasDryMix;
	}

	public synchronized void setHasDryMix() {

		hasDryMix = true;
	}

	public synchronized boolean isMixing() {

		return isMixing;
	}

	public void setMixing() {
//		System.out.println("isMixing" +id);
		isMixing = true;
	}

	public boolean isHasWater() {

		return hasWater;
	}

	public void setHasWater() {
		//System.out.println("has water " + id);
		this.hasWater = true;
	}

	public boolean isHasWetMix() {
		return hasWetMix;
	}

	public void setHasWetMix() {
		//System.out.println("has wet mix " + id);
		this.hasWetMix = true;
	}

	public boolean isFinishedMixing() {
		return finishedMixing;
	}

	public void setFinishedMixing() {
		finishedMixing = true;
	}

	public int getId() {
		return id;
	}

}
