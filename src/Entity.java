import java.awt.*;
import javax.swing.*;

/**
 * 
 * @author Kyle Shepard, Chad Ross
 */
abstract class Entity {
	protected ImageIcon entityImage;
	protected Rectangle entityLocation;
	protected int hitbox;
	protected int maxHealth, minHealth, currentHealth;
	protected int maxStrength, minStrength, currentStrength;
	protected double maxMomentum, minMomentum, currentMomentum;
	
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
	
	protected void drawEntity(ImageIcon image, Rectangle location) {
		
	}
}