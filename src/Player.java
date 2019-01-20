import javax.swing.*;
import java.awt.*;

public class Player extends Entity {
	Player(int initialHealth, int initialStrength, int hitbox, ImageIcon image, Rectangle location) {
		createEntity(initialHealth, initialStrength, hitbox);
		drawEntity(image, location);
	}
}
