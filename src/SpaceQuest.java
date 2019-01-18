// this class implements Jamepad, an open-source Java gamepad library. it can be found at "https://github.com/williamahartman/Jamepad"
//
import javax.swing.*;
import java.awt.*;
import com.studiohartman.jamepad.ControllerManager;
import com.studiohartman.jamepad.ControllerState;

public class SpaceQuest {
	private int[] xRes = {1920, 1280, 1024};
	private int[] yRes = {1080, 720, 576};
	private static int res = 1;
	private static ControllerManager controllers;
	private JFrame frame;

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
		try {
			SpaceQuest window = new SpaceQuest();
			window.frame.setVisible(true);
		} catch (Exception e) { e.printStackTrace(); }
		
		while(true) {
			  ControllerState currState = controllers.getState(0);
			  
			  if(!currState.isConnected || currState.b) {
			    break;
			  }
			  if(currState.a) {
			    System.out.println("\"A\" on \"" + currState.controllerType + "\" is pressed");
			  }
			  if(currState.x) {
				System.out.println("\"X\" on \"" + currState.controllerType + "\" is pressed");
			  }
			}
		System.out.println("Exited button test.");
		
		controllers.quitSDLGamepad();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		controllers = new ControllerManager();
		controllers.initSDLGamepad();
		
		frame = new JFrame();
		frame.setBounds(100, 100, xRes[res] + 15, yRes[res] + 39);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, xRes[res], yRes[res]);
		frame.getContentPane().add(panel);
		panel.setLayout(null);

	}

}
