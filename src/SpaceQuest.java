// SOURCES:
// Jamepad -- "https://github.com/williamahartman/Jamepad"
// ScheduleThreadPoolExecuter -- https://examples.javacodegeeks.com/core-java/util/concurrent/scheduledthreadpoolexecutor/java-util-concurrent-scheduledthreadpoolexecutor-example/

import javax.swing.*;
import java.awt.*;
import com.studiohartman.jamepad.*;
import java.lang.Math;
import java.util.concurrent.*;

/**
 * This class runs the entire game of SpaceQuest. To do this, it performs two main tasks.
 * The first task is to initialize the graphics of the game. After doing this, the second 
 * task is to run the game loop until the game exit conditions are met.
 * 
 * @since 20 January 2019
 * @version 1.0
 * @author Kyle Shepard, Chad Ross
 */
public class SpaceQuest {
	// Resolution options.
	private static int[] xRes = {1920, 1280, 1024};
	private static int[] yRes = {1080, 720, 576};
	private static int res = 1;
	
	// GUI elements.
	private static JFrame frame;
	private static JPanel panel;
	private static RotateLabel character;
	
	// File I/O.
	private String curdir = System.getProperty("user.dir");
	private String OS = System.getProperty("os.name");
	
	// Controller.
	private static ControllerManager controllers;
	private static double minMagnitude = 0.2;
	
	// Game components.
	private Room[][] map;
	static Room r;
	
	// Character
	private final static int characterDimensions = 50;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		// TODO: Figure out how to make the mainMenu() method work.
		runGame();
		//mainMenu();
	}

	/**
	 * Create the application.
	 */
	public SpaceQuest() {
		initialize();
	}
	
	/**
	 * Run the game
	 */
	public static void runGame() {
		// TODO: Initialize the game map.
		
		// Create threads to check/run multiple game components at the same time.
		ScheduledExecutorService scheduledPool = Executors.newScheduledThreadPool(5);
		
		try {
			SpaceQuest window = new SpaceQuest();
			window.frame.setVisible(true);
		} catch (Exception e) { e.printStackTrace(); }
		
		// movePlayer checks to see if the the leftStick is active
		// and determines where to move the player if it is.
		Runnable movePlayer = new Runnable() {
			@Override
			public void run() {
				// Check to current state of the controller.
				ControllerState currState = controllers.getState(0);
				
				if(currState.leftStickMagnitude >= minMagnitude) {
					// Determine what direction to send the player.
					// For reference, 0 is right, PI/2 is upward,
					// PI is left, and -PI/2 is downward.
					double currAngle = Math.toRadians(currState.leftStickAngle);
					
					//if left stick magnitude is over one, change it to one to represent that player is at maximum speed
					double lsmag = currState.leftStickMagnitude;
					if (lsmag > 1.0)
						lsmag = 1.0;
					
					// The formula below determines what percentage out
					// of 5 pixels the player should move in the X and Y
					// directions. The result is approximated since it
					// must be type int. Add for newX because x increases
					// from left to right and subtract for newY because y
					// increases from top to bottom.
					int newX = character.getLocation().x + (int)Math.round((Math.cos(currAngle)*5*lsmag));
					int newY = character.getLocation().y - (int)Math.round((Math.sin(currAngle)*5*lsmag));
					
					// Check to see if the player will stay inside of the
					// bounds of the map. If so, update their location.
					// Otherwise, keep their location the same.
					if(r.checkRoomBounds(character, new Point(newX, newY))) {
						// Update the JLabel which represents the character.
						character.setLocation(newX, newY);
					} else if (r.checkRoomBounds(character, new Point(character.getLocation().x, newY))) {
						character.setLocation(character.getLocation().x, newY);
					} else if (r.checkRoomBounds(character, new Point(newX, character.getLocation().y))) {
						character.setLocation(newX, character.getLocation().y);
					} else {
						character.setLocation(character.getLocation().x, character.getLocation().y);
					}
					
				}
			}
		};
		
		Runnable rotatePlayer = new Runnable() {
			// TODO: Fix the rotation so that it doesn't depend upon the player moving.
			
			@Override
			public void run() {
				// Check to current state of the controller.
				ControllerState currState = controllers.getState(0);
				
				if(currState.rightStickMagnitude >= minMagnitude) {
					character.setRotation(currState.rightStickAngle);
					character.setLocation(character.getLocation().x, character.getLocation().y);
				}
			}
		};
		
		// Schedule to check for controller updates every 10 milliseconds.
		scheduledPool.scheduleWithFixedDelay(movePlayer, 0, 10, TimeUnit.MILLISECONDS);
		scheduledPool.scheduleWithFixedDelay(rotatePlayer, 0, 10, TimeUnit.MILLISECONDS);
		
		// This loop will check to see if the game exit conditions are satisfied.
		// If they are, then stop all threads and execute the game exit process.
		while(true) {
			// Check the current state of the controller.
			ControllerState currState = controllers.getState(0);
			
			// Check for exit conditions.
			if(!currState.isConnected || currState.b) {
				break;
			}
		}
		
		System.out.println("Game exit initialized.");
		
		// Process to exit the game.
		scheduledPool.shutdown();
		controllers.quitSDLGamepad();
	}
	
	/*public static void mainMenu() {
		JFrame testFrame = new JFrame();
		testFrame.setResizable(false);
		testFrame.setBounds(100, 100, xRes[res] + 6, yRes[res] + 29);
		testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		testFrame.getContentPane().setLayout(null);
		
		JPanel menuPanel = new JPanel();
		menuPanel.setBounds(0, 0, xRes[res], yRes[res]);
		menuPanel.setLayout(null);
		
		JLabel test = new JLabel("Test");
		test.setBounds(new Rectangle(20, 20, xRes[res]/32, yRes[res]/18));
		
		menuPanel.add(test);
		testFrame.getContentPane().add(menuPanel);
		testFrame.setVisible(true);
		
		while(true) {
			ControllerState currState = controllers.getState(0);
			
			if(currState.a) {
				runGame();
			}
			if(currState.b || !currState.isConnected) {
				break;
			}
		}
	}*/
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		controllers = new ControllerManager();
		controllers.initSDLGamepad();
		
		// Create the JFrame
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, xRes[res] + 6, yRes[res] + 29);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		// Create the JPanel
		panel = new JPanel();
		panel.setBounds(0, 0, xRes[res], yRes[res]);
		panel.setLayout(null);
		
		// Create the player
		character = new RotateLabel(new ImageIcon(curdir + "/assets/textures/demoCharacter.png"));
		character.setBounds(new Rectangle(256, 128, characterDimensions, characterDimensions));
		
		// TODO: Initialize the game menu.
		
		// Add all GUI components to the JFrame
		panel.add(character);
		frame.getContentPane().add(panel);
		frame.setVisible(true);
		
		r = new Room(xRes[res], yRes[res], panel);
		r.initializeRoom();
		r.drawRoom();
	}
}