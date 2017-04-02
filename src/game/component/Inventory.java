package game.component;

import java.awt.Point;

import game.component.entities.environment.Item;

/**
 * Contains items with a few special functions
 * 
 * @author Anthony DePaul
 */
public class Inventory {

	/** The items in the inventory */
	public Item[][] items = new Item[3][5];
	public Point selector = new Point(0, 0);

	/**
	 * Contains items with a few special functions
	 */
	public Inventory() {
	}

	/**
	 * Adds an item to the end of the inventory if there is space
	 * 
	 * @param a
	 * item to try to add
	 * @return
	 * true if picked up. false if full
	 */
	public boolean pickUp(Item a) {
		for (int row = 0; row < items.length; row++)
			for (int col = 0; col < items[0].length; col++)
				if (items[row][col] == null) {
					items[row][col] = a;
					return true;
				}
		return false;
	}

	/**
	 * Gets the currently selected item
	 * 
	 * @return
	 * the currently selected item
	 */
	public Item getSelected() {
		return items[selector.y][selector.x];
	}
	
	/**
	 * Removes the item the player currently has selected.
	 */
	public void removeSelected() {
		items[selector.y][selector.x] = null;
	}

	/**
	 * Adds an item to the specified location
	 * 
	 * @param x
	 * the column in the inventory
	 * @param y
	 * the row in the inventory
	 * @param item
	 * the item to be added
	 * @return
	 * true if item was successfully added. false if not
	 */
	public boolean add(int x, int y, Item item) {
		if (items[y][x] != null) {
			items[y][x] = item;
			return true;
		}
		return false;
	}

	/**
	 * Removes the item at the specified location and returns it
	 * 
	 * @param x
	 * the column in the inventory
	 * @param y
	 * the row in the inventory
	 * @return
	 * the item that was removed
	 */
	public Item remove(int x, int y) {
		Item temp = items[y][x];
		items[y][x] = null;
		return temp;
	}

	/**
	 * Removes the type of object specified from the inventory
	 * 
	 * @param i
	 * the type of object to remove
	 * @return if the specified item was removed
	 */
	public boolean remove(int i) {
		for (int row = 0; row < items.length; row++)
			for (int col = 0; col < items[0].length; col++)
				if (items[row][col] != null)
					if (items[row][col].ID == i) {
						items[row][col] = null;
						return true;
					}
		return false;
	}
}
