// SOURCES:
// Jamepad -- "https://github.com/williamahartman/Jamepad"
// ScheduleThreadPoolExecuter -- https://examples.javacodegeeks.com/core-java/util/concurrent/scheduledthreadpoolexecutor/java-util-concurrent-scheduledthreadpoolexecutor-example/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import com.studiohartman.jamepad.*;
import java.lang.Math;
import java.util.*;
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
	private int[] xRes = {1920, 1280, 1024};
	private int[] yRes = {1080, 720, 576};
	private static int res = 1;
	
	// File I/O.
	private String curdir = System.getProperty("user.dir");
	private String OS = System.getProperty("os.name");
	
	// Static members.
	private static ControllerManager controllers;
	private static double minMagnitude = 0.2;
	private static JFrame frame;
	private static JPanel panel;
	private static JLabel character;
	
	// Game components.
	private Room[][] map;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		run();
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
	public static void run() {
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
					
					// The formula below determines what percentage out
					// of 5 pixels the player should move in the X and Y
					// directions. The result is approximated since it
					// must be type int.
					int changeX = (int)Math.round((Math.cos(currAngle)*10)/2);
					int changeY = (int)Math.round((Math.sin(currAngle)*10)/2);
					
					// Update the JLabel which represents the character. Since
					// the positive X direction moves from left to right, add
					// changeX to the character's current location. Since the
					// positive Y direction moves from top to bottom, subtract
					// changY from the character's current location (to avoid
					// the Y direction being inverted).
					character.setLocation(character.getLocation().x + changeX, character.getLocation().y - changeY );
				}
			}
		};
		
		// Schedule movePlayer to check for controller updates every 10 milisecions.
		scheduledPool.scheduleWithFixedDelay(movePlayer, 0, 10, TimeUnit.MILLISECONDS);
		
		// This loop will check to see if the game exit conditions are satisfied.
		// If they are, then stop all threads and execute the game exit process.
		while(true) {
			// Check the current state of the controller.
			ControllerState currState = controllers.getState(0);
			
			// Check for exit conditions.
			if(!currState.isConnected || currState.b) {
			    character.setText("Game over");
				break;
			}
		}
		
		System.out.println("Game exit initialized.");
		
		// Process to exit the game.
		scheduledPool.shutdown();
		controllers.quitSDLGamepad();
	}
	
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
		character = new JLabel("Josh Miller");
		character.setBounds(new Rectangle(20, 20, 100, 30));
		
		// Add all GUI components to the JFrame
		panel.add(character);
		frame.getContentPane().add(panel);
		frame.setVisible(true);
		
		String splitBy = " "; //information segmented by spaces
		
		File f;
		
		// Different operating systems may use different file path systems.
		System.out.println("OS: " + OS);
        if(OS.equals("Mac OS X")) {
        	f =  new File(curdir + "/assets/levels.INFO");
        	System.out.println("Mac detected.");
        } else {
        	f = new File(curdir + "\\assets\\levels.INFO");
        	System.out.println("Other OS detected.");
        }
        
        try(FileInputStream is = new FileInputStream(f)) {
        	InputStreamReader ir = new InputStreamReader(is);
        	BufferedReader rdr = new BufferedReader(ir);
        	String line = rdr.readLine();
        	
        	while(line != null) {
        		String[] lineData = line.split(splitBy);
        		
        		line = rdr.readLine();
        	}
        } catch(Exception ex) { System.out.printf("Failed for %s\n", f.getName()); }
        
        // ISSUE: cd.listFiles() returns null.
        /*File cd =  new File(curdir + "\\assets");
        File[] assets = cd.listFiles();
        
        for (File f: assets) {
        	
            if (f.getName().equals("levels.INFO")) {
            	
            	try (FileInputStream is = new FileInputStream(f)) {
                    InputStreamReader ir = new InputStreamReader(is);
                    BufferedReader rdr = new BufferedReader(ir);
                    String line = rdr.readLine();
                    
                    while (line != null) {
                    	String[] lineData = line.split(splitBy);
                    	
                        line = rdr.readLine();
                        
                    }
                }
                catch (Exception ex) { System.out.printf("Failed for %s\n", f.getName()); }
            }
        }*/
	}
}