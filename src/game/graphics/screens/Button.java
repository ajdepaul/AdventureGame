package game.graphics.screens;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import game.Game;
import game.GameLauncher;

/**
 * A simple button that can be used to activate things. (Must be integrated with a MouseHandler class)
 * 
 * @author Anthony DePaul
 */
public class Button {

	/** The game this button is in */
	private Game game;

	/** Possible states of the button */
	public enum Status {
		HOVERED, PRESSED, RELEASED
	}

	/** The picture of all the button states */
	private BufferedImage image;
	/** The image the button will display */
	public Image currentImage;

	/** If the button has been activated */
	public boolean activated = false;

	/** The size of the button */
	public int width, height;
	/** The center location of the button */
	public Point loc;

	/**
	 * A simple button that can be used to activate things. (Must be integrated with a MouseHandler class)
	 * 
	 * @param g
	 * the game this button is in
	 * @param img
	 * the picture of all the button states
	 * @param xp
	 * position of the button's center
	 * @param yp
	 * position of the button's center
	 */
	public Button(Game g, Image img, int x, int y) {
		game = g;
		loc = new Point(x, y);
		image = (BufferedImage) img;
		width = image.getWidth(null);
		height = image.getHeight(null) / 3;
		currentImage = image.getSubimage(0, 0, width, height);
	}

	/**
	 * A simple button that can be used to activate things. (Must be integrated with a MouseHandler class)
	 * 
	 * @param g
	 * the game this button is in
	 * @param img
	 * the picture of all the button states
	 * @param xp
	 * position of the button's center (% of screen)
	 * @param yp
	 * position of the button's center (% of screen)
	 */
	public Button(Game g, Image img, double xp, double yp) {
		game = g;
		int x = (int) (game.data.WIDTH * xp);
		int y = (int) (game.data.HEIGHT * yp);
		loc = new Point(x, y);
		image = (BufferedImage) img;
		width = image.getWidth(null);
		height = image.getHeight(null) / 3;
		currentImage = image.getSubimage(0, 0, width, height);
	}

	/**
	 * Draws the button
	 * 
	 * @param g
	 * The graphics object to be drawn on
	 */
	public void draw(Graphics2D g) {
		g.drawImage(currentImage, loc.x - width / 2, loc.y - height / 2, null);
	}

	/**
	 * Updates the currentImage and if it is activated depending mouse position and what the mouse is doing.
	 * 
	 * @param e
	 * the MouseEvent that tells where the mouse is
	 * @param s
	 * the status of the mouse (hovered/pressed/released)
	 */
	public void update(MouseEvent e, Status s) {
		int x, y;

		if (!GameLauncher.isApplet) {
			int xOffset = (int) (game.data.frameXOffset / game.data.scale);
			int yOffset = (int) (game.data.frameYOffset / game.data.scale);
			x = (int) (e.getX() / game.data.scale) - xOffset;
			y = (int) (e.getY() / game.data.scale) - yOffset;

		} else {
			x = (int) e.getX();
			y = (int) e.getY();
		}

		// normal
		currentImage = image.getSubimage(0, 0, width, height);
		// hover
		if (s == Status.HOVERED)
			if (x > loc.x - width / 2 && x < loc.x + width / 2)
				if (y > loc.y - height / 2 && y < loc.y + height / 2)
					currentImage = image.getSubimage(0, height, width, height);
		// pressed
		if (s == Status.PRESSED)
			if (x > loc.x - width / 2 && x < loc.x + width / 2)
				if (y > loc.y - height / 2 && y < loc.y + height / 2)
					currentImage = image.getSubimage(0, height * 2, width, height);
		// released
		if (s == Status.RELEASED)
			if (x > loc.x - width / 2 && x < loc.x + width / 2)
				if (y > loc.y - height / 2 && y < loc.y + height / 2) {
					currentImage = image.getSubimage(0, height, width, height);
					activated = true;
				} else
					activated = false;
	}
}
