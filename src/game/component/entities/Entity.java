package game.component.entities;

import game.Game;
import game.component.Tile;
import game.component.entities.enemies.Enemy;
import game.component.entities.environment.Destructible;
import game.component.entities.friendlies.Friendly;
import game.enums.Direction;
import game.enums.EntityState;
import game.graphics.images.ImageSheet;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.Random;

/**
 * Basic entity. Has an image and can be placed in a zone.
 * 
 * @author Anthony DePaul
 */
public abstract class Entity implements Comparable<Entity> {

	// --------------------------------------------------------- VARIABLE DECLARATIONS

	/** The game this Entity is in */
	protected Game game;

	/** If the entity is alive */
	public boolean isAlive = true;
	/** The location of the entity in the zone */
	public Point2D.Double loc = new Double();
	/** Current state of the entity */
	public EntityState status = EntityState.GROUND;

	/** The initial tick when hit */
	protected float startHitTick = -100;
	/** How many ticks before the entity can be hit again */
	protected int hitDelay = 10;
	/** The max health of the entity */
	protected int maxhp = 100;
	/** The current health of the entity */
	public int hp = maxhp;
	/** The damage the entity does */
	public int damage = 10;

	/** The direction of the entity */
	public Direction direction = Direction.SOUTH;
	/** Number of times the entity moves one pixel per tick while moving */
	public double speed = 3;
	/** Size of entity (diameter) */
	public int size = 20;

	/** If entity is moving north */
	public boolean movingNorth = false;
	/** If entity is moving east */
	public boolean movingEast = false;
	/** If entity is moving south */
	public boolean movingSouth = false;
	/** If entity is moving west */
	public boolean movingWest = false;

	/** Image to be displayed for the entity */
	public Image currentImage;
	/** Image of all the static entity positions */
	protected ImageSheet images;

	/** The tile location of the entity on the zone layout array */
	public Point tilePos;

	/** The entity this entity will drop on death */
	public Entity[] entityDrops = null;
	/** If the entityDrop has been dropped yet */
	protected boolean droppedEntity = false;

	// --------------------------------------------------------- CONSTRUCTORS

	/**
	 * Entity object constructor. Used for inheritance.
	 */
	protected Entity() {
	}

	/**
	 * Entity object constructor. Used for inheritance.
	 * 
	 * @param g
	 * The game this Entity is in
	 * @param x
	 * x location of the Entity
	 * @param y
	 * y location of the Entity
	 */
	protected Entity(Game g, double x, double y) {
		game = g;
		hp = maxhp;
		loc.setLocation(x, y);
	}

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
	protected Entity(Game g, ImageSheet imgs, int x, int y) {
		game = g;
		images = imgs;
		hp = maxhp;
		loc.setLocation(x, y);
	}

	// --------------------------------------------------------- IMAGE

	/**
	 * Creates the image for the entity based off its current situation.
	 */
	public void updateImage() {
		if (game.data.updateTick - startHitTick <= hitDelay && isAlive)
			currentImage = Entity.damageImage(currentImage);
	}

	/**
	 * Gets the location of the entity relative to the screen.
	 * 
	 * @return
	 * pixels from the left side of the screen
	 */
	public int locXToScreen() {
		int offset = -(currentImage.getWidth(null) / 2);
		return (int) (loc.x - game.camera.bigXOffset() + offset);
	}

	/**
	 * Gets the location of the entity relative to the screen.
	 * 
	 * @return
	 * pixels from the top side of the screen
	 */
	public int locYToScreen() {
		int offset = -(currentImage.getHeight(null) - (game.data.TILE_RES / 2));
		return (int) (loc.y - game.camera.bigYOffset() + offset);
	}

	/**
	 * Compares this entity to another by y position.
	 * 
	 * @param e
	 * the Entity being compared
	 */
	@Override
	public int compareTo(Entity e) {
		// if (e instanceof Item)
		// return 1; // on top
		if (loc.y < e.loc.y)
			return -1; // beneath
		if (loc.y > e.loc.y)
			return 1; // on top
		return 0; // neutral
	}

