package recipes;

import static ingredients.WetIngredient.*;
import static ingredients.DryIngredient.*;

public class BrownBread extends Recipe{
	
	public BrownBread() {
		addDryIngredient(CORN_STARCH, 11000);
		addDryIngredient(POTATO_STARCH, 10000);
		addDryIngredient(TAPIOCA_STARCH, 7000);
		addDryIngredient(RICE_FLOUR_WHOLEGRAIN, 5000);
		addDryIngredient(XANTHAM_GUM_CARGIL, 800);
		addDryIngredient(BUCKWHEAT, 700);
		addDryIngredient(SALT, 700);
		addDryIngredient(LINSEED_DARK, 1400);
		addDryIngredient(MILLET_SEEDS, 600);
		addDryIngredient(SUNFLOWER_SEEDS, 600);

		
		
		addWetIngredient(WATER, 45000);
		addWetIngredient(TREACLE, 1000);
		addWetIngredient(YEAST, 2000);
		addWetIngredient(GLYCERINE, 2000);
	}

}
