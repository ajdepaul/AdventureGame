package game.graphics.screens;

import game.Game;
import game.enums.GameState;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

/**
 * What is displayed when the player wins.
 * 
 * 
 * @author Anthony DePaul
 */
public class ControlsMenu {

	/** The game the win screen is in */
	private Game game;
	/** Button to reset the game and return to the main menu */
	public Button mainmenu;

	/**
	 * What is displayed when the player wins.
	 * 
	 * @param g
	 * the game the win screen is in
	 */
	public ControlsMenu(Game g) {
		game = g;
		mainmenu = new Button(game, game.images.controlsMainMenuButton, 0.5, 0.9);
	}

	/**
	 * Runs update methods on all the buttons in the pause menu.
	 * 
	 * @param e
	 * the MouseEvent that says where the mouse is
	 * @param s
	 * the status of the mouse (hovered?, pressed?, released?)
	 */
	public void updateButtons(MouseEvent e, Button.Status s) {
		mainmenu.update(e, s);
	}

	/**
	 * Draws the pause menu
	 * 
	 * @param g
	 * the graphics object to draw on
	 */
	public void render(Graphics2D g) {
		g.drawImage(game.images.controlsBackground, 0, 0, null);
		g.drawImage(mainmenu.currentImage, mainmenu.loc.x - mainmenu.width / 2, mainmenu.loc.y - mainmenu.height / 2, null);
	}

	/**
	 * Makes a pause menu buttons do what they do when activated.
	 */
	public void update() {
		if (mainmenu.activated) {
			game.data.status = GameState.MAIN_MENU;
			mainmenu.activated = false;
		}
	}
}