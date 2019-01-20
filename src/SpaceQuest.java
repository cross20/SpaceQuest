// this class implements Jamepad, an open-source Java gamepad library. it can be found at "https://github.com/williamahartman/Jamepad"

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import com.studiohartman.jamepad.*;
import java.lang.Math;
import java.util.*;

public class SpaceQuest {
	private int[] xRes = {1920, 1280, 1024};
	private int[] yRes = {1080, 720, 576};
	private static int res = 1;
	private static double minMagnitude = 0.15;
	private Room[][] map;
	private static ControllerManager controllers;
	private String curdir = System.getProperty("user.dir");
	private String OS = System.getProperty("os.name");
	private JFrame frame;
	JPanel panel;
	private ArrayList<JLabel> labels = new ArrayList<>();
	JLabel character = new JLabel("Josh Miller");

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
	 * run the game
	 */
	public static void run() {
		SpaceQuest window = null;
		
		try {
			window = new SpaceQuest();
			window.frame.setVisible(true);
		} catch (Exception e) { e.printStackTrace(); }
		
		// Gameloop.
		while(true) {
			ControllerState currState = controllers.getState(0);

			  if(!currState.isConnected || currState.b) {
			    break;
			    // Passed
			  }
			  if(currState.a) {
				  // TEST
				  System.out.println("\"A\" on \"" + currState.controllerType + "\" is pressed");
				  // Passed
			  }
			  if(currState.x) {
				  // TEST
				  System.out.println("\"X\" on \"" + currState.controllerType + "\" is pressed");
				  // Passed
			  }
			  if(currState.rightStickMagnitude >= minMagnitude) {
				  // TEST
				  System.out.println("\"Right Stick\" pushed " + currState.rightStickAngle + " degrees.");
				  System.out.println("\"Right Stick\" magnitude " + currState.rightStickMagnitude + ".");
				  // Passed. Due to manufacturing imperfections, keep minMagnitude >= 0.13. Recommended 0.15 for best results.
				  
				  // TEST
				  try {
					  JLabel character = new JLabel("Josh Millerrrrr");
					  character.setBounds(new Rectangle(10, 10, 100, 100));
					  character.setVisible(true);
					  window.panel.add(character);
					  window.frame.getContentPane().add(window.panel);
					  window.frame.setVisible(true);
				  } catch(Exception e) { System.out.println("Exception found at character creation. Exception: " + e.toString()); }
				  // Failed. JLabel does not appear in window.
			  }
				  
			  if(currState.leftStickMagnitude >= minMagnitude) {
				  // TEST
				  System.out.println("\"Left\" stick pushed " + currState.leftStickAngle + " degrees.");
				  System.out.println("\"Left Stick\" magnitude " + currState.leftStickMagnitude + ".");
				  // Passed. Due to manufacturing imperfections, keep minMagnitude >= 0.13. Recommended 0.15 for best results.
				  Double xVelocity = currState.rightStickMagnitude * Math.cos(Math.toRadians(currState.rightStickAngle));
				  Double yVelocity = currState.rightStickMagnitude * Math.sin(Math.toRadians(currState.rightStickAngle));
			  }
			}
		System.out.println("Exited test.");
		
		controllers.quitSDLGamepad();
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		controllers = new ControllerManager();
		controllers.initSDLGamepad();
		
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, xRes[res] + 6, yRes[res] + 29);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		panel = new JPanel();
		panel.setBounds(0, 0, xRes[res], yRes[res]);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		frame.setVisible(true);
		
		String splitBy = " ";	//information segmented by spaces
		
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