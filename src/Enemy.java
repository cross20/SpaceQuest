import java.awt.*;
import javax.swing.*;

class Enemy extends Entity {
	private RotateLabel enemy;
	
	Enemy(int initialHealth, int initialStrength, int hitbox, RotateLabel enemy) {
		createEntity(initialHealth, initialStrength, hitbox, enemy);
	}
	
	public void updateEnemyLocation(Room currRoom, RotateLabel target) {
		double xDistance = (target.getLocation().x + target.getWidth()/2) - (entity.getLocation().x + entity.getWidth()/2);
		double yDistance = (target.getLocation().y + target.getHeight()/2) - (entity.getLocation().y + entity.getWidth()/2);
		
		double theta = Math.atan(yDistance/xDistance);
		
		int newX = entity.getLocation().x + (int)Math.round((Math.cos(theta)*10));
		int newY = entity.getLocation().y - (int)Math.round((Math.sin(theta)*10));
		
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