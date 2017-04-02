package game.component;

import game.enums.Direction;

/**
 * A single TILE_RES x TILE_RES tile within a zone.
 * 
 * @author Anthony DePaul
 */
public class Tile {

	// --------------------------------------------------------- VARIABLE DECLARATIONS

	/** Open space */
	public static final Tile OPEN = new Tile(Type.OPEN);
	/** A wall */
	public static final Tile WALL = new Tile(Type.WALL);
	/** A pit */
	public static final Tile PIT = new Tile(Type.PIT);

	/** The zone that this tile will teleport the player to */
	public int tpZone;
	/** Type of tile */
	public Type type;
	/** If entities can move through this tile */
	public boolean isCollidable = false;
	/** Direction used for locks */
	public Direction dir = Direction.NORTH;
	
	public enum Type {
		OPEN, WALL, PIT, TELEPORT, LOCK_I, LOCK_II, LOCK_III;
	}

	// --------------------------------------------------------- CONSTRUCTORS

	/**
	 * Constructor specifying type. Not a teleport.
	 * 
	 * @param t
	 * the type of tile
	 */
	public Tile(Type t) { // pit, wall, open
		type = t;
		switch (type) {
		case LOCK_I:
		case LOCK_II:
		case LOCK_III:
		case WALL:
			isCollidable = true;
		default:
		}
	}

	/**
	 * Constructor mainly used for locks.
	 * 
	 * @param t
	 * the type of tile
	 * @param d
	 * the direction of the lock
	 */
	public Tile(Type t, Direction d, boolean x) { // lock
		type = t;
		dir = d;
		switch (type) {
		case LOCK_I:
		case LOCK_II:
		case LOCK_III:
		case WALL:
			isCollidable = true;
		default:
		}
		
		isCollidable = x;
	}

	/**
	 * Constructor specifying type and teleport location.
	 * 
	 * @param t
	 * the type of tile
	 * @param zone
	 * the zone to teleport to
	 */
	public Tile(int zone) { // teleport
		tpZone = zone;
		type = Type.TELEPORT;
	}

	/** Opens a lock */
	public void openLock() {
		switch (type) {
		case LOCK_I:
		case LOCK_II:
		case LOCK_III:
			isCollidable = false;
		default:
		}
	}
}
