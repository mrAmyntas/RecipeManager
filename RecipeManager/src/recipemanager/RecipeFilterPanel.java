package recipemanager;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

// Creates all the filter options

public class RecipeFilterPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private boolean			servings_filtered;
	private int				spinner_value = 2;		
	RecipeFilterPanel(HashMap<String, RecipeData> recipeMap, PanelMediator mediator) {
		
		this.setBackground(Color.cyan);
		this.setLayout(new FlowLayout());

		//Add text to panel
		JLabel label = new JLabel();
		label.setText("Filter options:");
		this.add(label);
		
		//--------------------------------------------------------------------
		
		//add checkbox for filtering vegetarian recipes
		JCheckBox VegetarianCheckBox = new JCheckBox("Vegetarian");
		VegetarianCheckBox.setFocusable(false);
		ActionListener actionListener = new ActionListener() {
		      public void actionPerformed(ActionEvent actionEvent) {
		        AbstractButton abstractButton = (AbstractButton) actionEvent.getSource();
		        boolean selected = abstractButton.getModel().isSelected();
		        if (selected == true) {
		        	recipeMap.forEach((key, value) -> {
		        		if (value.getVegetarian() == false) {
		        			value.setFiltered(true, filterType.VEGETARIAN);
		        		}
		        	});
		        }
		        else {
		        	recipeMap.forEach((key, value) -> {
		        		value.setFiltered(false, filterType.VEGETARIAN);
		        	});
		        }
		        mediator.eventOccurred(recipeMap, mediator);		      }
		};
		VegetarianCheckBox.addActionListener(actionListener);
		this.add(VegetarianCheckBox);
		
		//--------------------------------------------------------------------
	
		//add spinner and checkbox to filter recipes based on number of servings
		
		//label
		JLabel servingsLabel = new JLabel();
		servingsLabel.setText("Number of servings (1-20):");
		this.add(servingsLabel);
		
		//checkbox
		JCheckBox ServingsCheckBox = new JCheckBox();
		ServingsCheckBox.setFocusable(false);
		ActionListener actionListener2 = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				AbstractButton abstractButton = (AbstractButton) actionEvent.getSource();
		        boolean selected = abstractButton.getModel().isSelected();
		        if (selected == true) {
		        	servings_filtered = true;
		        	recipeMap.forEach((key, value) -> {
		        		if (value.getServings() == spinner_value) {
		        			value.setFiltered(false, filterType.N_SERVINGS);
		        		}
		        		else {
		        			value.setFiltered(true, filterType.N_SERVINGS);
		        		}
		        	});
		        }
		        else {
		        	servings_filtered = false;
		        	recipeMap.forEach((key, value) -> {
		        		value.setFiltered(false, filterType.N_SERVINGS);
		        	});
		        }
		        mediator.eventOccurred(recipeMap, mediator);		      
			}
		};
		ServingsCheckBox.addActionListener(actionListener2);
		this.add(ServingsCheckBox);
		
		//spinner
		SpinnerModel value = new SpinnerNumberModel(2, 1, 20, 1);
		JSpinner spinner = new JSpinner(value);
		spinner.setEditor(new JSpinner.DefaultEditor(spinner));
		spinner.setPreferredSize(new Dimension(36,20));
	    ChangeListener listener = new ChangeListener() {
	        public void stateChanged(ChangeEvent e) {
	        	spinner_value = (Integer)spinner.getValue();
	        	if (servings_filtered) {
	        		recipeMap.forEach((key, value) -> {
	        			if (value.getServings() == spinner_value) {
	        				value.setFiltered(false, filterType.N_SERVINGS);
	        			}
	        			else {
	        				value.setFiltered(true, filterType.N_SERVINGS);
	        			}	
	        		});
	        	}
	        	mediator.eventOccurred(recipeMap, mediator);
	        }
	    };
	    spinner.addChangeListener(listener);  
		this.add(spinner);
		
		//--------------------------------------------------------------------
		
		//add field to filter ingredients that have to be included
		//label
		JLabel includeLabel = new JLabel();
		includeLabel.setText("Ingredients to include (seperate by comma):");
		this.add(includeLabel);
		//textfield
		JTextField includeIngredients = new JTextField();
		includeIngredients.setPreferredSize(new Dimension(100, 20));
		DocumentListener documentListener = new DocumentListener() {
		      public void changedUpdate(DocumentEvent documentEvent) {
		    	  Document source = documentEvent.getDocument();
		    	  try {
		    		  checkIngredientIncluded(recipeMap, source.getText(0,source.getLength()));
		    	  } 
		    	  catch (BadLocationException e) {
		    		  e.printStackTrace();
		    	  }
			      mediator.eventOccurred(recipeMap, mediator);
		      }
		      public void insertUpdate(DocumentEvent documentEvent) {
		    	  Document source = documentEvent.getDocument();
		    	  try {
		    		  checkIngredientIncluded(recipeMap, source.getText(0,source.getLength()));
		    	  } 
		    	  catch (BadLocationException e) {
		    		  e.printStackTrace();
		    	  }
			      mediator.eventOccurred(recipeMap, mediator);

		      }
		      public void removeUpdate(DocumentEvent documentEvent) {
		    	  Document source = documentEvent.getDocument();
		    	  try {
		    		  checkIngredientIncluded(recipeMap, source.getText(0,source.getLength()));
		    	  } 
		    	  catch (BadLocationException e) {
		    		  e.printStackTrace();
		    	  }
			      mediator.eventOccurred(recipeMap, mediator);

		      }
		};
		includeIngredients.getDocument().addDocumentListener(documentListener);
		this.add(includeIngredients);
		
		//--------------------------------------------------------------------
		
		//add field to filter ingredients that have to be excluded
		JLabel excludeLabel = new JLabel();
		excludeLabel.setText("/ exclude:");
		this.add(excludeLabel);

		JTextField excludeIngredients = new JTextField();
		excludeIngredients.setPreferredSize(new Dimension(100, 20));
		DocumentListener documentListener2 = new DocumentListener() {
		      public void changedUpdate(DocumentEvent documentEvent) {
		    	  Document source = documentEvent.getDocument();
		    	  try {
		    		  checkIngredientExcluded(recipeMap, source.getText(0,source.getLength()));
		    	  } 
		    	  catch (BadLocationException e) {
		    		  e.printStackTrace();
		    	  }
			      mediator.eventOccurred(recipeMap, mediator);
		      }
		      public void insertUpdate(DocumentEvent documentEvent) {
		    	  Document source = documentEvent.getDocument();
		    	  try {
		    		  checkIngredientExcluded(recipeMap, source.getText(0,source.getLength()));
		    	  } 
		    	  catch (BadLocationException e) {
		    		  e.printStackTrace();
		    	  }
			      mediator.eventOccurred(recipeMap, mediator);

		      }
		      public void removeUpdate(DocumentEvent documentEvent) {
		    	  Document source = documentEvent.getDocument();
		    	  try {
		    		  checkIngredientExcluded(recipeMap, source.getText(0,source.getLength()));
		    	  } 
		    	  catch (BadLocationException e) {
		    		  e.printStackTrace();
		    	  }
			      mediator.eventOccurred(recipeMap, mediator);
		      }
		};
		excludeIngredients.getDocument().addDocumentListener(documentListener2);
		this.add(excludeIngredients);

		//--------------------------------------------------------------------
		
		//add field to filter instructions that have to be included
		JLabel instructionsLabel = new JLabel();
		instructionsLabel.setText("Has instructions:");
		this.add(instructionsLabel);
		JTextField instructionsField = new JTextField();
		instructionsField.setPreferredSize(new Dimension(100, 20));
		DocumentListener documentListener3 = new DocumentListener() {
		      public void changedUpdate(DocumentEvent documentEvent) {
		    	  Document source = documentEvent.getDocument();
		    	  try {
		    		  checkInstructionsIncluded(recipeMap, source.getText(0,source.getLength()));
		    	  } 
		    	  catch (BadLocationException e) {
		    		  e.printStackTrace();
		    	  }
			      mediator.eventOccurred(recipeMap, mediator);
		      }
		      public void insertUpdate(DocumentEvent documentEvent) {
		    	  Document source = documentEvent.getDocument();
		    	  try {
		    		  checkInstructionsIncluded(recipeMap, source.getText(0,source.getLength()));
		    	  } 
		    	  catch (BadLocationException e) {
		    		  e.printStackTrace();
		    	  }
			      mediator.eventOccurred(recipeMap, mediator);

		      }
		      public void removeUpdate(DocumentEvent documentEvent) {
		    	  Document source = documentEvent.getDocument();
		    	  try {
		    		  checkInstructionsIncluded(recipeMap, source.getText(0,source.getLength()));
		    	  } 
		    	  catch (BadLocationException e) {
		    		  e.printStackTrace();
		    	  }
			      mediator.eventOccurred(recipeMap, mediator);
		      }
		};
		instructionsField.getDocument().addDocumentListener(documentListener3);
		this.add(instructionsField);

	}
	
	private void checkIngredientIncluded(HashMap<String, RecipeData> recipeMap, String filter_include) {
		
		//seperate by comma and remove spaces on start/end
		String[] ingredients_to_filter = filter_include.split(",");		
		for (int i = 0; i < ingredients_to_filter.length; i++) {
			ingredients_to_filter[i] = ingredients_to_filter[i].trim();
		}
		//for each recipe find if all the ingredients in the include bar are present, if not, filter it
		recipeMap.forEach((key, value) -> {
			List<String> ingredients = value.getIngredients();
			value.setFiltered(false, recipemanager.filterType.INGR_INCLUDED);
			for (String ingredient_to_filter : ingredients_to_filter) {
				boolean tmp = false;
				for ( String ingredient : ingredients) {
					if (ingredient.toLowerCase().contains(ingredient_to_filter.toLowerCase())) {
						tmp = true;
					}
				}
				if (tmp == false) {
					value.setFiltered(true, recipemanager.filterType.INGR_INCLUDED);
				}
				
			}
		});
	}
	
	private void checkIngredientExcluded(HashMap<String, RecipeData> recipeMap, String filter_exclude) {
		
		if (filter_exclude.length() == 0) {
			recipeMap.forEach((key, value) -> {
				value.setFiltered(false, recipemanager.filterType.INGR_EXCLUDED);
			});
			return;
		}
		String[] ingredients_to_filter = filter_exclude.split(",");		
		for (int i = 0; i < ingredients_to_filter.length; i++) {
			ingredients_to_filter[i] = ingredients_to_filter[i].trim();
		}
		recipeMap.forEach((key, value) -> {
			List<String> ingredients = value.getIngredients();
			value.setFiltered(false, recipemanager.filterType.INGR_EXCLUDED);
			for (String ingredient_to_filter : ingredients_to_filter) {
				boolean tmp = false;
				for ( String ingredient : ingredients) {
					if (ingredient.toLowerCase().contains(ingredient_to_filter.toLowerCase()) && ingredient_to_filter.length() > 0) {
						tmp = true;
					}
				}
				if (tmp == true) {
					value.setFiltered(true, recipemanager.filterType.INGR_EXCLUDED);
				}
			}
		});
	}
	
	private void checkInstructionsIncluded(HashMap<String, RecipeData> recipeMap, String filter_include) {
		
		String[] instructions_to_filter = filter_include.split(",");		
		for (int i = 0; i < instructions_to_filter.length; i++) {
			instructions_to_filter[i] = instructions_to_filter[i].trim();
		}
		recipeMap.forEach((key, value) -> {
			value.setFiltered(false, recipemanager.filterType.INSTR_INCLUDED);
			String instructions = value.getInstructions();
			for (String instruction_to_filter : instructions_to_filter) {
				if (!instructions.toLowerCase().contains(instruction_to_filter.toLowerCase())) {
					value.setFiltered(true, recipemanager.filterType.INSTR_INCLUDED);
				}
			}
		});
	}
}