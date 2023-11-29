package recipemanager;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/* Creates a panel where all the recipes that are unfiltered
 * are displayed as clickable buttons.
 */
public class RecipeListPanel extends JPanel implements PanelObserver {

	private static final long	serialVersionUID = 1L;
	private static boolean		filtered;
	private List<JButton> 		buttons = new ArrayList<>();
	ImageIcon 					veg_img = new ImageIcon("./src/veg.jpg");
	private int					n_buttons, j;
	RecipeListPanel(HashMap<String, RecipeData> recipeMap, PanelMediator mediator) {
		this.setBackground(Color.white);
		this.setLayout(new GridBagLayout());
		this.update(recipeMap, mediator);
	}

	@Override
	public void update(HashMap<String, RecipeData> recipeMap, PanelMediator mediator)  {
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST; //doesn't actually go NORTH? so fixed by using weighty
		gbc.insets = new Insets(2, 2, 2, 2);
		gbc.weightx = 1.0;
		gbc.weighty = 0.0;

		//clear buttons
        for (JButton button : buttons) {
            remove(button);
        }
		buttons.clear(); 
		//get amount of unfiltered buttons
		n_buttons = 0;
		recipeMap.forEach((key, value) -> {
    		filtered = false;
    		boolean[] filteredList = value.getFiltered();
    		for (int i = 0; i < 5; i++) {
    			if (filteredList[i] == true)
    				filtered = true;
    		}
    		if (!filtered)
    			n_buttons++;
		});
		//find all buttons to display - those who aren't filtered by any filters
		j = 0;
		recipeMap.forEach((key, value) -> {
    		filtered = false;
    		boolean[] filteredList = value.getFiltered();
    		for (int i = 0; i < 5; i++) {
    			if (filteredList[i] == true)
    				filtered = true;
    		}
    		if (!filtered) {
    			j++;
    			JButton button = new JButton();
    			button.setContentAreaFilled(false); // Remove the background
    			button.setBorderPainted(false); // Remove the border
    			button.setFont(new Font("Baghdad", Font.PLAIN, 16));
    			if (value.getVegetarian()) 
    				button.setIcon(veg_img);
    			button.setText(key);
    	        button.setHorizontalTextPosition(SwingConstants.LEFT); //text left, button right
    			button.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						recipeMap.forEach((title, value2) -> {
							if (title == key)
								value2.setDisplay(true);
							else
								value2.setDisplay(false);
						});
						mediator.eventOccurred(recipeMap, mediator);//initiates update
					}
    			});
    			if (j == n_buttons ) //let last button take up all vertical space so buttons are aligned vertically
    				gbc.weighty = 1.0;
    	        buttons.add(button);
    			this.add(button, gbc);
    			gbc.gridy++;
    		}
    	});
		revalidate();
    	repaint();
	}
}
