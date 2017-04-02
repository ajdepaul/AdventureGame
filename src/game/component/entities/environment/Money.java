package game.component.entities.environment;

import game.Game;
import game.component.entities.Entity;

/**
 * Money that can be picked up and increases the players money count
 * 
 * @author Anthony DePaul
 */
public class Money extends Entity {

	/** the amount of money to add to the player when it gets picked up */
	public int amount;

	public Money(Game g, int a) {
		super(g, 0, 0);
		amount = a;
	}

	/**
	 * money that can be picked up and increases the players money count
	 * 
	 * @param g
	 * the game this money is in
	 * @param x
	 * the x location of the money
	 * @param y
	 * the y location of the money
	 * @param a
	 * the amount of money to add to the player when it gets picked up
	 */
	public Money(Game g, double x, double y, int a) {
		super(g, x, y);
		amount = a;
		updateImage();
	}

	/**
	 * Creates the image for money depending on the amount
	 */
	public void updateImage() {
		if (hp == 0)
			currentImage = null;
		else
			switch (amount) {
			case 1:
				currentImage = game.images.moneyI;
				break;
			case 5:
				currentImage = game.images.moneyII;
				break;
			case 10:
				currentImage = game.images.moneyIII;
				break;
			default:
				currentImage = game.images.blankItem;
				break;
			}
	}

	/**
	 * Gets the money picked up and removes it
	 */
	public void update() {
		double distanceFromPlayer = Math.sqrt((loc.x - game.player.loc.x) * (loc.x - game.player.loc.x) + (loc.y - game.player.loc.y) * (loc.y - game.player.loc.y));
		if (distanceFromPlayer < (size / 2) + (game.player.size / 2)) {
			game.player.money += amount;
			hp = 0;
		}
	}
}
