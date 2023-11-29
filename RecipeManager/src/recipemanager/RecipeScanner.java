package recipemanager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

/*recipes are always stored the same way in the recipes textfile.
 *	Recipe Title: <recipe name>
 *	<number of servings> 
 *	Ingredients:
 *	<ingredient> (one per line)
 *	Instructions:
 *	<instruction> (one per line)
 *	newline
 */

public class RecipeScanner {
	
	private String 			line;
	private RecipeData 		recipeData;
	
	File recipesFile = new File("src/Recipes.txt");
	
	public void readfile(HashMap<String, RecipeData> recipeMap) {
		try {
			Scanner myRecipes = new Scanner(recipesFile);
			while (myRecipes.hasNextLine()) {
				line = myRecipes.nextLine();
				if (line.contains("Recipe Title: ")) {
					//parse recipe into hashmap
					//set title
					recipeData = new RecipeData();
					recipeData.setTitle(line.substring(14));
					//set number of servings
					if (myRecipes.hasNextLine())
						line = myRecipes.nextLine();
					recipeData.setServings(Integer.parseInt(line));
					//set if vegetarian or not
					if (myRecipes.hasNextLine())
						line = myRecipes.nextLine();
					if (line.contains("Vegetarian")) {
						recipeData.setVegetarian(true);
						if (myRecipes.hasNextLine())
							line = myRecipes.nextLine();
					}
					while (myRecipes.hasNextLine()) {
						if (line.contains("Ingredients:")) {
							//set ingredients
							line = myRecipes.nextLine();
							while (!line.contains("Instructions:")) {
								recipeData.setIngredients(line);
								if (!myRecipes.hasNextLine())
									break;
								line = myRecipes.nextLine();
							}
							if (myRecipes.hasNextLine()) {
								line = myRecipes.nextLine();
								//set instructions
								while (line.length() != 0) {
									recipeData.setInstructions(line);									
									if (!myRecipes.hasNextLine())
										break;
									line = myRecipes.nextLine();
								}
							}
							recipeMap.put(recipeData.getTitle(), recipeData);
						}
						else
							break;
					}
				}				
			}
			myRecipes.close();
		} 
		catch (FileNotFoundException e) {
			//if file doesnt exist, make empty one
			if (e.getCause() == null) {
				File myFile = new File("src/Recipes.txt");
				try {
					if (myFile.createNewFile()) {
						System.out.println("created new file");
					}
					else {
						System.out.println("file already existed");
					}
				} 
				catch (IOException e1) {
					e1.printStackTrace();
					System.out.println("cause: " + e1.getCause());
				}
			}
			else {
				e.printStackTrace();
			}
		}
	}
	
	/* Made this just to remove extra lines that appear 
	 * in the text file from removing/updating recipes
	 */
	
	public void cleanup() throws IOException {
		
		String 	currentLine = "";
		String 	line2;
		
        File recipesFile = new File("./src/Recipes.txt");
        File tempFile = new File("tempFile.txt");

		BufferedReader reader = new BufferedReader(new FileReader(recipesFile));
		BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
    	currentLine = reader.readLine();
        while (currentLine != null) {
    		line2 = reader.readLine();
    		if (line2 != null && currentLine.length() == 0 && line2.length() == 0) {
    			writer.write(currentLine);
    		}
    		else {
    			if (currentLine != null)
    				writer.write(currentLine + "\n");
    			if (line2 != null)
    				writer.write(line2 + "\n");
    		}
    		currentLine = reader.readLine();
        }
        writer.close();
        reader.close();
        // Delete the original file
        if (recipesFile.delete()) {
            // Rename the temporary file to the original file name
            if (!tempFile.renameTo(recipesFile)) {
                System.out.println("Could not rename the file");
            }
        }
        else {
            System.out.println("Could not delete the original file");
        }
	}
}
