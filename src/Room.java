import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import javax.swing.*;

/**
 * This class manages each {@code Room} inside of
 * the game map.
 * 
 * @author Kyle Shepard, Chad Ross
 */
public class Room {
	private static int roomIDPool = 0;
	private int roomID;
	private String curdir = System.getProperty("user.dir");
	private String lineData[][];
	private int xPosition, yPosition;
	private JPanel panel;
	
	Room(int xRes, int yRes, JPanel panel) {
		// Since the screen resolution is based on
		// a 32:18 aspect ratio, determine the size
		// needed for boundaries to be drawn in the
		// JFrame (added to the JFrame).
		this.xPosition = xRes/32;
		this.yPosition = yRes/18;
		this.panel = panel;
		roomID = roomIDPool++;
	}
	
	/**
	 * Read in data from the levels file found in the assets 
	 * folder. Store that data for use when printing a room.
	 */
	public void initializeRoom() {
		String splitBy = " "; // Information is separated by spaces in the levels file.
		
		File f = new File(curdir + "/assets/levels.INFO");
		
		// Read in text from the file to create the room map.
		try(FileInputStream is = new FileInputStream(f)) {
			InputStreamReader ir = new InputStreamReader(is);
			BufferedReader rdr = new BufferedReader(ir);
			
			// Determine which location in the levels file to
			// start reading in room information. Each room
			// consists of 19 lines of information so, using
			// roomID * 19 gives the line number which should
			// be used to start reading in information.
			for(int i = 0; i < (roomID * 19); i++) {
				rdr.readLine();
			}

			// Initialize the lineData array to hold room
			// map information. All rooms use a 32:18 map. 
			// So, make 18 rows and 32 columns to match
			// the map in the levels file.
			lineData = new String[18][32];
			
			// Read the line data and store it in a 2D array.
			for (int i = 0; i < lineData.length; i++) {
				String line = rdr.readLine();
				this.lineData[i] = line.split(splitBy);
			}	
		} catch(Exception ex) { System.out.printf("Failed for %s\n", f.getName()); }
	}
	
	/**
	 * Generate a JLabel at each location which represents a boundary.
	 */
	public void drawRoom() {
		// Iterate through the lineData array to find which locations are
		// boundary/wall locations. Boundaries are represented by X's in
		// the levels file.
		for(int column = 0; column < lineData.length; column++) {
			for (int row = 0; row < lineData[0].length; row++) {
				if(lineData[column][row].equals("X")) {
					JLabel test = new JLabel(new ImageIcon(curdir + "/assets/textures/wall.png"));
					test.setBounds(xPosition*row, yPosition*column, xPosition, yPosition);
					panel.add(test);
					panel.revalidate();
					panel.repaint();
				}
				else {
					JLabel test = new JLabel(new ImageIcon(curdir + "/assets/textures/floor.png"));
					test.setBounds(xPosition*row, yPosition*column, xPosition, yPosition);
					panel.add(test);
					panel.revalidate();
					panel.repaint();
				}
			}
		}
	}
}
