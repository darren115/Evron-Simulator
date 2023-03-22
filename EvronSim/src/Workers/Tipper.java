package Workers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import equipment.Bowl;
import equipment.PoweredMixer;
import ingredients.DryIngredient;
import ingredients.IngredientBin;

//******************************************************************//
//Third Mixer man                                                   //
//Mixer man keeps ingredient bins filled and tips mixes into vemag  //
//
//Checks bins current volume and fills them if they are too low
//When the mixing machine finishes takes the bowl to the vemag and
//tips the mix into the vemag resulting in an empty bowl
//******************************************************************//


public class Tipper implements Runnable {

	private List<PoweredMixer> mixers = new ArrayList<>();
	
	private Map<DryIngredient, IngredientBin> bins;
	List<Bowl> bowls = new ArrayList<>();
	private boolean finished = false;

	public Tipper(Map<DryIngredient, IngredientBin> bins, List<Bowl> bowls, List<PoweredMixer> mixers) {
		this.bins = bins;
		this.bowls = bowls;
		this.mixers = mixers;
		// fill bins as long as they arent being used
		// when mix is ready tip mix

	}

	@Override
	public void run() {

		while (!finished) {
			
			checkBins();
			
			for (Bowl bowl : bowls) {
				//if(bowl.isHasWater() && bowl.isHasWetMix() && !bowl.isEmpty())
					//tipMix(bowl);
				
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
		System.out.println("Tipped Mix into Vemag");
		bowl.setEmpty(true);
	}

}
