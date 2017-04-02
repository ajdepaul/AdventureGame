package game;

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Launches the game.
 * 
 * @author Anthony DePaul
 */
@SuppressWarnings("serial")
public class GameLauncher extends JApplet {

	// --------------------------------------------------------- VARIABLE DECLARATIONS

	/** Scale used for game window */
	public static double inputScale = 1;
	/** If the game is in windowed mode */
	public static boolean windowed = false;
	/** If the game is an applet or application */
	public static boolean isApplet = false;
	/** The game object itself */
	public static Game game = new Game();
	/** The window for the game */
	public static JFrame frame = new JFrame();

	// --------------------------------------------------------- APPLET

	/**
	 * Initialization for applet
	 */
	@Override
	public void init() {
		isApplet = true;
		add(game);
		Dimension dim = new Dimension((int) (game.data.WIDTH * GameLauncher.inputScale), (int) (game.data.HEIGHT * GameLauncher.inputScale));
		setMaximumSize(dim);
		setMinimumSize(dim);
		setPreferredSize(dim);
	}

	/**
	 * Starts the applet
	 */
	@Override
	public void start() {
		game.start();
	}

	/**
	 * Stops the applet
	 */
	@Override
	public void stop() {
		game.stop();
	}

	// --------------------------------------------------------- APPLICATION

	/**
	 * Main method to start the game as an application.
	 * 
	 * @param args
	 * if there's a double parameter, the game will be put into windowed mode with a scale of the inputed double
	 */
	public static void main(String[] args) {
		// java argument
		if (args.length != 0) {
			double argu = Double.parseDouble(args[0]);
			if (argu != -1) {
				inputScale = argu;
				windowed = true;
			}
		}
		// if none, ask user
		else {
			String input = (String) JOptionPane.showInputDialog(new JFrame(), "Choose a scale (leave blank for full screen):", "Game Start", JOptionPane.PLAIN_MESSAGE, null, null, null);
			if (!input.equals("")) {
				inputScale = Double.parseDouble(input);
				windowed = true;
			}
		}
		game.frame = frame;
		// windowed...
		if (GameLauncher.windowed) {
			game.frame.add(game);
			game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			game.frame.setUndecorated(true);
			game.frame.setTitle(game.data.GAME_TITLE);
			game.frame.setSize(new Dimension((int) (game.data.WIDTH * GameLauncher.inputScale), (int) (game.data.HEIGHT * GameLauncher.inputScale)));
			game.frame.setLocationRelativeTo(null);
			game.frame.setVisible(true);
		}
		// full screen!
		else {
			GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
			game.frame.add(game);
			game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			game.frame.setUndecorated(true);
			game.frame.setResizable(false);
			device.setFullScreenWindow(game.frame);
		}
		game.start();
	}
}
