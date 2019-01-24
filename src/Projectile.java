import java.awt.*;
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
	
	public void updateLocation(double currAngle, Room currRoom) {
		
		int newX = entity.getLocation().x + (int)Math.round((Math.cos(currAngle)*20));
		int newY = entity.getLocation().y - (int)Math.round((Math.sin(currAngle)*20));
		
		// Check to see if the projectile will stay inside of the
		// bounds of the map. If so, update its location.
		// Otherwise, keep its location the same.
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
