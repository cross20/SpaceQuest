import javax.swing.*;

import com.studiohartman.jamepad.ControllerState;

import java.awt.*;

public class Player extends Entity {
	
	Player(int initialHealth, int initialStrength, int hitbox, RotateLabel character) {
		createEntity(initialHealth, initialStrength, hitbox, character);
	}
	
	/**
	 * Sets the player's rotation equal to the angle of the right stick.
	 * 
	 * @param currState
	 */
	public void updatePlayerRotation(ControllerState currState) {
		if(currState.rightStickMagnitude >= SpaceQuest.minMagnitude) {
			entity.setRotation(currState.rightStickAngle);
			entity.setLocation(entity.getLocation().x, entity.getLocation().y);
		}
	}
}