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
	protected RotateLabel entity;
	
	/**
	 * Initialize {@code Entity} objects.
	 * 
	 * @param initialHealth
	 * @param initialStrength
	 * @param hitbox
	 */
	protected void createEntity(int initialHealth, int initialStrength, int hitbox, RotateLabel entity) {
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
		this.entity = entity;
	}
	
	/**
	 * Add the {@code Entity} to the game window.
	 * 
	 * @param image
	 * @param location
	 */
	protected void drawEntity(JPanel panel, Point location) {
		JLabel label = new JLabel();
		label.setLocation(location);
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
	protected void updateEntityLocation(ControllerState currState, Room currRoom) {
		if(currState.leftStickMagnitude >= SpaceQuest.minMagnitude) {
			System.out.println(entity.getLocation());
			
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
			int newX = entity.getLocation().x + (int)Math.round((Math.cos(currAngle)*5*lsmag*2));
			int newY = entity.getLocation().y - (int)Math.round((Math.sin(currAngle)*5*lsmag*2));

			// Check to see if the player will stay inside of the
			// bounds of the map. If so, update their location.
			// Otherwise, keep their location the same.
			if(currRoom.checkRoomBounds(entity, new Point(newX, newY))) {
				// Update the JLabel which represents the character.
				entity.setLocation(newX, newY);
			} else if (currRoom.checkRoomBounds(entity, new Point(entity.getLocation().x, newY))) {
				entity.setLocation(entity.getLocation().x, newY);
			} else if (currRoom.checkRoomBounds(entity, new Point(newX, entity.getLocation().y))) {
				entity.setLocation(newX, entity.getLocation().y);
			} else {
				entity.setLocation(entity.getLocation().x, entity.getLocation().y);
			}
		}
	}
	
	public void setCurrentHealth(int health) {
		if(health >= this.minHealth && health <= this.maxHealth) {
			this.currentHealth = health;
		} else {
			System.out.println("Health out of bounds. No change applied.");
		}
	}
	
	public void setCurrentStrength(int strength) {
		if(strength >= this.minStrength && strength <= this.maxStrength) {
			this.currentStrength = strength;
		} else {
			System.out.println("Strength out of bounds. No change applied.");
		}
	}
	
	/*public void fireProjectile() {
		Projectile p = new Projectile()
	}*/

	public int getCurrentHealth() {
		return this.currentHealth;
	}
	
	public int getCurrentStrength() {
		return this.currentStrength;
	}
}