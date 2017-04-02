package game.graphics.screens;

import game.Game;
import game.component.entities.environment.Item;
import game.enums.GameState;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;

/**
 * The pause menu of the game
 * 
 * @author Anthony DePaul
 */
public class PauseMenu {

	/** The game the pause menu is in */
	private Game game;
	/** Button to resume the game */
	public Button resume;
	/** Button to return to the main menu */
	public Button mainmenu;
	/** Graphic to select items */
	public Image selector;
	/** Scale of the items */
	private final int ITEM_SCALE = 3;
	/** Scale of the money */
	private final double TEXT_SCALE = 2.5;
	/** Space between items */
	private final int SPACE_BETWEEN = 6;

	/**
	 * The pause menu of the game
	 * 
	 * @param g
	 * the game the pause menu is in
	 */
	public PauseMenu(Game g) {
		game = g;
		resume = new Button(game, game.images.pauseResumeButton, 0.3, .9);
		mainmenu = new Button(game, game.images.pauseMainMenuButton, 0.7, 0.9);
		selector = game.images.pauseSelector;
	}

	/**
	 * Runs update methods on all the buttons in the pause menu.
	 * 
	 * @param e
	 * the MouseEvent that says where the mouse is
	 * @param s
	 * the status of the mouse (hovered/pressed/released)
	 */
	public void updateButtons(MouseEvent e, Button.Status s) {
		resume.update(e, s);
		mainmenu.update(e, s);
	}

	/**
	 * Draws the pause menu
	 * 
	 * @param g
	 * the graphics object to draw on
	 */
	public void render(Graphics2D g) {
		g.drawImage(game.images.pauseBackground, 0, 0, null);
		g.drawImage(resume.currentImage, resume.loc.x - resume.width / 2, resume.loc.y - resume.height / 2, null);
		g.drawImage(mainmenu.currentImage, mainmenu.loc.x - mainmenu.width / 2, mainmenu.loc.y - mainmenu.height / 2, null);
		drawInv(g);
		drawText(g);
	}

	/**
	 * Draws the inventory onto the graphics object
	 * 
	 * @param g
	 * the graphics object to draw the inventory on
	 */
	private void drawInv(Graphics2D g) {
		Item[][] items = game.player.inventory.items;
		int ITEM_SIZE = game.data.TILE_RES * ITEM_SCALE;
		int WIDTH = (ITEM_SIZE * items[0].length) + (SPACE_BETWEEN * (items[0].length - 1));
		int HEIGHT = (ITEM_SIZE * items.length) + (SPACE_BETWEEN * (items.length - 1));
		int xOffset = (game.data.WIDTH - WIDTH) / 2;
		int yOffset = (game.data.HEIGHT - HEIGHT) / 2;

		for (int row = 0; row < items.length; row++)
			for (int col = 0; col < items[0].length; col++) {
				int x = xOffset + (col * ITEM_SIZE) + (col * SPACE_BETWEEN);
				int y = yOffset + (row * ITEM_SIZE) + (row * SPACE_BETWEEN);
				if (items[row][col] != null)
					g.drawImage(items[row][col].currentImage, x, y, ITEM_SIZE, ITEM_SIZE, null);
			}

		Point selectorPos = game.player.inventory.selector;
		int x = xOffset + (selectorPos.x * ITEM_SIZE) + (selectorPos.x * SPACE_BETWEEN);
		int y = yOffset + (selectorPos.y * ITEM_SIZE) + (selectorPos.y * SPACE_BETWEEN);
		g.drawImage(selector, x, y, ITEM_SIZE, ITEM_SIZE, null);
	}

	/**
	 * Draws the money and lives onto the graphics object
	 * 
	 * @param g
	 * the graphics object to draw the lives and money on
	 */
	private void drawText(Graphics2D g) {
		int fontSize = (int) (game.data.TILE_RES * TEXT_SCALE);
		Font font = new Font("Monospaced", Font.BOLD, fontSize);
		String money = game.player.money + "";
		boolean rightSize = false;
		FontMetrics metrics = g.getFontMetrics(font);

		// lives //
		g.setFont(font);
		int xOffset = -(metrics.stringWidth(game.player.lives + "") / 2);
		int yOffset = metrics.getHeight() / 4;
		g.drawString(game.player.lives + "", (int) (game.data.WIDTH * .9) + xOffset, (int) (game.data.HEIGHT * .5) + yOffset);

		// money //
		do {
			metrics = g.getFontMetrics(font);
			if (metrics.stringWidth(money) <= game.data.TILE_RES * TEXT_SCALE)
				rightSize = true;
			else {
				fontSize--;
				font = new Font("Monospaced", Font.BOLD, fontSize);
			}
		} while (!rightSize);

		xOffset = -(metrics.stringWidth(money) / 2);
		yOffset = metrics.getHeight() / 4;
		g.setFont(font);
		g.drawString(game.player.money + "", (int) (game.data.WIDTH * .1) + xOffset, (int) (game.data.HEIGHT * .5) + yOffset);

	}

	/**
	 * Makes a pause menu buttons do what they do when activated.
	 */
	public void update() {
		// resume
		if (resume.activated) {
			game.data.status = GameState.PLAYING;
			resume.activated = false;
		}
		// quit
		if (mainmenu.activated) {
			game.data.status = GameState.MAIN_MENU;
			mainmenu.activated = false;
		}
	}
}
