package recipemanager;

import java.util.ArrayList;
import java.util.List;

public class RecipeData {
	private List<String>	ingredients = new ArrayList<String>();
	private String			instructions = "";
	private boolean[] 		filtered = {false, false, false, false, false};
	private boolean			vegetarian = false;
	private String 			title = "";
	private int				n_servings = 0;
	private boolean			display = false;
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public int getServings() {
		return n_servings;
	}

	public void setServings(int n) {
		this.n_servings = n;
	}

	public List<String> getIngredients() {
		return ingredients;
	}

	public void setIngredients(String ingredient) {
		this.ingredients.add(ingredient);
	}

	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String newInstruction) {
		instructions = instructions + "\n" + newInstruction;
	}
	
	public boolean[] getFiltered() {
		return filtered;
	}

	public void setFiltered(boolean bool, filterType filterType) {
		if (filterType == recipemanager.filterType.VEGETARIAN) {
			this.filtered[0] = bool;
		}
		else if (filterType == recipemanager.filterType.N_SERVINGS) {
			this.filtered[1] = bool;
		}
		else if (filterType == recipemanager.filterType.INGR_INCLUDED) {
			this.filtered[2] = bool;
		}
		else if (filterType == recipemanager.filterType.INGR_EXCLUDED) {
			this.filtered[3] = bool;
		}
		else if (filterType == recipemanager.filterType.INSTR_INCLUDED) {
			this.filtered[4] = bool;
		}
	}
	public boolean getVegetarian() {
		return vegetarian;
	}
	
	public void setVegetarian(boolean bool) {
		this.vegetarian = bool;
	}
	
	public boolean getDisplay() {
		return display;
	}
	
	public void setDisplay(boolean bool) {
		this.display = bool;
	}
	
	public void clearIngredients() {
		this.ingredients.clear();
	}
	public void clearInstructions() {
		this.instructions = "";
	}

}
