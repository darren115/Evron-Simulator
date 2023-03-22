package Workers;

import static ingredients.WetIngredient.WATER;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import equipment.Bowl;
import equipment.PoweredMixer;
import ingredients.WetIngredient;
import recipes.Recipe;

//******************************************************************//
//Wet Ingredient Premixer                                           //
//Responsible for following the recipe and adding the correct       //
//ingredients to the mix.
//
//Recipe informs ingredient and amount
//When ingredients have been weighed they are added to containers
//wetMixReady is marked as true
//If no bowls are free the worker waits
//When a bowl is free water is added to the bowl
//The wet ingredients are added to the bowl
//When the bowl is full and has wet mix and water the bowl is 
//moved to the mixer and the mixing machine is started
//Paperwork is done 
//******************************************************************//

public class WetMix implements Runnable {

	private List<PoweredMixer> mixers = new ArrayList<>();

	private Recipe recipe;
	private List<Bowl> bowls = new ArrayList<>();

	private boolean running = true;

	private boolean wetMixReady = false;
	private Map<WetIngredient, Boolean> ingredientsWeighed = new HashMap<>();

	public WetMix(Recipe recipe, List<Bowl> bowls, List<PoweredMixer> mixers) {
		this.recipe = recipe;
		this.bowls = bowls;
		this.mixers = mixers;

		for (WetIngredient ing : recipe.getWetIngredients().keySet()) {
			ingredientsWeighed.put(ing, false);
		}
	}

	@Override
	public void run() {
		while (running) {

			// broke weigh water
			if (!wetMixReady) {

				weighMix();
			} else {
				tipMix();
			}

			for (Bowl bowl : bowls) {
				if (bowl.isEmpty() == false && bowl.isHasWater() == false) {
					// fork / join a separate thread?
					runWater(bowl);
					break;
				} else if (bowl.isEmpty() == false && bowl.isHasWater() == true) {
					startMixer(bowl);
					break;
				}
			}
		}

	}

	private void weighMix() {

		for (Entry<WetIngredient, Integer> ingredient : recipe.getWetIngredients().entrySet()) {
			if (ingredient.getKey() == WATER) {
				// skip?
			} else {
				try {
					// System.out.println("Weighing up wet ingredient " + ingredient.getKey());
					Thread.sleep(ingredient.getValue() / 3);

				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				// System.out.println("Finished weighing up " + ingredient.getKey());
				wetMixReady = true;
			}
		}

	}

	private void tipMix() {
		if (wetMixReady) {
			for (Bowl bowl : bowls) {
				if (!bowl.isEmpty() && !bowl.isHasWetMix()) {

					System.out.println("Tipped wet mix");
					bowl.setHasWetMix(true);
					wetMixReady = false;
					break;
				}
			}
		}
	}

	private void runWater(Bowl bowl) {
		System.out.println("Started Water");
		bowl.setHasWater(true);
	}

	private void startMixer(Bowl bowl) {
		for(PoweredMixer mixer: mixers) {
			if(!mixer.isMixing()) {
				mixer.start(bowl, 1000);
				System.out.println("Started Mixer");
				break;
			}
		}
		
	}

}
