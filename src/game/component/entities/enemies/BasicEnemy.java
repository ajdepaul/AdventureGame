package game.component.entities.enemies;

import game.Game;
import game.component.entities.Entity;
import game.component.entities.environment.Money;
import game.graphics.images.CustomAnimation;

/**
 * Basic type of enemy that simply moves around.
 * 
 * @author Anthony DePaul
 */
public class BasicEnemy extends Enemy {

	// --------------------------------------------------------- VARIABLE DECLARATIONS

	/** The animation for walking east */
	private CustomAnimation walkE;
	/** The animation for walking south */
	private CustomAnimation walkS;
	/** The animation for walking west */
	private CustomAnimation walkW;
	/** The animation for walking north */
	private CustomAnimation walkN;

	// --------------------------------------------------------- CONSTRUCTORS

	/**
	 * Basic type of enemy that simply moves around.
	 * 
	 * @param game
	 * the game this enemy is in
	 * @param x
	 * the x position of the enemy
	 * @param y
	 * the y position of the enemy
	 */
	public BasicEnemy(Game g, int x, int y) {
		super(g, x, y);
		images = game.images.basicenemy;
		walkN = game.images.basicEnemyWalkN;
		walkE = game.images.basicEnemyWalkE;
		walkS = game.images.basicEnemyWalkS;
		walkW = game.images.basicEnemyWalkW;
		updateImage();
		entityDrops = new Entity[]
		{ new Money(game, 10), new Money(game, 5), new Money(game, 5), new Money(game, 1), new Money(game, 1), new Money(game, 1), null, null, null, null };
	}

	// --------------------------------------------------------- UPDATE

	/**
	 * Updates the image of the enemy depending on its current situation.
	 */
	@Override
	public void updateImage() {
		// RIP //
		if (!isAlive) {
			switch (direction) {
			case SOUTH:
				currentImage = images.grabImage(0, 2, 1, 2);
				break;
			case EAST:
				currentImage = images.grabImage(1, 2, 1, 2);
				break;
			case NORTH:
				currentImage = images.grabImage(2, 2, 1, 2);
				break;
			case WEST:
				currentImage = images.grabImage(3, 2, 1, 2);
				break;
			}
			return;
		}
		// moving or standing //
		switch (direction) {
		case SOUTH:
			currentImage = images.grabImage(0, 0, 1, 2);
			if (distanceFromPlayer <= viewDistance && distanceFromPlayer >= size / 2 && game.player.isAlive) {
				currentImage = walkS.getImage();
			}
			break;
		case EAST:
			currentImage = images.grabImage(1, 0, 1, 2);
			if (distanceFromPlayer <= viewDistance && distanceFromPlayer >= size / 2 && game.player.isAlive) {
				currentImage = walkE.getImage();
			}
			break;
		case NORTH:
			currentImage = images.grabImage(2, 0, 1, 2);
			if (distanceFromPlayer <= viewDistance && distanceFromPlayer >= size / 2 && game.player.isAlive) {
				currentImage = walkN.getImage();
			}
			break;
		case WEST:
			currentImage = images.grabImage(3, 0, 1, 2);
			if (distanceFromPlayer <= viewDistance && distanceFromPlayer >= size / 2 && game.player.isAlive) {
				currentImage = walkW.getImage();
			}
			break;
		}
		// damaged enemy //
		super.updateImage();
	}
}
