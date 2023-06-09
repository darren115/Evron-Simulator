package ingredients;

public class IngredientBin {
	
	private DryIngredient type;
	private int maxVolume;
	private int currentVolume;
	private boolean used = false;
	
	public IngredientBin(DryIngredient type, int max, int current) {
		this.type = type;
		this.maxVolume = max;
		this.currentVolume = current;
	}
	
	public IngredientBin(DryIngredient type, int max) {
		this(type, max, max);
	}

	public int getMaxVolume() {
		return maxVolume;
	}

	public void setMaxVolume(int maxVolume) {
		this.maxVolume = maxVolume;
	}

	public int getCurrentVolume() {
		return currentVolume;
	}

	public void setCurrentVolume(int currentVolume) {
		this.currentVolume += currentVolume;
		if(currentVolume > maxVolume) currentVolume = maxVolume;
	}

	public DryIngredient getType() {
		return type;
	}
	
	public synchronized boolean beingUsed() {
		return used;
	}
	
	public synchronized void useBin() {
		used = true;
	}
	
	public synchronized void releaseBin() {
		used = false;
	}
	
	
}
