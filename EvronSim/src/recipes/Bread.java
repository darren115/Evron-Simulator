package recipes;

import static ingredients.WetIngredient.*;
import static ingredients.DryIngredient.*;

public class Bread extends Recipe{
	
	public Bread() {
		addDryIngredient(CORN_STARCH, 11000);
		addDryIngredient(POTATO_STARCH, 10000);
		addDryIngredient(TAPIOCA_STARCH, 7000);
		addDryIngredient(SUGAR, 1000);
		addDryIngredient(RICE_FLOUR_WHITE, 5000);
		addDryIngredient(XANTHAM_GUM, 800);
		addDryIngredient(E471, 700);
		addDryIngredient(PSYLLIUM_HUSK, 1400);
		addDryIngredient(SALT, 600);
		addDryIngredient(WHITE_PREMIX, 4000);

		
		
		addWetIngredient(WATER, 45000);
		addWetIngredient(GLYCERINE, 1000);
		addWetIngredient(YEAST, 2000);
		addWetIngredient(RAPESEED_OIL, 2000);
	}

}
