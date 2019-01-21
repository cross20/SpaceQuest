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
	
	Room(int xRes, int yRes) {
		this.xPosition = xRes/32;
		this.yPosition = yRes/18;
		roomID = roomIDPool++;
	}
	
	public void createMap(JPanel panel) {
		this.panel = panel;
		
		readRoom();
		drawRoom();
	}
	
	private void readRoom() {
		String splitBy = " "; //information segmented by spaces
		
		File f = new File(curdir + "/assets/levels.INFO");
		
		try(FileInputStream is = new FileInputStream(f)) {
			InputStreamReader ir = new InputStreamReader(is);
			BufferedReader rdr = new BufferedReader(ir);
			
			for(int i = 0; i < (roomID * 19); i++)
				rdr.readLine();
			
			
			lineData = new String[18][32];
			
			for (int i = 0; i < lineData.length; i++) {
				String line = rdr.readLine();
				this.lineData[i] = line.split(splitBy);
			}
			
			
		} catch(Exception ex) { System.out.printf("Failed for %s\n", f.getName()); }
	}
	
	private void drawRoom() {
		for(int column = 0; column < lineData.length; column++) {
			for (int row = 0; row < lineData[0].length; row++) {
				if(lineData[column][row].equals("X")) {
					JLabel test = new JLabel(new ImageIcon(curdir + "/assets/textures/wall.png"));
					test.setBounds(xPosition*row, yPosition*column, xPosition, yPosition);
					panel.add(test);
					panel.revalidate();
					panel.repaint();
				}
				
				System.out.print(lineData[column][row] + " ");
			}
			System.out.println();
		}
	}
}
