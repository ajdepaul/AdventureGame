package game.component.entities.environment;

import game.Game;
import game.component.entities.Entity;

/**
 * An entity that can be picked up by the player.
 * 
 * @author Anthony DePaul
 */
public class Item extends Entity {

	public static final int BLANK = 0;
	// 1, 2 & 3 used for gold
	public static final int KEY_I = 4;
	public static final int KEY_II = 5;
	public static final int KEY_III = 6;
	public static final int SWORD_I = 7;
	public static final int SWORD_II = 8;
	public static final int SWORD_III = 9;
	/** The type of item */
	public int ID;
	/** How much money it will cost to pick up the item */
	public int cost = 0;

	/**
	 * An entity that can be picked up by the player.
	 * 
	 * @param g
	 * the game the item is in
	 * @param x
	 * the x location of the item
	 * @param y
	 * the y location of the item
	 * @param i
	 * the id of the item
	 */
	public Item(Game g, double x, double y, int i) {
		super(g, x, y);
		ID = i;
		updateImage();
	}

	/**
	 * An entity that can be picked up by the player.
	 * 
	 * @param g
	 * the game the item is in
	 * @param x
	 * the x location of the item
	 * @param y
	 * the y location of the item
	 * @param i
	 * the id of the item
	 * @param c
	 * the cost to pick up the item
	 */
	public Item(Game g, double x, double y, int i, int c) {
		super(g, x, y);
		ID = i;
		cost = c;
		updateImage();
	}

	/**
	 * Creates the image for the entity depending on the ID
	 */
	public void updateImage() {
		if (hp == 0)
			currentImage = null;
		else
			switch (ID) {
			case SWORD_I:
				currentImage = game.images.swordI;
				break;
			case SWORD_II:
				currentImage = game.images.swordII;
				break;
			case SWORD_III:
				currentImage = game.images.swordIII;
				break;
			case KEY_I:
				currentImage = game.images.keyI;
				break;
			case KEY_II:
				currentImage = game.images.keyII;
				break;
			case KEY_III:
				currentImage = game.images.keyIII;
				break;
			default:
				currentImage = game.images.blankItem;
				break;
			}
	}

	/**
	 * Gets the items picked up and removes it
	 */
	public void update() {
		double distanceFromPlayer = Math.sqrt((loc.x - game.player.loc.x) * (loc.x - game.player.loc.x) + (loc.y - game.player.loc.y) * (loc.y - game.player.loc.y));
		if (distanceFromPlayer < (size / 2) + (game.player.size / 2))
			if (game.player.money >= cost)
				if (game.player.inventory.pickUp(this)) {
				hp = 0;
				game.player.money -= cost;
			}
	}

	// /**
	// * Compares this entity to another by y position.
	// *
	// * @param e
	// * the Entity being compared
	// */
	// @Override
	// public int compareTo(Entity e) {
	// if (e instanceof Item || e instanceof Gold) {
	// if (loc.y < e.loc.y)
	// return -1; // beneath
	// if (loc.y > e.loc.y)
	// return 1; // on top
	// }
	// else
	// return -1; // beneath
	// return 0; // neutral
	// }
}
