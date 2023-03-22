package equipment;

public class Bowl {

	private volatile boolean isEmpty = true;
	private boolean hasWater = false;
	private boolean hasWetMix = false;
	private boolean finishedMixing = false;
	private int id = 0;

	public Bowl(int id) {
		this.id = id;
	}

	public synchronized boolean isEmpty() {
		return isEmpty;
	}

	public synchronized void setEmpty(boolean isEmpty) {
		this.isEmpty = isEmpty;
		setHasWater(false);
		setHasWetMix(false);
	}

	public boolean isHasWater() {
		return hasWater;
	}

	public void setHasWater(boolean hasWater) {
		this.hasWater = hasWater;
	}

	public boolean isHasWetMix() {
		return hasWetMix;
	}

	public void setHasWetMix(boolean hasWetMix) {
		this.hasWetMix = hasWetMix;
	}

	public int getId() {
		return id;
	}

}
