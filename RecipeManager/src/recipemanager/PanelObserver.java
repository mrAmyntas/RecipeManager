package recipemanager;

import java.util.HashMap;

public interface PanelObserver {
	void update(HashMap<String, RecipeData> recipeMap, PanelMediator mediator);
}
