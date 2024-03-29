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
	public static int currColumn = 0;
	public static int currRow = 0;

	private int roomID;
	private String lineData[][];
	private int xPosition, yPosition;
	private JPanel panel;
	private ArrayList<JLabel> walls = new ArrayList<>();
	private ArrayList<Enemy> enemies = new ArrayList<>();
	private ArrayList<Projectile> projectiles = new ArrayList<>();

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

		File f = new File(SpaceQuest.curdir + "/assets/levels.INFO");

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
	public void drawRoom(JLabel character, int x, int y) {
		// Iterate through the lineData array to find which locations are
		// boundary/wall locations. Boundaries are represented by X's in
		// the levels file.
		panel.removeAll();
		//character.setLocation(x, y);
		panel.add(character);

		for(int column = 0; column < lineData.length; column++) {
			for (int row = 0; row < lineData[0].length; row++) {
				if(lineData[column][row].equals("*")) {
					JLabel space = new JLabel(new ImageIcon(SpaceQuest.curdir + "/assets/textures/space.png"));
					space.setBounds(xPosition*row, yPosition*column, xPosition, yPosition);
					panel.add(space);
				}
				else if(lineData[column][row].equals("E")) {
					RotateLabel l = new RotateLabel(new ImageIcon(SpaceQuest.curdir + "/assets/textures/enemy.png"));
					l.setBounds(xPosition*row, yPosition*column, xPosition, yPosition);
					Enemy e = new Enemy(3, 3, 1, l);
					enemies.add(e);
					e.drawEntity(l, panel, l.getLocation());
					
					drawFloor(row, column);
				}
				else if(lineData[column][row].equals("X")) {
					JLabel wall = new JLabel(new ImageIcon(SpaceQuest.curdir + "/assets/textures/wall.png"));
					wall.setBounds(xPosition*row, yPosition*column, xPosition, yPosition);
					panel.add(wall);
					walls.add(wall);
					System.out.println("Wall found at (" + column + ", " + row + ").");
				}
				else {
					drawFloor(row, column);
				}
			}
		}
		character.setLocation(x, y);
		panel.revalidate();
		panel.repaint();
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

		// Allow for the entity and the wall objects to overlap
		// slightly to avoid inaccurate graphical collisions.
		final int buffer = 6;

		// Calculate various (separate) x and y coordinates around
		// the entity.
		int locX = (int)newP.getX() + buffer;
		int locY = (int)newP.getY() + buffer;
		int locW = character.getWidth() + locX - 2*buffer;
		int locH = character.getHeight() + locY - 2*buffer;
		int halfW = character.getWidth()/2;
		int halfH = character.getHeight()/2;

		// Join the x and y coordinates calculated above to determine
		// whether the entity has collided with a wall or not.
		Point top = new Point(locX + halfW, locY);
		Point topRight = new Point(locW, locY);
		Point right = new Point(locX + character.getWidth() - 2*buffer, locY + halfH);
		Point bottomRight = new Point(locW, locH);
		Point bottom = new Point(locX + halfW, locY + character.getHeight() - 2*buffer);
		Point bottomLeft = new Point(locX, locH);
		Point left = new Point(locX, locY + halfH);
		Point topLeft = new Point(locX, locY);

		// Run a check against each wall object to see if the entity's
		// coordinates are inside of the wall. If so, return false.
		// Otherwise, continue the check until all walls have been checked.
		for(JLabel wall : walls) {
			// Determine the boundaries of the current wall object.
			wallX = wall.getLocation().x;
			wallY = wall.getLocation().y;
			wallW = wall.getWidth() + wallX;
			wallH = wall.getHeight() + wallY;

			// Check the top-center of the entity.
			if(top.getX() > wallX && top.getX() < wallW && top.getY() > wallY && top.getY() < wallH) {
				System.out.println("Wall found at top");
				return false;
			}
			// Check the top-right of the entity.
			else if(topRight.getX() > wallX && topRight.getX() < wallW && topRight.getY() > wallY && topRight.getY() < wallH) {
				System.out.println("Wall found at top right");
				return false;
			}
			// Check the right-center of the entity.
			else if(right.getX() > wallX && right.getX() < wallW && right.getY() > wallY && right.getY() < wallH) {
				System.out.println("Wall found at right");
				return false;
			}
			// Check the bottom-right of the entity.
			else if(bottomRight.getX() > wallX && bottomRight.getX() < wallW && bottomRight.getY() > wallY && bottomRight.getY() < wallH) {
				System.out.println("Wall found at bottom right");
				return false;
			}
			// Check the bottom-center of the entity.
			else if(bottom.getX() > wallX && bottom.getX() < wallW && bottom.getY() > wallY && bottom.getY() < wallH) {
				System.out.println("Wall found at bottom");
				return false;
			}
			// Check the bottom-left of the entity.
			else if(bottomLeft.getX() > wallX && bottomLeft.getX() < wallW && bottomLeft.getY() > wallY && bottomLeft.getY() < wallH) {
				System.out.println("Wall found at bottom left");
				return false;
			}
			// Check the left-center of the entity.
			else if(left.getX() > wallX && left.getX() < wallW && left.getY() > wallY && left.getY() < wallH) {
				System.out.println("Wall found at left");
				return false;
			}
			// Check the top-left of the entity.
			else if(topLeft.getX() > wallX && topLeft.getX() < wallW && topLeft.getY() > wallY && topLeft.getY() < wallH) {
				System.out.println("Wall found at top left");
				return false;
			}
		}
		
		int enemyX, enemyW, enemyY, enemyH;
		
		for(Enemy e : enemies) {
			enemyX = e.getRotateLabel().getLocation().x;
			enemyY = e.getRotateLabel().getLocation().y;
			enemyW = enemyX + e.getRotateLabel().getWidth();
			enemyH = enemyY + e.getRotateLabel().getHeight();
			
			if(top.getX() > enemyX && top.getX() < enemyW && top.getY() > enemyY && top.getY() < enemyH) {
				System.out.println("Enemy found at top");
				return false;
			}
			// Check the top-right of the entity.
			else if(topRight.getX() > enemyX && topRight.getX() < enemyW && topRight.getY() > enemyY && topRight.getY() < enemyH) {
				System.out.println("Enemy found at top right");
				return false;
			}
			// Check the right-center of the entity.
			else if(right.getX() > enemyX && right.getX() < enemyW && right.getY() > enemyY && right.getY() < enemyH) {
				System.out.println("Enemy found at right");
				return false;
			}
			// Check the bottom-right of the entity.
			else if(bottomRight.getX() > enemyX && bottomRight.getX() < enemyW && bottomRight.getY() > enemyY && bottomRight.getY() < enemyH) {
				System.out.println("Enemy found at bottom right");
				return false;
			}
			// Check the bottom-center of the entity.
			else if(bottom.getX() > enemyX && bottom.getX() < enemyW && bottom.getY() > enemyY && bottom.getY() < enemyH) {
				System.out.println("Enemy found at bottom");
				return false;
			}
			// Check the bottom-left of the entity.
			else if(bottomLeft.getX() > enemyX && bottomLeft.getX() < enemyW && bottomLeft.getY() > enemyY && bottomLeft.getY() < enemyH) {
				System.out.println("Enemy found at bottom left");
				return false;
			}
			// Check the left-center of the entity.
			else if(left.getX() > enemyX && left.getX() < enemyW && left.getY() > enemyY && left.getY() < enemyH) {
				System.out.println("Enemy found at left");
				return false;
			}
			// Check the top-left of the entity.
			else if(topLeft.getX() > enemyX && topLeft.getX() < enemyW && topLeft.getY() > enemyY && topLeft.getY() < enemyH) {
				System.out.println("Enemy found at top left");
				return false;
			}
		}

		return true;
	}

	/**
	 * Get the size of the {@code ArrayList enemies}.
	 * @return int
	 */
	public int getNumEnemies() {
		return enemies.size();
	}

	/**
	 * Get the enemy at the specified index.
	 *
	 * @param index
	 * @return Enemy
	 */
	public Enemy getEnemy(int index) {
		return enemies.get(index);
	}

	/**
	 * Get all of the enemies in the room.
	 *
	 * @return ArrayList<Enemy>
	 */
	public ArrayList<Enemy> getEnemies() {
		return enemies;
	}

	/**
	 * Draw the floor texture at the specified location.
	 *
	 * @param row
	 * @param column
	 */
	private void drawFloor(int row, int column) {
		JLabel floor = new JLabel(new ImageIcon(SpaceQuest.curdir + "/assets/textures/floor.png"));
		floor.setBounds(xPosition*row, yPosition*column, xPosition, yPosition);
		panel.add(floor);
	}
}
