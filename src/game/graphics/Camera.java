package game.graphics;

import game.Game;

/**
 * Finds values to offset the zone relative to the screen to make it look nice.
 * 
 * @author Anthony DePaul
 */
public class Camera {

	/** The game this camera is in */
	private Game game;

	/**
	 * Finds values to offset the zone relative to the screen to make it look nice.
	 * 
	 * @param g
	 * the game this camera is in
	 */
	public Camera(Game g) {
		game = g;
	}

	/**
	 * Offsets the zone if it is not as wide as the screen and centers it.
	 * 
	 * @return the distance from the left side of the screen to left edge of the zone
	 */
	public int smallXOffset() {
		if (game.data.currentZone.layout[0].length < game.data.TILES_WIDE)
			return (game.data.WIDTH - game.data.currentZone.WIDTH) / 2;
		return 0;
	}

	/**
	 * Offsets the zone if it is not as high as the screen and centers it.
	 * 
	 * @return the distance from the top of the screen to top edge of the zone
	 */
	public int smallYOffset() {
		if (game.data.currentZone.layout.length < game.data.TILES_HIGH)
			return (game.data.HEIGHT - game.data.currentZone.HEIGHT) / 2;
		return 0;
	}

	/**
	 * Offsets the zone if it is wider than the screen.
	 * 
	 * @return the distance from the left side of the screen to left edge of the zone
	 */
	public int bigXOffset() {
		if (game.data.currentZone.layout[0].length > game.data.TILES_WIDE)
			if (game.player.loc.x > game.data.WIDTH / 2) // away from left wall
				if (game.player.loc.x < game.data.currentZone.WIDTH - game.data.WIDTH / 2) // away from right wall
					return (int) (game.player.loc.x - (game.data.WIDTH / 2)); // middle
				else
					return game.data.currentZone.WIDTH - game.data.WIDTH; // against right wall
		return 0; // small or right size or against left wall
	}

	/**
	 * Offsets the zone if it is higher than the screen.
	 * 
	 * @return the distance from the top of the screen to top edge of the zone
	 */
	public int bigYOffset() {
		if (game.data.currentZone.layout.length > game.data.TILES_HIGH)
			if (game.player.loc.y > game.data.HEIGHT / 2) // away from top wall
				if (game.player.loc.y < game.data.currentZone.HEIGHT - game.data.HEIGHT / 2) // away from bottom wall
					return (int) (game.player.loc.y - (game.data.HEIGHT / 2)); // middle
				else
					return game.data.currentZone.HEIGHT - game.data.HEIGHT; // against bottom wall
		return 0; // small or right size or against top wall
	}
}
