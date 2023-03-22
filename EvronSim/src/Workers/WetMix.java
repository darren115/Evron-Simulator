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

public class WetMix implements Runnable{
	
	private Thread mixer;
	
	private Recipe recipe;
	private List<Bowl> bowls = new ArrayList<>();
	
	private boolean running = true;
	
	private boolean wetMixReady = false;
	private Map<WetIngredient, Boolean> ingredientsWeighed = new HashMap<>();
	
	public WetMix(Recipe recipe, List<Bowl> bowls) {
		this.recipe = recipe;
		this.bowls = bowls;
		
		for(WetIngredient ing: recipe.getWetIngredients().keySet()) {
			ingredientsWeighed.put(ing, false);
		}
	}
	
	@Override
	public void run() {
		while(running) {
			
			//broke weigh water
			if(!wetMixReady) {
				
				weighMix();
			} else {
				tipMix();
			}
			
			for(Bowl bowl: bowls) {
				if(bowl.isEmpty() == false && bowl.isHasWater() == false) {
					//fork / join a separate thread?
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
		
		for(Entry<WetIngredient, Integer> ingredient : recipe.getWetIngredients().entrySet()) {
			if(ingredient.getKey() == WATER) {
				//skip?
			} else {
				try {
					System.out.println("Weighing up wet ingredient " + ingredient.getKey());
					Thread.sleep(ingredient.getValue()/30);
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				System.out.println("Finished weighing up " + ingredient.getKey());
				wetMixReady = true;
			}
		}
			
		
		//weigh yeast
		//weigh oil
		//weigh glycerine
		//when bowl is ready add water
		// add wet ingredients to bowl
		
		//start mix when mixer is free // finish previous mix
		
		
	}
	
	private void tipMix() {
		if(wetMixReady) {
			for(Bowl bowl: bowls) {
				if(!bowl.isEmpty() && !bowl.isHasWetMix()) {
					
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
		//System.out.println("Started Mixer");
		//mixer = new Thread(new PoweredMixer(1000, bowl));
		//mixer.start();
	}



}
