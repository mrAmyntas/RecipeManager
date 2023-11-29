package recipemanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PanelMediator {
    private List<PanelObserver> observers = new ArrayList<>();

    public void addObserver(PanelObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(PanelObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(HashMap<String, RecipeData> recipeMap, PanelMediator mediator) {
        for (PanelObserver observer : observers) {
            observer.update(recipeMap, mediator);
        }
    }

    // Method to trigger an event that notifies observers
    public void eventOccurred(HashMap<String, RecipeData> recipeMap, PanelMediator mediator) {
        notifyObservers(recipeMap, mediator);
    }
}
