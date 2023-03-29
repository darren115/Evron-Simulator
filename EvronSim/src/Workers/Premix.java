package Workers;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

import equipment.Bowl;
import ingredients.DryIngredient;
import ingredients.IngredientBin;
import recipes.Drawable;
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



public class Premix implements Runnable, Drawable {

	private int speed;
	private Recipe recipe;
	private Map<DryIngredient, IngredientBin> bins;
	private boolean containersEmpty = true;
	private int containersVolume = 0;
	List<Bowl> bowls = new ArrayList<>();
	private int mixNumber = 201;

	private ConcurrentMap<DryIngredient, Integer> addedIngredients = new ConcurrentHashMap<>();

	private int numMixes = 10;
	
	//should be 7 minutes per mix avg // 420 seconds
	private int scoopSize = 1000; //1000g per scoop avg //
	private int scoopTime = 300; //should be 3 seconds with random element
	
	private long timeForLastMix = 0;
	private long totalTime = 0;
	
	ConcurrentLinkedQueue <String> previousStatus;
	
	//Rendering info
	private String status = "";
	private int x = 30;
	private int y = 30;

	public Premix(Recipe recipe, Map<DryIngredient, IngredientBin> bins, List<Bowl> bowls, int nummixes) {
		this.recipe = recipe;
		this.bins = bins;
		this.bowls = bowls;	
		this.numMixes = nummixes;
		previousStatus = new ConcurrentLinkedQueue<>();
		
		updateStatus( "Started working");

	}

	private void weighMix() {

		addedIngredients.putAll(recipe.getDryIngredients());

		System.out.println("Dry mix start " + mixNumber);
		updateStatus("Dry mix start " + mixNumber);
		long startTime = System.currentTimeMillis();

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
					updateStatus("Weighed up ingredient " + ingredient.getKey() + " " + temp.getCurrentVolume());
					try {
						//Take time to weigh up ingredient number of scoops * time per scoop
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
		
		System.out.println("Dry mix finish " + mixNumber);
		updateStatus("Dry mix finish " + mixNumber);
		mixNumber++;
		long finishTime = System.currentTimeMillis();
		timeForLastMix = (finishTime - startTime) ;
		totalTime += timeForLastMix;
		System.out.println("Average time " + (totalTime/1000) /(mixNumber-200));

		// while containers arent empty keep trying to tip them;
		// producer consumer on the bowls?
		while (!containersEmpty) {
			for (Bowl bowl : bowls) {
				
				if (!bowl.isHasDryMix()) {
					tipContainers(bowl);
					break;
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}

	private synchronized void tipContainers(Bowl bowl) {
		System.out.println("containers tipped into bowl " + bowl.getId());
		updateStatus("containers tipped into bowl " + bowl.getId());
		bowl.setHasDryMix();
		containersEmpty = true;
	}

	@Override
	public void run() {
		while (mixNumber <= numMixes+200) {
			weighMix();
		}

	}
	
	private void updateStatus(String status) {
		
		if(previousStatus.size()> 5) previousStatus.poll();
		previousStatus.offer(this.status);
		this.status = status;
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.black);
		g.setFont(new Font("arial", Font.BOLD, 16));
		
		int index = 0;
		for(String oldStatus: previousStatus) {
			g.drawString(oldStatus, 60, 125-((previousStatus.size() - index)*20));
			index++;
		}
		
		g.setColor(Color.black);
		g.setFont(new Font("times new roman", Font.BOLD, 16));
		g.drawString(status, 60, 130);
		
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