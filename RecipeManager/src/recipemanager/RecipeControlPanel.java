package recipemanager;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class RecipeControlPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JButton addRecipeButton;
	private JButton removeRecipeButton;
	private JButton updateRecipeButton;

	
	RecipeControlPanel(HashMap<String, RecipeData> recipeMap, PanelMediator mediator) {
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.insets = new Insets(2, 2, 2, 2);

		addRecipeButton = new JButton("Add Recipe");
		addRecipeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addRecipe(recipeMap, mediator);
			}
		});
		this.add(addRecipeButton, gbc);
		gbc.gridy = 1;
		removeRecipeButton = new JButton("Remove a Recipe");
		removeRecipeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				removeRecipe(recipeMap, mediator, null);
			}
		});
		this.add(removeRecipeButton, gbc);
		gbc.gridy = 2;
		updateRecipeButton = new JButton("Update a Recipe");
		updateRecipeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateRecipe(recipeMap, mediator);
			}
		});
		this.add(updateRecipeButton, gbc);
	}

	
	private void addRecipe(HashMap<String, RecipeData> recipeMap, PanelMediator mediator) {
		
		try {
			RecipeData recipeData = new RecipeData();
			recipeData.setTitle(JOptionPane.showInputDialog(this, "Enter Recipe Name:"));
			recipeData.setServings(Integer.parseInt(JOptionPane.showInputDialog(this, "Enter number of servings:")));

	        List<String> ingredients = new ArrayList<>();
	        String ingredient;
	        do {
	            ingredient = JOptionPane.showInputDialog(this, "Enter ingredient (or leave blank to finish):");
	            if (ingredient != null && !ingredient.trim().isEmpty()) {
	                ingredients.add(ingredient);
	            }
	        } while (ingredient != null && !ingredient.trim().isEmpty());
	        for ( String ingr : ingredients) {
	        	recipeData.setIngredients(ingr);
	        }			
	        
	        List<String> instructions = new ArrayList<>();
	        String instruction;
	        do {
	        	instruction = JOptionPane.showInputDialog(this, "Enter instructions step by step (or leave blank to finish):");
	            if (instruction != null && !instruction.trim().isEmpty()) {
	            	instructions.add(instruction);
	            }
	        } while (instruction != null && !instruction.trim().isEmpty());
	        for ( String instr : instructions) {
	        	recipeData.setInstructions(instr);
	        }
	        
	        boolean isVegetarian = JOptionPane.showConfirmDialog(this, "Is the recipe vegetarian?", "Vegetarian", 
	        		JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
	        recipeData.setVegetarian(isVegetarian);
	        JOptionPane.showMessageDialog(this, "Successfully added recipe!");
	        //add to dict
	        recipeMap.put(recipeData.getTitle(), recipeData);
	        //add to txt so it saved on app close
	        try {
	            // Open the file in append mode
	            FileWriter fileWriter = new FileWriter("./src/Recipes.txt", true); // 'true' for append mode
	            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

	            // Append text to the file
	            bufferedWriter.newLine();
	            bufferedWriter.write("Recipe Title: " + recipeData.getTitle() + "\n");
	            bufferedWriter.write(recipeData.getServings() + "\n");
	            if (recipeData.getVegetarian()) {
	            	bufferedWriter.write("Vegetarian\n");
	            }
	            bufferedWriter.write("Ingredients:\n");
		        for ( String ingr : ingredients) {
		        	bufferedWriter.write(ingr + "\n");
		        }
		        bufferedWriter.write("Instructions:\n");
		        for ( String instr : instructions) {
		        	bufferedWriter.write(instr + "\n");
		        }
	            bufferedWriter.close();
	        } 
	        catch (IOException e) {
	            System.err.println("Error appending text to the file: " + e.getMessage());
	        }
	        mediator.eventOccurred(recipeMap, mediator);
		}
        catch (Exception e) {
        	System.out.println("Something went wrong entering recipe data.");
        	JOptionPane.showMessageDialog(this, "Failed to add recipe, make sure to input only what is asked!");
        }
    }

	private void removeRecipe(HashMap<String, RecipeData> recipeMap, PanelMediator mediator, String to_remove) {
		
		if (to_remove == null) {
			to_remove = JOptionPane.showInputDialog(this, "Enter recipe name to remove:");
		}
		RecipeData ret = recipeMap.get(to_remove);
		if (ret == null) {
			JOptionPane.showMessageDialog(this, "Failed to remove recipe, make sure spell name correctly!");
			return;
		}
		//remove from dict
		recipeMap.remove(to_remove);
		//remove from txt
		try {
            File recipesFile = new File("./src/Recipes.txt");
            File tempFile = new File("tempFile.txt");

            BufferedReader reader = new BufferedReader(new FileReader(recipesFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
            	if (currentLine.contains("Recipe Title: " + to_remove)) {
            		while (currentLine != null && currentLine.length() > 0) {
            			currentLine = reader.readLine();
            		}
            	}
            	if (currentLine != null)
            		writer.write(currentLine + "\n");
            }
            writer.close();
            reader.close();

            // Delete the original file
            if (recipesFile.delete()) {
                // Rename the temporary file to the original file name
                if (!tempFile.renameTo(recipesFile)) {
                    System.out.println("Could not rename the file");
                }
            } else {
                System.out.println("Could not delete the original file");
            }
            System.out.println("Recipe removed successfully.");
            JOptionPane.showMessageDialog(this, "Recipe removed/updated!");
            mediator.eventOccurred(recipeMap, mediator);
		}
		catch (IOException e) {
			System.err.println("Error removing text from the file: " + e.getMessage());
		}
	}
	
	private void updateRecipe(HashMap<String, RecipeData> recipeMap, PanelMediator mediator) {

		String recipe_to_update = JOptionPane.showInputDialog(this, "Enter recipe name to update:");
		RecipeData recipeData = recipeMap.get(recipe_to_update);
		if (recipeData == null) {
			JOptionPane.showMessageDialog(this, "Failed to update recipe, make sure spell name correctly!");
			return;
		}
		//make copy
		RecipeData copy = new RecipeData();
		List<String> oldingredients = recipeData.getIngredients();
		for (String ingredient : oldingredients) {
			copy.setIngredients(ingredient);
		}
		copy.setInstructions(recipeData.getInstructions());
		copy.setVegetarian(recipeData.getVegetarian());
		copy.setTitle(recipeData.getTitle());
		copy.setServings(recipeData.getServings());
		
		try {
			boolean update = JOptionPane.showConfirmDialog(this, "Do you want to change the name of the recipe?", "updateTitle", 
	        		JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
			if (update) {
				String new_name = JOptionPane.showInputDialog(this, "Enter recipe name to update:");
				if (new_name.length() == 0) {
					JOptionPane.showMessageDialog(this, "Can't have an empty name!");
					return;
				}
				copy.setTitle(new_name);
			}
			update = JOptionPane.showConfirmDialog(this, "Do you want to change the ingredients list?", "updateIngredients", 
	        		JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
			if (update) {
				copy.clearIngredients();
		        List<String> ingredients = new ArrayList<>();
		        String ingredient;
		        do {
		            ingredient = JOptionPane.showInputDialog(this, "Enter ingredient (or leave blank to finish):");
		            if (ingredient != null && !ingredient.trim().isEmpty()) {
		                ingredients.add(ingredient);
		            }
		        } while (ingredient != null && !ingredient.trim().isEmpty());
		        for ( String ingr : ingredients) {
		        	copy.setIngredients(ingr);
		        }			
			}
			update = JOptionPane.showConfirmDialog(this, "Do you want to change the instructions?", "updateInstructions", 
	        		JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
			if (update) {
				copy.clearInstructions();
		        List<String> instructions = new ArrayList<>();
		        String instruction;
		        do {
		        	instruction = JOptionPane.showInputDialog(this, "Enter instructions step by step (or leave blank to finish):");
		            if (instruction != null && !instruction.trim().isEmpty()) {
		            	instructions.add(instruction);
		            }
		        } while (instruction != null && !instruction.trim().isEmpty());
		        for ( String instr : instructions) {
		        	copy.setInstructions(instr);
		        }
			}
			update = JOptionPane.showConfirmDialog(this, "Do you want to change the number of servings?", "updateServings", 
	        		JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
			if (update) {
				copy.setServings(Integer.parseInt(JOptionPane.showInputDialog(this, "Enter number of servings:")));
			}
			update = JOptionPane.showConfirmDialog(this, "Is the recipe Vegetarian?", "updateVeg", 
	        		JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
			copy.setVegetarian(update);
			
			//remove old
			this.removeRecipe(recipeMap, mediator, recipeData.getTitle());
			//add the copy
			recipeMap.put(copy.getTitle(), copy);
			this.addRecipePreset(recipeMap, mediator, copy);
			mediator.eventOccurred(recipeMap, mediator);
		}
        catch (Exception e) {
        	System.out.println("Something went wrong entering recipe data..");
        	System.err.println("Error: " + e.getMessage());
        	JOptionPane.showMessageDialog(this, "Failed to add recipe, make sure to input only what is asked!");
        }
	}
	
	
	private void addRecipePreset(HashMap<String, RecipeData> recipeMap, PanelMediator mediator, RecipeData recipeData) {
		
		try {
            // Open the file in append mode
            FileWriter fileWriter = new FileWriter("./src/Recipes.txt", true); // 'true' for append mode
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // Append text to the file
            bufferedWriter.newLine();
            bufferedWriter.write("Recipe Title: " + recipeData.getTitle() + "\n");
            bufferedWriter.write(recipeData.getServings() + "\n");
            if (recipeData.getVegetarian()) {
            	bufferedWriter.write("Vegetarian\n");
            }

	        List<String> ingredients = recipeData.getIngredients();
	        String[] array = recipeData.getInstructions().split("\n");
	        List<String> instructions = new ArrayList<>();
	        for ( String instr : array) {
	        	instructions.add(instr);
	        }
            bufferedWriter.write("Ingredients:\n");
	        for ( String ingr : ingredients) {
	        	bufferedWriter.write(ingr + "\n");
	        }
	        bufferedWriter.write("Instructions:\n");
	        for ( String instr : instructions) {
	        	bufferedWriter.write(instr);
	        }
	        bufferedWriter.write("\n");
            bufferedWriter.close();
        } 
        catch (IOException e) {
            System.err.println("Error appending text to the file: " + e.getMessage());
        }
        mediator.eventOccurred(recipeMap, mediator);
	}
   
}
