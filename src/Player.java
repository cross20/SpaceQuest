import javax.swing.*;

import com.studiohartman.jamepad.ControllerState;

import java.awt.*;

public class Player extends Entity {
	
	Player(int initialHealth, int initialStrength, int hitbox, RotateLabel character) {
		createEntity(initialHealth, initialStrength, hitbox, character);
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
		if(currState.leftStickMagnitude >= SpaceQuest.minMagnitude) {
			
			// Determine what direction to send the player.
			// For reference, 0 is right, PI/2 is upward,
			// PI is left, and -PI/2 is downward.
			double currAngle = Math.toRadians(currState.leftStickAngle);

			//if left stick magnitude is over one, change it to one to represent that player is at maximum speed
			double lsmag = currState.leftStickMagnitude;
			if (lsmag > 1.0)
				lsmag = 1.0;

			// The formula below determines what percentage out
			// of 10 pixels the player should move in the X and Y
			// directions. The result is approximated since it
			// must be type int. Add for newX because x increases
			// from left to right and subtract for newY because y
			// increases from top to bottom.
			int newX = entity.getLocation().x + (int)Math.round((Math.cos(currAngle)*10*lsmag));
			int newY = entity.getLocation().y - (int)Math.round((Math.sin(currAngle)*10*lsmag));

			// Check to see if the player will stay inside of the
			// bounds of the map. If so, update their location.
			// Otherwise, keep their location the same.
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