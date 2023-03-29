package Sim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import Workers.Premix;
import Workers.Tipper;
import Workers.WetMix;
import equipment.Bowl;
import equipment.PoweredMixer;
import equipment.Vemag;
import ingredients.DryIngredient;
import ingredients.IngredientBin;
import recipes.Bread;

public class Main {
	
	public static final int NUM_MIXES = 10;
	private static Visual image;

	public static void main(String args[]) {
		
		Vemag vemag = new Vemag(60, 500);
		Thread vmg = new Thread(vemag);
		vmg.start();
		
		PoweredMixer mixer1 = new PoweredMixer(1);
		PoweredMixer mixer2 = new PoweredMixer(2);
		List<PoweredMixer> mixers = new ArrayList<>();
		mixers.add(mixer1);
		mixers.add(mixer2);
		
		Bowl bowl1 = new Bowl(1);
		Bowl bowl2 = new Bowl(2);
		List<Bowl> bowls = new ArrayList<>();
		bowls.add(bowl1);
		bowls.add(bowl2);
		Bread bread = new Bread();
		
		//Instantiate Map of Bins containing Ingredients
		Map<DryIngredient, IngredientBin> bins = new HashMap<>();
	
		
		for(Entry<DryIngredient, Integer> ingredient : bread.getDryIngredients().entrySet()) {
			bins.put(ingredient.getKey(), new IngredientBin(ingredient.getKey(), 50000) );
		}
		Premix mix1 = new Premix(bread, bins, bowls, NUM_MIXES);
		WetMix mix2 = new WetMix(bread, bowls, mixers, NUM_MIXES);
		Tipper mix = new Tipper(bins, bowls, mixers, vemag, NUM_MIXES);
		
		Thread thread= new Thread(mix1);
		thread.start();
		Thread thread2 = new Thread(mix2);
		thread2.start();
		Thread thread3 = new Thread(mix);
		thread3.start();
		
		image = new Visual(mix1, mix2, mix, bowl1, bowl2, vemag);
		Thread thread4 = new Thread(image);
		thread4.start();
		
		//ThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(5);
		//executor.execute(THREAD);
		
		
		
	}
}
