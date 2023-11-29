package recipemanager;

import java.io.IOException;
import java.util.HashMap;

enum filterType {
	  VEGETARIAN,
	  N_SERVINGS,
	  INGR_INCLUDED,
	  INGR_EXCLUDED,
	  INSTR_INCLUDED
	}

public class RecipeManager {
	public static void main(String[] args) {
		RecipeScanner recipeScanner = new RecipeScanner();
		HashMap<String, RecipeData> recipeMap = new HashMap<String, RecipeData>();
		recipeScanner.readfile(recipeMap);
		try {
			recipeScanner.cleanup();
		} catch (IOException e) {
			e.printStackTrace();
		}
		new GUI(recipeMap);
	}
}
