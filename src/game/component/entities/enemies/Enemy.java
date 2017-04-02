package game.component.entities.enemies;

import game.Game;
import game.component.entities.Entity;
import game.enums.Direction;

/**
 * Foundation for an enemy.
 * 
 * @author Anthony DePaul
 */
public abstract class Enemy extends Entity {

	// --------------------------------------------------------- VARIABLE DECLARATIONS

	/** How close the player needs to be for the enemy to start moving towards him */
	protected int viewDistance = 250;
	/** How far the enemy if from the player */
	protected double distanceFromPlayer = Double.MAX_VALUE;

	// --------------------------------------------------------- CONSTRUCTORS

	/**
	 * Enemy object constructor. Used for inheritance.
	 * 
	 * @param game
	 * the game this enemy is in
	 * @param x
	 * the x position of the enemy
	 * @param y
	 * the y position of the enemy
	 */
	protected Enemy(Game g, int x, int y) {
		super(g, x, y);
		speed = 2;
	}

	// --------------------------------------------------------- UPDATE

	/**
	 * Updates status based off other information for the enemy.
	 */
	@Override
	public void update() {
		// pits & death //
		super.update();
		// movement values //
		double x = loc.x - game.player.loc.x;
		double y = loc.y - game.player.loc.y;
		double angle = Math.abs(Math.atan(y / x));
		double movex = Math.cos(angle) * speed;
		double movey = Math.sin(angle) * speed;

		if (isAlive) {
			distanceFromPlayer = Math.sqrt((loc.x - game.player.loc.x) * (loc.x - game.player.loc.x) + (loc.y - game.player.loc.y) * (loc.y - game.player.loc.y));
			// hit //
			if (distanceFromPlayer <= game.player.reach + (size / 2) + (game.player.size / 2) && game.data.updateTick - startHitTick >= hitDelay && game.player.attackDir != null) {
				switch (game.player.attackDir) {
				case NORTH:
					if (loc.y <= game.player.loc.y) {
						hp -= game.player.damage;
						startHitTick = game.data.updateTick;
					}
					break;
				case EAST:
					if (loc.x >= game.player.loc.x) {
						hp -= game.player.damage;
						startHitTick = game.data.updateTick;
					}
					break;
				case SOUTH:
					if (loc.y >= game.player.loc.y) {
						hp -= game.player.damage;
						startHitTick = game.data.updateTick;
					}
					break;
				case WEST:
					if (loc.x <= game.player.loc.x) {
						hp -= game.player.damage;
						startHitTick = game.data.updateTick;
					}
					break;
				}
			}
			// knock back //
			if (game.data.updateTick - startHitTick <= hitDelay) {
				if (loc.x - game.player.loc.x <= 0 && loc.y - game.player.loc.y <= 0) // down right
					move(-movex, -movey);
				else if (loc.x - game.player.loc.x >= 0 && loc.y - game.player.loc.y <= 0) // down left
					move(movex, -movey);
				else if (loc.x - game.player.loc.x <= 0 && loc.y - game.player.loc.y >= 0) // up right
					move(-movex, movey);
				else if (loc.x - game.player.loc.x >= 0 && loc.y - game.player.loc.y >= 0) // up left
					move(movex, movey);
			}
			// move towards player //
			else if (distanceFromPlayer <= viewDistance && distanceFromPlayer >= size / 2 && game.player.isAlive) {
				if (loc.x - game.player.loc.x <= 0 && loc.y - game.player.loc.y <= 0) { // down right
					move(movex, movey);
					if (movex > movey)
						direction = Direction.EAST;
					else
						direction = Direction.SOUTH;
				} else if (loc.x - game.player.loc.x >= 0 && loc.y - game.player.loc.y <= 0) { // down left
					move(-movex, movey);
					if (movex > movey)
						direction = Direction.WEST;
					else
						direction = Direction.SOUTH;
				} else if (loc.x - game.player.loc.x <= 0 && loc.y - game.player.loc.y >= 0) { // up right
					move(movex, -movey);
					if (movex > movey)
						direction = Direction.EAST;
					else
						direction = Direction.NORTH;
				} else if (loc.x - game.player.loc.x >= 0 && loc.y - game.player.loc.y >= 0) { // up left
					move(-movex, -movey);
					if (movex > movey)
						direction = Direction.WEST;
					else
						direction = Direction.NORTH;
				}
			}
		}
	}
}
