import java.awt.*;
import javax.swing.*;

class Enemy extends Entity {
	Enemy(int initialHealth, int initialStrength, int hitbox, JPanel panel, ImageIcon image, Rectangle location) {
		createEntity(initialHealth, initialStrength, hitbox);
		drawEntity(panel, image, location);
	}
}