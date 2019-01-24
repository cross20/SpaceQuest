import javax.swing.*;

public class Projectile extends Entity{
	private static ImageIcon icon = new ImageIcon(SpaceQuest.curdir + "/assets/textures/projectile.png");
	boolean isPlayerProjectile = false;
	
	public Projectile(int x, int y,double angle,boolean isPlayer) {
		entity = new RotateLabel(icon);
		entity.setLocation(x, y);
		entity.setRotation(angle);
		isPlayerProjectile = isPlayer;
	}
	
	public RotateLabel getLabel() { return entity; }
	
	public void setLocation(int x, int y) {
		entity.setLocation(x, y);
	}
}
