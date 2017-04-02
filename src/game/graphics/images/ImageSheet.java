package game.graphics.images;

import java.awt.Image;
import java.awt.image.BufferedImage;

import game.Game;

/**
 * An image with special functions that utilizes a grid where each square is game.data.TILE_RES x game.data.TILE_RES.
 * 
 * @author Anthony DePaul
 */
public class ImageSheet {

	/** The game this class will refer to */
	private Game game;
	/** The big sheet image this class uses */
	public BufferedImage image;

	/**
	 * An image with special functions that utilizes a grid where each square is game.data.TILE_RES x game.data.TILE_RES.
	 * 
	 * @param g
	 * the game this object will refer to
	 * @param path
	 * location of the image to be used
	 */
	public ImageSheet(Game g, String path) {
		game = g;
		image = (BufferedImage) GraphicLoader.getImage(path);
	}

	/**
	 * An image with special functions that utilizes a grid where each square is game.data.TILE_RES x game.data.TILE_RES.
	 * 
	 * @param g
	 * the game this object will refer to
	 * @param img
	 * the image to be used
	 */
	public ImageSheet(Game g, Image img) {
		game = g;
		image = (BufferedImage) img;
	}

	/**
	 * Gets the image at the designated location according to the grid.
	 * 
	 * @param x
	 * column the image is in (starting at 0)
	 * @param y
	 * row the image is in (starting at 0)
	 * @param width
	 * how many grid squares wide
	 * @param height
	 * how many grid squares high
	 * @return
	 * the width x height image at location x, y
	 */
	public Image grabImage(int x, int y, int width, int height) {
		return image.getSubimage(x * game.data.TILE_RES, y * game.data.TILE_RES, game.data.TILE_RES * width, game.data.TILE_RES * height);
	}
}