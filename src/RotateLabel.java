// SOURCES:
// https://www.dreamincode.net/forums/topic/266194-rotating-a-jlabel/

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class RotateLabel extends JLabel {
	private static final long serialVersionUID = 1L;
	private double angle = 0;
	
	public RotateLabel( String text, int x, int y ) {
		super(text);
		int width = getPreferredSize().width;
		int height = getPreferredSize().height;
		
		setBounds(x, y, width, height);
	}
	
	public RotateLabel(ImageIcon i) {
		super(i);
		int width = getPreferredSize().width;
		int height = getPreferredSize().height;
	}
	
	@Override
	public void paintComponent( Graphics g ) {
		Graphics2D gx = (Graphics2D) g;
		gx.rotate(-Math.toRadians(angle+ 270), getWidth() / 2, getHeight() / 2);
		super.paintComponent(g);
	}
	
	public void setRotation( double angle ) { this.angle = angle; }
	public double getRotation() { return angle; }
}