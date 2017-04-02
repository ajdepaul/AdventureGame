package game.component.entities.friendlies;

import game.Game;
import game.component.entities.Entity;
import game.enums.Direction;
import game.enums.GameState;
import game.graphics.images.ImageSheet;
import game.graphics.screens.Scene;

/**
 * An entity the player may or may not talk to.
 * 
 * @author Anthony DePaul
 */
public class Friendly extends Entity {

	// --------------------------------------------------------- CONSTRUCTORS

	/** If the friendly scene starts without a player interacting */
	public boolean autoScene = false;
	/** The scene the friendly creates. Null if none */
	public Scene scene = null;
	/** How close the player needs to be to activate a scene */
	public int distanceForScene = 25;

	// --------------------------------------------------------- CONSTRUCTORS

	/**
	 * A friendly the player can talk to.
	 * 
	 * @param game
	 * the game the friendly is in
	 * @param imgsheet
	 * static images to be used by the friendly
	 * @param x
	 * the x position of the friendly
	 * @param y
	 * the y position of the friendly
	 * @param as
	 * if the scene activates automatically
	 * @param sc
	 * the scene the friendly uses
	 */
	public Friendly(Game g, ImageSheet imgsheet, int x, int y, boolean as, Scene sc, int dir) {
		game = g;
		loc.setLocation(x, y);
		images = imgsheet;
		currentImage = images.grabImage(2, 0, 1, 2);
		autoScene = as;
		scene = sc;
		updateImage();
		switch (dir) {
		case 0:
			direction = Direction.NORTH;
			break;
		case 1:
			direction = Direction.EAST;
			break;
		case 2:
			direction = Direction.SOUTH;
			break;
		case 3:
			direction = Direction.WEST;
			break;
		}
	}

	// --------------------------------------------------------- IMAGE

	/**
	 * Creates the image for the player based off its current situation.
	 */
	@Override
	public void updateImage() {
		super.updateImage();
		switch (direction) {
		case NORTH:
			currentImage = images.grabImage(0, 0, 1, 2);
			break;
		case EAST:
			currentImage = images.grabImage(1, 0, 1, 2);
			break;
		case SOUTH:
			currentImage = images.grabImage(2, 0, 1, 2);
			break;
		case WEST:
			currentImage = images.grabImage(3, 0, 1, 2);
			break;
		}
	}

	/**
	 * Updates friendly data based off other information.
	 */
	@Override
	public void update() {
		// pits and death //
		super.update();
		// scenes //
		if (scene != null && game.data.status == GameState.PLAYING) {
			double distanceFromPlayer = Math.sqrt((loc.x - game.player.loc.x) * (loc.x - game.player.loc.x) + (loc.y - game.player.loc.y) * (loc.y - game.player.loc.y));
			if (distanceFromPlayer <= distanceForScene) {
				// auto scene
				if (autoScene) {
					game.data.currentScene = scene;
					game.data.status = GameState.SCENE;
				}
				// activate scene
				else if (game.data.sceneActivate == true) {

					double xd = loc.x - game.player.loc.x;
					double yd = loc.y - game.player.loc.y;
					double angle = Math.abs(Math.atan(yd / xd));
					double x = Math.cos(angle);
					double y = Math.sin(angle);

					if (loc.x - game.player.loc.x <= 0 && loc.y - game.player.loc.y <= 0) // down right
						if (x > y)
							direction = Direction.EAST;
						else
							direction = Direction.SOUTH;
					else if (loc.x - game.player.loc.x >= 0 && loc.y - game.player.loc.y <= 0) // down left
						if (x > y)
							direction = Direction.WEST;
						else
							direction = Direction.SOUTH;
					else if (loc.x - game.player.loc.x <= 0 && loc.y - game.player.loc.y >= 0) // up right
						if (x > y)
							direction = Direction.EAST;
						else
							direction = Direction.NORTH;
					else if (loc.x - game.player.loc.x >= 0 && loc.y - game.player.loc.y >= 0) // up left
						if (x > y)
							direction = Direction.WEST;
						else
							direction = Direction.NORTH;

					game.data.currentScene = scene;
					game.data.status = GameState.SCENE;
				}
			}
		}
	}
}
