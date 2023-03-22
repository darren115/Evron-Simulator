package recipes;

import java.util.HashMap;
import java.util.Map;

import ingredients.DryIngredient;
import ingredients.WetIngredient;

public abstract class Recipe {

	private Map<WetIngredient, Integer> wetIngredients = new HashMap<>();
	private Map<DryIngredient, Integer> dryIngredients = new HashMap<>();
	
	
	public Map<WetIngredient, Integer> getWetIngredients(){
		return wetIngredients;
	}

	public Map<DryIngredient, Integer> getDryIngredients(){
		return dryIngredients;
	}
	
	protected void addDryIngredient(DryIngredient ingredient, int amount) {
		dryIngredients.put(ingredient, amount);
	}
	
	protected void addWetIngredient(WetIngredient ingredient, int amount) {
		wetIngredients.put(ingredient, amount);
	}
}
