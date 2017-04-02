package game.graphics.screens;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import game.Game;
import game.enums.GameState;

/**
 * The main menu of the game
 * 
 * @author Anthony DePaul
 */
public class MainMenu {

	/** The game the main menu is in */
	private Game game;
	/** Button to start the game */
	public Button play;
	/** Button to quit the game */
	public Button quit;
	/** Button to get to controls menu */
	public Button controls;

	/**
	 * The main menu of the game
	 * 
	 * @param g
	 * the game the main menu is in
	 */
	public MainMenu(Game g) {
		game = g;
		play = new Button(game, game.images.mainPlayButton, 0.3, 0.9);
		quit = new Button(game, game.images.mainQuitButton, 0.7, 0.9);
		controls = new Button(game, game.images.mainControlsButton, 0.5, 0.9);
	}

	/**
	 * Runs update methods on all the buttons in the main menu.
	 * 
	 * @param e
	 * the MouseEvent that says where the mouse is
	 * @param s
	 * the status of the mouse (hovered/pressed/released)
	 */
	public void updateButtons(MouseEvent e, Button.Status s) {
		play.update(e, s);
		quit.update(e, s);
		controls.update(e, s);
	}

	/**
	 * Draws the main menu
	 * 
	 * @param g
	 * the graphics object to draw on
	 */
	public void render(Graphics2D g) {
		g.drawImage(game.images.mainBackground, 0, 0, null);
		play.draw(g);
		quit.draw(g);
		controls.draw(g);
	}

	/**
	 * Makes a main menu buttons do what they do when activated.
	 */
	public void update() {
		// play
		if (play.activated) {
			game.data.status = GameState.PLAYING;
			play.activated = false;
		}
		// quit
		if (quit.activated)
			System.exit(0);
		if (controls.activated) {
			game.data.status = GameState.CONTROLS_MENU;
			controls.activated = false;
		}
	}
}
