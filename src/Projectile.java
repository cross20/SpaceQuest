import javax.swing.*;

public class Projectile {
	RotateLabel label;
	private static ImageIcon icon = new ImageIcon(System.getProperty("user.dir") + "/assets/textures/projectile.png");

	public Projectile(int x, int y,double angle) {
		label = new RotateLabel(icon);
		label.setLocation(x, y);
		label.setRotation(angle);
	}
	
	
	
}
