import java.awt.*;
import javax.swing.*;

class Enemy extends Entity {
	private RotateLabel enemy;
	
	Enemy(int initialHealth, int initialStrength, int hitbox, RotateLabel enemy) {
		createEntity(initialHealth, initialStrength, hitbox, enemy);
	}
}