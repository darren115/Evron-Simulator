package Workers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import equipment.Bowl;
import ingredients.DryIngredient;
import ingredients.IngredientBin;
import recipes.Recipe;


//******************************************************************//
//Dry Ingredient Premixer                                           //
//Responsible for following the recipe and adding the correct       //
//ingredients to the mix.
//
//Recipe informs ingredient and amount
//Bins contain ingredients and have a limited volume
//When ingredients have been weighed they are added to containers
//When containers are full and there is an empty bowl the containers
//are tipped into the bowl.
//Paperwork is done and if a bowl is not available the worker waits
//******************************************************************//



public class Premix implements Runnable {

	private int speed;
	private Recipe recipe;
	private Map<DryIngredient, IngredientBin> bins;
	private boolean containersEmpty = true;
	private int containersVolume = 0;
	List<Bowl> bowls = new ArrayList<>();
	private int mixNumber = 0;

	private ConcurrentMap<DryIngredient, Integer> addedIngredients = new ConcurrentHashMap<>();

	private int numMixes = 10;
	
	private int scoopSize = 1000;
	private int scoopTime = 300; //should be 3 seconds with random element

	public Premix(Recipe recipe, Map<DryIngredient, IngredientBin> bins, List<Bowl> bowls) {
		this.recipe = recipe;
		this.bins = bins;
		this.bowls = bowls;	

	}

	private void weighMix() {

		addedIngredients.putAll(recipe.getDryIngredients());

		System.out.println("Dry mix start");

		// while mix isnt complete keep looping
		// check each bin to see if there is enough ingredient in it
		// if there is weigh it up

		while (addedIngredients.size() > 0) {
			for (Entry<DryIngredient, Integer> ingredient : addedIngredients.entrySet()) {

				IngredientBin temp = bins.get(ingredient.getKey());
				temp.useBin();
				int amount = ingredient.getValue();
				if (temp.getCurrentVolume() >= amount) {
					temp.setCurrentVolume(-amount);
					containersVolume += amount;
					addedIngredients.remove(ingredient.getKey());
					try {
						//Take time to weigh up ingredient
						Thread.sleep((amount/scoopSize) * scoopTime);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				temp.releaseBin();

			}
			
			if(addedIngredients.size()== 0) {
				containersEmpty = false;
			}
		}
		
		mixNumber++;
		System.out.println("Dry mix finish " +mixNumber);

		// while containers arent empty keep trying to tip them;
		// producer consumer on the bowls?
		while (!containersEmpty) {
			for (Bowl bowl : bowls) {
				if (bowl.isEmpty()) {
					tipContainers(bowl);
					break;
				}
			}
		}

	}

	private void tipContainers(Bowl bowl) {
		bowl.setEmpty(false);
		containersEmpty = true;
		System.out.println("containers tipped");
	}

	@Override
	public void run() {
		int i = 0;
		while (i < numMixes) {
			weighMix();
			i++;
		}

	}

}

//		for (Entry<DryIngredient, IngredientBin> entry : bins.entrySet()) {
//			System.out.println(entry.getKey() + " " + entry.getValue().getCurrentVolume());
//		}
//		for(Entry<DryIngredient, IngredientBin> entry: bins.entrySet()) {
//			System.out.println(entry.getKey()  + " " +entry.getValue().getCurrentVolume());
//		}
//		
//		System.out.println(containersVolume);

//start with 2 empty containers 
//tip containers into empty bowl

// for each ingredient open bin -- synchronised
//weigh up ingredient taking x amount of time

//when all ingredients weighed up wait on empty bowl-- synchronised wait
//wait on bowl 2 -- synchronised mutex?
//tip mix 
//finished