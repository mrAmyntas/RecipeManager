package recipemanager;

import java.util.HashMap; 

public class GUI  {
	
	public GUI(HashMap<String, RecipeData> recipeMap) {
		new MyFrame(recipeMap);
	}
}
