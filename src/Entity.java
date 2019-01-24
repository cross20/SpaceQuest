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
		this.hitbox = hitbox;
		this.entity = entity;
	}

	/**
	 * Add the {@code Entity} to the game window.
	 *
	 * @param entity
	 * @param panel
	 * @param location
	 */
	protected void drawEntity(RotateLabel entity, JPanel panel, Point location) {
		entity.setLocation(location);
		panel.add(entity);
		panel.revalidate();
		panel.repaint();
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

	public RotateLabel getLabel() { return entity; }

	public int getCurrentHealth() {
		return this.currentHealth;
	}

	public int getCurrentStrength() {
		return this.currentStrength;
	}

	public RotateLabel getRotateLabel() {
		return this.entity;
	}
}
