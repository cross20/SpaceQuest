import java.awt.*;
import java.io.*;
import java.util.*;
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
	private ArrayList<JLabel> walls = new ArrayList<>();
	
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
					JLabel wall = new JLabel(new ImageIcon(curdir + "/assets/textures/wall.png"));
					wall.setBounds(xPosition*row, yPosition*column, xPosition, yPosition);
					panel.add(wall);
					panel.revalidate();
					panel.repaint();
					walls.add(wall);
				}
				else {
					JLabel floor = new JLabel(new ImageIcon(curdir + "/assets/textures/floor.png"));
					floor.setBounds(xPosition*row, yPosition*column, xPosition, yPosition);
					panel.add(floor);
					panel.revalidate();
					panel.repaint();
				}
			}
		}
	}
	
	/**
	 * Checks whether an object is inside of the bounds. It does
	 * this by taking in the character which is traveling and
	 * the point that it is traveling to. With this, the method
	 * checks the character's size against all of the bounds. 
	 * It'll return false if any of the bounds will be violated.
	 * Otherwise, it returns true.
	 * 
	 * @return boolean
	 */
	public boolean checkRoomBounds(JLabel character, Point newP) {
		int wallX, wallY, wallW, wallH;
		
		int locX = (int)newP.getX();
		int locY = (int)newP.getY();
		int locW = character.getWidth() + locX;
		int locH = character.getHeight() + locY;
		int halfW = character.getWidth()/2;
		int halfH = character.getHeight()/2;
		
		Point top = new Point(locX + halfW, locY);
		Point topRight = new Point(locW, locY);
		Point right = new Point(locX + character.getWidth(), locY + halfH);
		Point bottomRight = new Point(locW, locH);
		Point bottom = new Point(locX + halfW, locY + character.getHeight());
		Point bottomLeft = new Point(locX, locH);
		Point left = new Point(locX, locY + halfH);
		Point topLeft = new Point(locX, locY);
		
		for(JLabel wall : walls) {
			wallX = wall.getLocation().x;
			wallY = wall.getLocation().y;
			wallW = wall.getWidth() + wallX;
			wallH = wall.getHeight() + wallY;
			
			if(top.getX() > wallX && top.getX() < wallW && top.getY() > wallY && top.getY() < wallH) {
				return false;
			}	
			else if(topRight.getX() > wallX && topRight.getX() < wallW && topRight.getY() > wallY && topRight.getY() < wallH) {
				return false;
			}
			else if(right.getX() > wallX && right.getX() < wallW && right.getY() > wallY && right.getY() < wallH) {
				return false;
			}
			else if(bottomRight.getX() > wallX && bottomRight.getX() < wallW && bottomRight.getY() > wallY && bottomRight.getY() < wallH) {
				return false;
			}
			else if(bottom.getX() > wallX && bottom.getX() < wallW && bottom.getY() > wallY && bottom.getY() < wallH) {
				return false;
			}
			else if(bottomLeft.getX() > wallX && bottomLeft.getX() < wallW && bottomLeft.getY() > wallY && bottomLeft.getY() < wallH) {
				return false;
			}
			else if(left.getX() > wallX && left.getX() < wallW && left.getY() > wallY && left.getY() < wallH) {
				return false;
			}
			else if(topLeft.getX() > wallX && topLeft.getX() < wallW && topLeft.getY() > wallY && topLeft.getY() < wallH) {
				return false;
			}
		}
		
		return true;
	}
}