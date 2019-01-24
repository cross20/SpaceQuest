import javax.swing.*;

import com.studiohartman.jamepad.ControllerState;

import java.awt.*;

public class Player extends Entity {
	private RotateLabel character;
	
	Player(int initialHealth, int initialStrength, int hitbox, JPanel panel, ImageIcon image, Rectangle location) {
		createEntity(initialHealth, initialStrength, hitbox);
		drawEntity(panel, image, location);
	}
	
	Player(RotateLabel character) {
		this.character = character;
	}
	
	/**
	 * Checks the controller state against the location that a player
	 * is attempting to travel to to determine if the move is legal. If
	 * the move is legal, update their location to that location. Else,
	 * keep the player where they are.
	 * 
	 * @param currState
	 * @param currRoom
	 */
	public void updatePlayerLocation(ControllerState currState, Room currRoom) {
		updateEntityLocation(currState, character, currRoom);
	}
	
	/**
	 * Sets the player's rotation equal to the angle of the right stick.
	 * 
	 * @param currState
	 */
	public void updatePlayerRotation(ControllerState currState) {
		updateEntityRotation(currState, character);
	}
}