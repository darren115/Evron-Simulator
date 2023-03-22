package Workers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import equipment.Bowl;
import ingredients.DryIngredient;
import ingredients.IngredientBin;

public class Tipper implements Runnable {

	private Map<DryIngredient, IngredientBin> bins;
	List<Bowl> bowls = new ArrayList<>();
	private boolean finished = false;

	public Tipper(Map<DryIngredient, IngredientBin> bins, List<Bowl> bowls) {
		this.bins = bins;
		this.bowls = bowls;
		// fill bins as long as they arent being used
		// when mix is ready tip mix

	}

	@Override
	public void run() {

		while (!finished) {
			
			checkBins();
			
			for (Bowl bowl : bowls) {
				if(bowl.isHasWater())
					tipMix(bowl);
				
			}
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	private void checkBins() {
		for (IngredientBin bin : bins.values()) {
			if (bin.beingUsed() == false && bin.getCurrentVolume() < bin.getMaxVolume() / 2) {
				bin.useBin();
				
				fillBin(bin);
				
				bin.releaseBin();
				
			}
		}
	}

	private void fillBin(IngredientBin bin) {
		System.out.println(bin.getType() + " " + bin.getCurrentVolume());
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		bin.setCurrentVolume(bin.getMaxVolume());
		System.out.println("Bin filled " + bin.getType());
	}

	private void tipMix(Bowl bowl) {
		bowl.setEmpty(true);
	}

}
