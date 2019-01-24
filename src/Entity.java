import java.awt.*;
import javax.swing.*;

import com.studiohartman.jamepad.ControllerState;

/**
 * This class manages {@code Entity} objects. These objects
 * represent both {@code Player} and {@code Enemy} objects.
 * 
 * @author Kyle Shepard, Chad Ross
 */
abstract class Entity {
	protected int hitbox;
	protected int maxHealth, minHealth, currentHealth;
	protected int maxStrength, minStrength, currentStrength;
	protected double maxMomentum, minMomentum, currentMomentum;
	
	/**
	 * Initialize {@code Entity} objects.
	 * 
	 * @param initialHealth
	 * @param initialStrength
	 * @param hitbox
	 */
	protected void createEntity(int initialHealth, int initialStrength, int hitbox) {
		this.maxHealth = 10;
		this.minHealth = 0;
		this.currentHealth = initialHealth;
		this.maxStrength = 10;
		this.minStrength = 0;
		this.currentStrength = initialStrength;
		this.maxMomentum = 10;
		this.minMomentum = -10;
		this.currentMomentum = 0;
		this.hitbox = hitbox;
	}
	
	/**
	 * Add the {@code Entity} to the game window.
	 * 
	 * @param image
	 * @param location
	 */
	protected void drawEntity(JPanel panel, ImageIcon image, Rectangle location) {
		JLabel label = new JLabel(image);
		label.setBounds(location);
		panel.add(label);
		panel.revalidate();
		panel.repaint();
	}
	
	/**
	 * Checks the controller state against the location that an entity
	 * is attempting to travel to to determine if the move is legal. If
	 * the move is legal, update their location to that location. Else,
	 * keep the entity where it is.
	 * 
	 * @param currState
	 * @param character
	 * @param currRoom
	 */
	protected void updateEntityLocation(ControllerState currState, RotateLabel character, Room currRoom) {
		if(currState.leftStickMagnitude >= SpaceQuest.minMagnitude) {
			System.out.println(character.getLocation());
			
			// Determine what direction to send the player.
			// For reference, 0 is right, PI/2 is upward,
			// PI is left, and -PI/2 is downward.
			double currAngle = Math.toRadians(currState.leftStickAngle);

			//if left stick magnitude is over one, change it to one to represent that player is at maximum speed
			double lsmag = currState.leftStickMagnitude;
			if (lsmag > 1.0)
				lsmag = 1.0;

			// The formula below determines what percentage out
			// of 5 pixels the player should move in the X and Y
			// directions. The result is approximated since it
			// must be type int. Add for newX because x increases
			// from left to right and subtract for newY because y
			// increases from top to bottom.
			int newX = character.getLocation().x + (int)Math.round((Math.cos(currAngle)*5*lsmag*2));
			int newY = character.getLocation().y - (int)Math.round((Math.sin(currAngle)*5*lsmag*2));

			// Check to see if the player will stay inside of the
			// bounds of the map. If so, update their location.
			// Otherwise, keep their location the same.
			if(currRoom.checkRoomBounds(character, new Point(newX, newY))) {
				// Update the JLabel which represents the character.
				character.setLocation(newX, newY);
			} else if (currRoom.checkRoomBounds(character, new Point(character.getLocation().x, newY))) {
				character.setLocation(character.getLocation().x, newY);
			} else if (currRoom.checkRoomBounds(character, new Point(newX, character.getLocation().y))) {
				character.setLocation(newX, character.getLocation().y);
			} else {
				character.setLocation(character.getLocation().x, character.getLocation().y);
			}
		}
	}
	
	/**
	 * Sets the entity's rotation equal to the angle of the right stick.
	 * 
	 * @param currState
	 * @param character
	 */
	protected void updateEntityRotation(ControllerState currState, RotateLabel character) {
		if(currState.rightStickMagnitude >= SpaceQuest.minMagnitude) {
			character.setRotation(currState.rightStickAngle);
			character.setLocation(character.getLocation().x, character.getLocation().y);
		}
	}
}