	/**
	 * Makes image red maintaining alpha values. (did not write myself)
	 * 
	 * @param image
	 * any image
	 * @return
	 * the red image
	 */
	public static BufferedImage damageImage(Image img) {

		int width = img.getWidth(null);
		int height = img.getHeight(null);
		BufferedImage newImg = Game.copyImage(img);

		WritableRaster raster = newImg.getRaster();

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int[] pixel = raster.getPixel(x, y, (int[]) null);
				pixel[0] = 175;
				pixel[1] = 0;
				pixel[2] = 0;
				raster.setPixel(x, y, pixel);
			}
		}
		return newImg;
	}

	/**
	 * Draws the health the current entity has underneath it
	 * 
	 * @param g
	 * The graphics object to draw on
	 */
	public void drawHp(Graphics g) {
		if (isAlive) {
			int xOffset = -(size / 2);
			int x = (int) (loc.x - game.camera.bigXOffset() + xOffset);
			int yOffset = game.data.TILE_RES / 3;
			int y = (int) (loc.y - game.camera.bigYOffset() + yOffset);

			Color original = g.getColor();
			if (hp < maxhp && hp > 0) {
				g.setColor(Color.BLACK);
				g.fillRect(x, y, size, 2);
				g.setColor(Color.RED);
				double percenthp = (double) hp / maxhp;
				g.fillRect(x, y, (int) (size * percenthp), 2);
			}
			g.setColor(original);
		}
	}

	// --------------------------------------------------------- UPDATE

	/**
	 * Handles pits and death. (To be used in inheritance for updates)
	 */
	public void update() {
		if (pitCollision() && status == EntityState.GROUND)
			isAlive = false;
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
	}

	// --------------------------------------------------------- COLLISION

	/**
	 * Returns true if entity tries to walk into tangible tiles.
	 * 
	 * @param direction
	 * the direction the entity is facing
	 * @return
	 * if the player is colliding with an object in the specified direction
	 */
	protected boolean collision(Direction direction) {

		Point NE = new Point((int) (loc.x + size / 2), (int) (loc.y - size / 2));
		Point SE = new Point((int) (loc.x + size / 2), (int) (loc.y + size / 2));
		Point SW = new Point((int) (loc.x - size / 2), (int) (loc.y + size / 2));
		Point NW = new Point((int) (loc.x - size / 2), (int) (loc.y - size / 2));

		boolean knockedBack = false;
		if (game.data.updateTick - startHitTick <= hitDelay)
			knockedBack = true;

		switch (direction) {
		case NORTH:
			if (game.data.currentZone.layout[(NE.y - 1) / game.data.TILE_RES][NE.x / game.data.TILE_RES].isCollidable // NE
					|| game.data.currentZone.layout[(NW.y - 1) / game.data.TILE_RES][NW.x / game.data.TILE_RES].isCollidable) // NW
				return true;

			for (Entity e : game.data.currentZone.entities) {
				boolean otherKnockedBack = false;
				if (game.data.updateTick - e.startHitTick <= e.hitDelay && (e instanceof Friendly || e instanceof Enemy || e instanceof Player))
					otherKnockedBack = true;

				if (loc.y > e.loc.y && e.isAlive && (e instanceof Friendly || e instanceof Enemy || e instanceof Player || e instanceof Destructible) && !knockedBack && !otherKnockedBack) {
					double distanceFromEntity = Math.sqrt((loc.x - e.loc.x) * (loc.x - e.loc.x) + (loc.y - e.loc.y) * (loc.y - e.loc.y));
					if (distanceFromEntity <= (size / 2) + (e.size / 2))
						return true;
				}
			}
			break;
		case EAST:
			// wall
			if (game.data.currentZone.layout[NE.y / game.data.TILE_RES][(NE.x + 1) / game.data.TILE_RES].isCollidable // NE
					|| game.data.currentZone.layout[SE.y / game.data.TILE_RES][(SE.x + 1) / game.data.TILE_RES].isCollidable) // SE
				return true;
			// entities
			for (Entity e : game.data.currentZone.entities) {
				boolean otherKnockedBack = false;
				if (game.data.updateTick - e.startHitTick <= e.hitDelay && (e instanceof Friendly || e instanceof Enemy || e instanceof Player))
					otherKnockedBack = true;

				if (loc.x < e.loc.x && e.isAlive && (e instanceof Friendly || e instanceof Enemy || e instanceof Player || e instanceof Destructible) && !knockedBack && !otherKnockedBack) {
					double distanceFromEntity = Math.sqrt((loc.x - e.loc.x) * (loc.x - e.loc.x) + (loc.y - e.loc.y) * (loc.y - e.loc.y));
					if (distanceFromEntity <= (size / 2) + (e.size / 2))
						return true;
				}
			}
			break;
		case SOUTH:
			// wall
			if (game.data.currentZone.layout[(SE.y + 1) / game.data.TILE_RES][SE.x / game.data.TILE_RES].isCollidable // SE
					|| game.data.currentZone.layout[(SW.y + 1) / game.data.TILE_RES][SW.x / game.data.TILE_RES].isCollidable) // SW
				return true;
			// entity
			for (Entity e : game.data.currentZone.entities) {
				boolean otherKnockedBack = false;
				if (game.data.updateTick - e.startHitTick <= e.hitDelay && (e instanceof Friendly || e instanceof Enemy || e instanceof Player))
					otherKnockedBack = true;

				if (loc.y < e.loc.y && e.isAlive && (e instanceof Friendly || e instanceof Enemy || e instanceof Player || e instanceof Destructible) && !knockedBack && !otherKnockedBack) {
					double distanceFromEntity = Math.sqrt((loc.x - e.loc.x) * (loc.x - e.loc.x) + (loc.y - e.loc.y) * (loc.y - e.loc.y));
					if (distanceFromEntity <= (size / 2) + (e.size / 2))
						return true;
				}
			}
			break;
		case WEST:
			// wall
			if (game.data.currentZone.layout[NW.y / game.data.TILE_RES][(NW.x - 1) / game.data.TILE_RES].isCollidable // NW
					|| game.data.currentZone.layout[SW.y / game.data.TILE_RES][(SW.x - 1) / game.data.TILE_RES].isCollidable) // SW
				return true;
			// entity
			for (Entity e : game.data.currentZone.entities) {
				boolean otherKnockedBack = false;
				if (game.data.updateTick - e.startHitTick <= e.hitDelay && (e instanceof Friendly || e instanceof Enemy || e instanceof Player))
					otherKnockedBack = true;

				if (loc.x > e.loc.x && e.isAlive && (e instanceof Friendly || e instanceof Enemy || e instanceof Player || e instanceof Destructible) && !knockedBack && !otherKnockedBack) {
					double distanceFromEntity = Math.sqrt((loc.x - e.loc.x) * (loc.x - e.loc.x) + (loc.y - e.loc.y) * (loc.y - e.loc.y));
					if (distanceFromEntity <= (size / 2) + (e.size / 2))
						return true;
				}
			}
			break;
		}
		return false;
	}

	/**
	 * Returns true if entity is over a pit.
	 * 
	 * @return
	 * if the entity is over a pit
	 */
	protected boolean pitCollision() {
		tilePos = new Point((int) (loc.x / game.data.TILE_RES), (int) (loc.y / game.data.TILE_RES));
		if (game.data.currentZone.layout[tilePos.y][tilePos.x].type == Tile.Type.PIT)
			return true;
		return false;
	}

	// --------------------------------------------------------- MOVEMENT

	/**
	 * Changes the entity position.
	 * 
	 * @param x
	 * speed the player is moving on x-axis
	 * @param y
	 * speed the player is moving on y-axis
	 */
	public void move(double x, double y) {

		boolean canMoveX = true;
		boolean canMoveY = true;

		if (y < 0 && collision(Direction.NORTH))
			canMoveY = false;
		if (y > 0 && collision(Direction.SOUTH))
			canMoveY = false;
		if (x > 0 && collision(Direction.EAST))
			canMoveX = false;
		if (x < 0 && collision(Direction.WEST))
			canMoveX = false;

		if (canMoveX)
			loc.x += x;
		if (canMoveY)
			loc.y += y;
	}
}
