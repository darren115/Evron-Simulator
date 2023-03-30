package Workers;

import static ingredients.WetIngredient.WATER;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentLinkedQueue;

import equipment.Bowl;
import equipment.PoweredMixer;
import ingredients.WetIngredient;
import recipes.Drawable;
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

public class WetMix implements Runnable, Drawable {

	private List<PoweredMixer> mixers = new ArrayList<>();

	private Recipe recipe;
	private List<Bowl> bowls = new ArrayList<>();

	private boolean running = true;

	private boolean wetMixReady = false;
	private Map<WetIngredient, Boolean> ingredientsWeighed = new HashMap<>();

	private int numMixes = 10;
	int numWeighed = 0;
	int numTipped = 0;
	int addedWater = 0;
	int numStarted = 0;

	ConcurrentLinkedQueue<String> previousStatus;
	private String status = "";

	public WetMix(Recipe recipe, List<Bowl> bowls, List<PoweredMixer> mixers, int numMixes) {
		this.recipe = recipe;
		this.bowls = bowls;
		this.mixers = mixers;
		this.numMixes = numMixes;

		previousStatus = new ConcurrentLinkedQueue<>();

		for (WetIngredient ing : recipe.getWetIngredients().keySet()) {
			ingredientsWeighed.put(ing, false);
		}
	}

	@Override
	public void run() {
		while (running) {

			if (!wetMixReady && numWeighed < numMixes) {
				weighMix();
			} else {
				tipMix();
			}

			if (addedWater <= numMixes && numStarted < numMixes) {
				for (Bowl bowl : bowls) {

					if (bowl.isHasDryMix() && bowl.isHasWetMix() && bowl.isHasWater() == false) {
						runWater(bowl);
						break;
					} else if (bowl.isHasDryMix() && bowl.isHasWater() && bowl.isHasWetMix() && !bowl.isMixing()) {
						startMixer(bowl);
						break;
					}
				}
			}
			
			//System.out.println("-------------" + addedWater + numWeighed + numTipped);
			if((addedWater + numWeighed + numTipped + numStarted) == numMixes*4) running = false;
		}
		
		updateStatus("Finished work");

	}

	private void weighMix() {

		for (Entry<WetIngredient, Integer> ingredient : recipe.getWetIngredients().entrySet()) {
			if (ingredient.getKey() == WATER) {
				// skip?
			} else {
				try {
//					System.out.println("Weighing up wet ingredient " + ingredient.getKey());
					updateStatus("Weighing up wet ingredient " + ingredient.getKey() + " " + ingredient.getValue());
					Thread.sleep(ingredient.getValue() * 2);

				} catch (InterruptedException e) {
					e.printStackTrace();
				}

//				System.out.println("Finished weighing up " + ingredient.getKey());
				updateStatus("Finished weighing up " + ingredient.getKey());
				wetMixReady = true;
			}
		}
		numWeighed++;
		updateStatus("Finished wet mix " + (200 + numWeighed));

	}

	private void tipMix() {
		if (wetMixReady) {
			for (Bowl bowl : bowls) {
				if (!bowl.isHasWetMix() && bowl.isHasDryMix()) {

					numTipped++;
//					System.out.println("Tipped wet mix");
					updateStatus("Tipped wet mix");
					bowl.setHasWetMix();
					wetMixReady = false;
					break;
				}
			}
		}
	}

	private void runWater(Bowl bowl) {
//		System.out.println("Started Water");
		updateStatus("Started Water");
		addedWater++;
		bowl.setHasWater();
	}

	// issue with bowl being started with 2 mixers
	// adding bowl to both mixers

	private void startMixer(Bowl bowl) {
		for (PoweredMixer mixer : mixers) {
			if (!mixer.isMixing() && !bowl.isFinishedMixing() && !bowl.isMixing()) {
				bowl.setMixing();
				mixer.start(bowl, 20000);
				numStarted++;
				break;
			}
		}

	}

	private void updateStatus(String status) {

		if (previousStatus.size() > 5)
			previousStatus.poll();
		previousStatus.offer(this.status);
		this.status = status;
	}

	@Override
	public void draw(Graphics g) {

		g.setColor(Color.black);
		g.setFont(new Font("times new roman", Font.BOLD, 16));
		g.drawString("Wet Premixer", 30, 360);

		g.setColor(Color.black);
		g.setFont(new Font("arial", Font.BOLD, 16));

		int index = 0;
		for (String oldStatus : previousStatus) {
			g.drawString(oldStatus, 30, 510 - ((previousStatus.size() - index) * 20));
			index++;
		}

		g.setColor(Color.black);
		g.setFont(new Font("times new roman", Font.BOLD, 16));
		g.drawString(status, 30, 520);

	}

}
