package game.graphics.images;

import java.awt.Image;
import java.awt.Point;
import java.io.IOException;

import javax.swing.ImageIcon;

import game.Game;

/**
 * An animation which can either be in GIF or PNG format.
 * 
 * @author Anthony DePaul
 */
public class CustomAnimation extends Animation {

	/** If this animation uses a GIF */
	public boolean isGif;
	/** The GIF this animation uses */
	public ImageIcon gif;

	/**
	 * An animation which can either be in GIF or PNG format.
	 * 
	 * @param g
	 * the game this animation is in
	 * @param path
	 * the path of the image excluding the extension
	 * @param h
	 * height of a frame (must be a multiple of game.data.TILE_RES)
	 * @param l
	 * if the animation loops (only applies to non-gifs)
	 * @throws IOException
	 * throws exception if cannot find the image
	 */
	public CustomAnimation(Game g, String path, int h, boolean l) {
		game = g;

		try { // png
			Image img = GraphicLoader.getImage(path + ".png");

			isGif = false;
			images = new ImageSheet(game, img);
			width = images.image.getWidth();
			height = h * game.data.TILE_RES;
			looped = l;

			int frames = images.image.getHeight() / height;
			locs = new Point[frames];
			for (int i = 0; i < frames; i++)
				locs[i] = new Point(0, i * (height / game.data.TILE_RES));
			
		} catch (Exception e) { // gif
			isGif = true;
			gif = GraphicLoader.getImageIcon(path + ".gif");
		}
	}

	/**
	 * Gets the current image of the animation.
	 * 
	 * @return
	 * the current image the animation is on
	 */
	public Image getImage() {
		if (!isGif)
			return super.getImage();
		else
			return gif.getImage();
	}

	/**
	 * Resets the animation.
	 */
	public void start() {
		if (!isGif)
			super.start();
		else
			gif.getImage().flush();
	}
}
