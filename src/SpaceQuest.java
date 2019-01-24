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
	public static int[] xRes = {1920, 1280, 1024};
	public static int[] yRes = {1080, 720, 576};
	public static int res = 1;
	private static double frameRate = 30;

	// GUI elements.
	private static JFrame frmSpacequest;
	private static JPanel panel;
	private static RotateLabel character;
	private static MainMenu mm;

	// File I/O.
	public static String curdir = System.getProperty("user.dir");
	private String OS = System.getProperty("os.name");

	// Controller.
	private static ControllerManager controllers;
	public static double minMagnitude = 0.2;

	// Game components.
	private static Room[][] map;
	private static Room currRoom;

	// Character
	private final static int characterDimensions = 50;
	private static Player p;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		// TODO: Figure out how to make the mainMenu() method work.
		runGame();
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
		// Create threads to check/run multiple game components at the same time.
		ScheduledExecutorService scheduledPool = Executors.newScheduledThreadPool(5);

		try {
			SpaceQuest window = new SpaceQuest();
			window.frmSpacequest.setBackground(Color.black);
			window.frmSpacequest.setVisible(true);
		} catch (Exception e) { e.printStackTrace(); }
		
		// Loop until a menu selection has been made on the main menu.
		while(mm.getChoice() == "NO SELECTION MADE") {
			mm.checkSelectionUpdate(controllers.getState(0));
			
			if(mm.getChoice().equals("START GAME")) {
				// Do nothing
			} else if(mm.getChoice().equals("HOW TO PLAY")) {
				mm.howToPlay();
			} else if(mm.getChoice().equals("SETTINGS")) {
				mm.settings();
			} else if(mm.getChoice().equals("QUIT GAME")) {
				quitGame();
			}
		}
		
		currRoom.drawRoom(character, xRes[res]/2, yRes[res]/2);

		// movePlayer checks to see if the the leftStick is active
		// and determines where to move the player if it is.
		Runnable movePlayer = new Runnable() {
			@Override
			public void run() {
				p.updatePlayerLocation(controllers.getState(0), currRoom);
				//currRoom.getEnemy(0).updateEnemyLocation(currRoom, p.getRotateLabel());
			}
		};

		Runnable rotatePlayer = new Runnable() {
			@Override
			public void run() {
				p.updatePlayerRotation(controllers.getState(0));
				frmSpacequest.repaint();
			}
		};

		Runnable fireProjectile = new Runnable() {	//INCOMPLETE
			@Override
			public void run() {
				ControllerState currState = controllers.getState(0);
				//System.out.println(currState.rightTrigger);
				if(currState.rightTrigger > (float)minMagnitude) {
					Projectile proj = new Projectile(p.getLabel().getX(), p.getLabel().getY(),p.getLabel().getRotation(),true);
					proj.drawEntity(proj.getLabel(), panel, p.getLabel().getLocation());
					System.out.println("pew");
					try{
						Thread.sleep(250);
					} catch (InterruptedException ex) {;}
				}
			}
		};
		
		Runnable changeRoom = new Runnable() {
			@Override
			public void run() {			
				int centerX = character.getX() + (character.getWidth() / 2);
				int centerY = character.getY() + (character.getHeight() / 2);
				int currentX = character.getX();
				int currentY = character.getY();

				// only teleport to adjacent room if there is a room to enter
				if(centerY <= 0 && Room.currRow > 0) {	//if player exits via top of screen
					currRoom = map[--Room.currRow][Room.currColumn];
					currRoom.drawRoom(character,currentX, (yRes[res] - (character.getHeight()/2 + 10)) );
					//character.setLocation(currentX, (yRes[res] - (character.getHeight()/2 + 10)));

				} else if (centerY >= yRes[res] && Room.currRow < map.length - 1) {	//if player exits via bottom of screen
					currRoom = map[++Room.currRow][Room.currColumn];
					currRoom.drawRoom(character,currentX, (0 - character.getHeight()/2 + 10));
					//character.setLocation(currentX, (0 - character.getHeight()/2 + 10));

				} else if (centerX <= 0 && Room.currColumn > 0) {	//if player exits via left of screen
					currRoom = map[Room.currRow][--Room.currColumn];
					currRoom.drawRoom(character, (xRes[res] - (character.getWidth()/2 + 10)),currentY);
					//character.setLocation((xRes[res] - (character.getWidth()/2 + 10)),currentY);

				} else if (centerX >= xRes[res] && Room.currColumn < map[0].length) { //if player exits via right of screen
					currRoom = map[Room.currRow][++Room.currColumn];
					currRoom.drawRoom(character, (0 -  character.getWidth()/2 + 10),currentY);
					//character.setLocation((0 -  character.getWidth()/2 + 10),currentY);
				}
			}
		};

		scheduledPool.scheduleWithFixedDelay(movePlayer, 0, (int)(1000.0/frameRate), TimeUnit.MILLISECONDS);
		scheduledPool.scheduleWithFixedDelay(rotatePlayer, 0, (int)(1000.0/frameRate), TimeUnit.MILLISECONDS);
		scheduledPool.scheduleWithFixedDelay(fireProjectile, 0, (int)(1000.0/frameRate), TimeUnit.MILLISECONDS);
		scheduledPool.scheduleWithFixedDelay(changeRoom, 0, (int)(1000.0/frameRate), TimeUnit.MILLISECONDS);
		
		frmSpacequest.repaint();
		
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
		quitGame();
	}
	
	private static void quitGame() {
		frmSpacequest.setVisible(false);
		frmSpacequest.dispose();
		System.exit(0);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		controllers = new ControllerManager();
		controllers.initSDLGamepad();

		// Create the JFrame
		frmSpacequest = new JFrame();
		frmSpacequest.setTitle("SpaceQuest");
		frmSpacequest.setResizable(false);
		frmSpacequest.setBounds(100, 100, xRes[res] + 6, yRes[res] + 29);
		frmSpacequest.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmSpacequest.getContentPane().setLayout(null);

		// Create the JPanel
		panel = new JPanel();
		panel.setBounds(0, 0, xRes[res], yRes[res]);
		panel.setLayout(null);
		panel.setBackground(Color.black);

		// Create the player
		character = new RotateLabel(new ImageIcon(curdir + "/assets/textures/demoCharacter.png"));
		character.setBounds(new Rectangle(xRes[res]/2, yRes[res]/2, characterDimensions, characterDimensions));
		p = new Player(10, 10, 1, character);

		// Add all GUI components to the JFrame
		frmSpacequest.getContentPane().add(panel);
		frmSpacequest.setVisible(true);

		//fill map
		map = new Room[4][5];
		for( int row = 0; row < map.length; row++) {
			for (int col = 0; col < map[row].length; col++) {
				map[row][col] = new Room(xRes[res], yRes[res], panel);
				map[row][col].initializeRoom();
			}
		}
		currRoom = map[0][0];
		
		mm = new MainMenu(xRes[res], yRes[res]);
		mm.drawMenu(panel);

	}
}
