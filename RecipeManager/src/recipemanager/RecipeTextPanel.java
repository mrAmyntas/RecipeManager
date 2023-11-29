package recipemanager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.HashMap;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

/* Creates a panel on which a recipes information is 
 * displayed. One recipe per time, updated by clicking
 * on a recipe in the RecipeListPanel.
 */
public class RecipeTextPanel extends JPanel implements PanelObserver {

	private static final long 	serialVersionUID = 1L;
	JTextArea 					textArea;
	JScrollPane 				scrollPane;

	RecipeTextPanel (HashMap<String, RecipeData> recipeMap) {
		this.setBackground(Color.white);
		this.setLayout(new BorderLayout());		
		textArea = new JTextArea();
		textArea.setPreferredSize(new Dimension(350,2000));
		textArea.setEditable(false);
		this.add(textArea);
	}

	@Override
	public void update(HashMap<String, RecipeData> recipeMap, PanelMediator mediator) {
		//clear previous recipe
		this.remove(textArea);
		if (scrollPane != null)
			this.remove(scrollPane);
		//display current recipe
		recipeMap.forEach((key, value) -> {
			if (value.getDisplay()) {	
				textArea = new JTextArea();
				textArea.setLayout(new BorderLayout());
				textArea.setBorder(new EmptyBorder(5, 10, 5 ,5));
				textArea.setFont(new Font("Baghdad", Font.PLAIN, 16));
				textArea.setEditable(false);
				this.add(textArea);
				textArea.append(key + "\n" + "\n");
				textArea.append("Amount of servings: " + value.getServings() + "\n" + "\n");
				textArea.append("Ingredients:\n");
				List<String> ingredients = value.getIngredients();
				for (String ingredient : ingredients) {
					textArea.append("- " + ingredient + "\n");
				}
				textArea.append("\nInstructions:" + value.getInstructions());
				textArea.setCaretPosition(0);
			}
		});
	} 
}
