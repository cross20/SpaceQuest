import java.awt.*;
import java.util.*;
import javax.swing.*;
import com.studiohartman.jamepad.ControllerState;

/**
 * This class is in charge of formatting and displaying the menu.
 * This includes tracking the current state of the menu, updating
 * the menu according to that state, and returning the selection
 * based upon the state.
 * 
 * @author Chad Ross
 */
public class MainMenu {
	private String choice;
	private int xCenter, yCenter;
	private String[] selectionText = {"START GAME", "SETTINGS", "HOW TO PLAY", "QUIT GAME"};
	private JLabel[] selectionLabels = new JLabel[selectionText.length];
	private int selectionIndex;
	
	/**
	 * Initialize the main menu according to the specified
	 * screen resolution. All resolutions should be 16:9.
	 * 
	 * @param xRes
	 * @param yRes
	 */
	MainMenu(int xRes, int yRes) {
		this.choice = "NO SELECTION MADE";
		this.xCenter = xRes / 2;
		this.yCenter = yRes / 2;
		this.selectionIndex = 0;
	}
	
	/**
	 * Add all of the GUI components to the main menu.
	 * 
	 * @param panel
	 */
	public void drawMenu(JPanel panel) {
		String curdir = System.getProperty("user.dir");
		Random rnd = new Random();
		int r = rnd.nextInt(20);
		
		// Setup the game logo.
		JLabel title;
		
		// Check to see which title logo to choose. This is an
		// easter egg. Occasionally, the game will flip the logo
		// vertically (about 1 in every 20 times the game runs).
		if(r == 1)
			title = new JLabel(new ImageIcon(curdir + "/assets/textures/SpaceQuestFlip.png"));
		else
			title = new JLabel(new ImageIcon(curdir + "/assets/textures/SpaceQuest.png"));
		
		title.setBounds(xCenter - 96, yCenter - 130, 192, 108);
		panel.removeAll();
		panel.add(title);
		
		// Setup the JLabels that hold the menu selection choices. This
		// is based upon the values found in the selectionText array.
		// The default option is START GAME which is always at index 0.
		for(int i = 0; i < selectionText.length; i++) {
			JLabel l = new JLabel(selectionText[i], SwingConstants.CENTER);
			if(i == 0) {
				l.setForeground(Color.white);
				l.setFont(new Font("Dialog", Font.PLAIN, 15));
			} else
				l.setForeground(Color.gray);
			l.setBounds(xCenter - 55, yCenter + (i * 35), 110, 40);
			panel.add(l);
			selectionLabels[i] = l;
		}
		
		// Add all the components to the panel.
		panel.revalidate();
		panel.repaint();
	}
	
	/**
	 * Check for any changes made to the main menu. This is based
	 * off of the current state of the controller.
	 * 
	 * @param currState
	 */
	public void checkSelectionUpdate(ControllerState currState) {
		if(currState.leftStickMagnitude >= SpaceQuest.minMagnitude) {
			
			// Limit the speed that the player can cycle through the options.
			try {
				Thread.sleep(150);
			} catch(Exception ex) { System.out.println("Sleep failed for " + ex.toString()); }
			
			// Check to see if the player chooses to cycle up in the menu.
			if((currState.leftStickAngle >= 45 && currState.leftStickAngle <= 135)) {
				
				// Update old selection. Items which are not selected are
				// greyed out and a smaller font than the selected item.
				selectionLabels[selectionIndex].setForeground(Color.gray);
				selectionLabels[selectionIndex].setFont(new Font("Dialog", Font.PLAIN, 12));
				
				// Keep the selection index in the bounds of the array which holds the options.
				if(selectionIndex <= 0)
					selectionIndex = selectionText.length - 1; 
				else
					selectionIndex--;
				
				// Update new selection. Selected items appear as white
				// and are a larger font than non-selected items.
				selectionLabels[selectionIndex].setForeground(Color.white);
				selectionLabels[selectionIndex].setFont(new Font("Dialog", Font.PLAIN, 15));
			} 
			
			// Check to see if the player chooses to cycle down in the menu.
			else if(currState.leftStickAngle <= -45 && currState.leftStickAngle >= -135) {
				
				// Update old selection. Items which are not selected are
				// greyed out and a smaller font than the selected item.
				selectionLabels[selectionIndex].setForeground(Color.gray);
				selectionLabels[selectionIndex].setFont(new Font("Dialog", Font.PLAIN, 12));
				
				// Keep the selection index in the bounds of the array which holds the options.
				if(selectionIndex >= selectionText.length - 1)
					selectionIndex = 0;
				else
					selectionIndex++;
				
				// Update new selection. Selected items appear as white
				// and are a larger font than non-selected items.
				selectionLabels[selectionIndex].setForeground(Color.white);
				selectionLabels[selectionIndex].setFont(new Font("Dialog", Font.PLAIN, 15));
			}
		} 
		
		// Check to see if an item has been selected.
		else if(currState.aJustPressed) {
			this.choice = selectionText[selectionIndex];
		} 
		
		// Check to see if the quit game option has been selected ('b' prompts this action).
		else if(currState.bJustPressed) {
			this.choice = "QUIT GAME";
		}
	}
	
	/**
	 * Return the choice that was made by the player. If no
	 * choice was made, the default is "NO SELECTION MADE".
	 * 
	 * @return choice
	 */
	public String getChoice() {
		return this.choice;
	}
}
