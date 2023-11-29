package recipemanager;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.HashMap;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;

public class MyFrame extends JFrame {
	
	private static final long serialVersionUID = 1L;

	MyFrame(HashMap<String, RecipeData> recipeMap) {
		
		PanelMediator mediator = new PanelMediator();

		//create frame
		this.setSize(1200,800);
		this.setTitle("Recipe Manager");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//so it exists out of app instead of hiding it
		this.setResizable(false);//can't resize window
		ImageIcon codamlogo = new ImageIcon(getClass().getResource("/logo.jpg"));
		this.setIconImage(codamlogo.getImage());
		this.getContentPane().setBackground(Color.CYAN);
		this.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 10));
		
		//create panel for the filters
		RecipeFilterPanel RecipeFilterPanel = new RecipeFilterPanel(recipeMap, mediator);
		this.add(RecipeFilterPanel);
		
		//panel for adding/removing/updating recipes
		RecipeControlPanel recipeControlPanel = new RecipeControlPanel(recipeMap, mediator);
		this.add(recipeControlPanel);

		//panel for list of all recipes (filtered)	
		RecipeListPanel recipeListPanel = new RecipeListPanel(recipeMap, mediator);
		mediator.addObserver(recipeListPanel);
	    JScrollPane scrollPane = new JScrollPane(recipeListPanel);//Wrap the recipeListPanel in a JScrollPane
	    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	    scrollPane.setPreferredSize(new Dimension(400, 680));
	    getContentPane().add(scrollPane);// Add the JScrollPane to the main frame's content pane
	    
		//panel for the displaying recipes
	    RecipeTextPanel recipeTextPanel = new RecipeTextPanel(recipeMap);
		mediator.addObserver(recipeTextPanel);
	    JScrollPane scrollPane2 = new JScrollPane(recipeTextPanel);//Wrap the recipeListPanel in a JScrollPane
	    scrollPane2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	    scrollPane2.setPreferredSize(new Dimension(400, 680));
	    getContentPane().add(scrollPane2);// Add the JScrollPane to the main frame's content pane

 		
		//make app close when pressing escape while focusses on frame
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
        KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Cancel"); //$NON-NLS-1$
        getRootPane().getActionMap().put("Cancel", new AbstractAction() { 
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
                System.exit(0);
			}
        });
        
	    setLocationRelativeTo(null); // Center the frame
		this.setVisible(true);

	}
}
