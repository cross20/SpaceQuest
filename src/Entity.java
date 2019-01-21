import java.awt.*;
import javax.swing.*;

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
}