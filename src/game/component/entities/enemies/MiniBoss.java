package game.component.entities.enemies;

import game.Game;
import game.component.entities.Entity;
import game.component.entities.environment.Item;
import game.enums.Direction;
import game.graphics.images.CustomAnimation;

import java.awt.geom.Point2D;
import java.util.Random;

/**
 * A simple boss.
 * 
 * @author Anthony DePaul
 */
public class MiniBoss extends Enemy {

	// --------------------------------------------------------- VARIABLE DECLARATIONS

	/** The animation for walking east */
	private CustomAnimation walkE;
	/** The animation for walking south */
	private CustomAnimation walkS;
	/** The animation for walking west */
	private CustomAnimation walkW;
	/** The animation for walking north */
	private CustomAnimation walkN;
	/** If the boss is chasing the player */
	private boolean chasing = false;

	// --------------------------------------------------------- CONSTRUCTORS

	/**
	 * A simple boss.
	 * 
	 * @param game
	 * the game this enemy is in
	 * @param x
	 * the x position of the boss
	 * @param y
	 * the y position of the boss
	 */
	public MiniBoss(Game g, int x, int y) {
		super(g, x, y);
		speed = .75;
		maxhp = 500;
		size = 60;
		damage = 20;
		hp = maxhp;
		images = game.images.miniBoss;
		walkN = game.images.miniBossWalkN;
		walkE = game.images.miniBossWalkE;
		walkS = game.images.miniBossWalkS;
		walkW = game.images.miniBossWalkW;
		updateImage();
		entityDrops = new Entity[]
		{ new Item(game, 0, 0, Item.KEY_I) };
	}

	// --------------------------------------------------------- UPDATE

	/**
	 * Updates the image of the blob depending its situation.
	 */
	@Override
	public void updateImage() {

		// RIP
		if (!isAlive) {
			switch (direction) {
			case SOUTH:
				currentImage = images.grabImage(0, 3, 3, 3);
				break;
			case EAST:
				currentImage = images.grabImage(3, 3, 3, 3);
				break;
			case NORTH:
				currentImage = images.grabImage(6, 3, 3, 3);
				break;
			case WEST:
				currentImage = images.grabImage(9, 3, 3, 3);
				break;
			}
			return;
		}

		// moving or standing
		switch (direction) {
		case SOUTH:
			currentImage = images.grabImage(0, 0, 3, 3);
			if (chasing && distanceFromPlayer >= size / 2 && game.player.isAlive) {
				currentImage = walkS.getImage();
			}
			break;
		case EAST:
			currentImage = images.grabImage(3, 0, 3, 3);
			if (chasing && distanceFromPlayer >= size / 2 && game.player.isAlive) {
				currentImage = walkE.getImage();
			}
			break;
		case NORTH:
			currentImage = images.grabImage(6, 0, 3, 3);
			if (chasing && distanceFromPlayer >= size / 2 && game.player.isAlive) {
				currentImage = walkN.getImage();
			}
			break;
		case WEST:
			currentImage = images.grabImage(9, 0, 3, 3);
			if (chasing && distanceFromPlayer >= size / 2 && game.player.isAlive) {
				currentImage = walkW.getImage();
			}
			break;
		}

		// damaged boss
		super.updateImage();
	}

	/**
	 * Updates data based off other information for the boss.
	 */
	@Override
	public void update() {
		// pits
		// if (pitCollision() && status == EntityState.GROUND)
		// isAlive = false;
		// death
		if (hp <= 0)
			isAlive = false;
		// entity drop
		if (!isAlive && entityDrops != null && !droppedEntity) {
			droppedEntity = true;
			int rand = new Random().nextInt(entityDrops.length);
			Entity drop = entityDrops[rand];
			if (drop != null) {
				drop.loc.setLocation(new Point2D.Double(loc.x, loc.y));
				game.data.currentZone.entities.add(drop);
			}
		}
		// movement values //
		distanceFromPlayer = Math.sqrt((loc.x - game.player.loc.x) * (loc.x - game.player.loc.x) + (loc.y - game.player.loc.y) * (loc.y - game.player.loc.y));
		double x = loc.x - game.player.loc.x;
		double y = loc.y - game.player.loc.y;
		double angle = Math.abs(Math.atan(y / x));
		double movex = Math.cos(angle) * speed;
		double movey = Math.sin(angle) * speed;

		if (!chasing && game.player.isAlive && distanceFromPlayer <= viewDistance)
			chasing = true;
		if (!game.player.isAlive && chasing)
			chasing = false;

		if (isAlive) {
			// hit //
			if (distanceFromPlayer <= game.player.reach + size / 2 && game.data.updateTick - startHitTick >= hitDelay && game.player.attackDir != null) {
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
			// movement //
			if (chasing && distanceFromPlayer >= size / 2 + game.player.size / 2 && game.player.isAlive) {
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