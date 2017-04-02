package game.component.entities.environment;

import game.Game;
import game.component.entities.Entity;
import game.graphics.images.ImageSheet;

/**
 * An entity that can be picked up by the player.
 * 
 * @author Anthony DePaul
 */
public class Destructible extends Entity {

	/**
	 * Entity object constructor. Used for inheritance.
	 * 
	 * @param g
	 * The game this Entity is in
	 * @param imgs
	 * Image of all the static entity positions
	 * @param x
	 * x location of the Entity
	 * @param y
	 * y location of the Entity
	 */
	public Destructible(Game g, ImageSheet imgs, int x, int y, Entity... ed) {
		super(g, imgs, x, y);
		entityDrops = ed;
		maxhp = 50;
		hp = maxhp;
	}

	/**
	 * 
	 */
	@Override
	public void update() {
		super.update();
		double distanceFromPlayer = Math.sqrt((loc.x - game.player.loc.x) * (loc.x - game.player.loc.x) + (loc.y - game.player.loc.y) * (loc.y - game.player.loc.y));

		if (isAlive)
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
	}

	/**
	 * 
	 */
	@Override
	public void updateImage() {
		if (isAlive)
			currentImage = images.grabImage(0, 0, 1, 1);
		else
			currentImage = images.grabImage(0, 1, 1, 1);
		super.updateImage();
	}
}
