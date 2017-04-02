package game.graphics.images;

import java.awt.Image;
import java.awt.Point;

import game.Game;

/**
 * A frame by frame animation through a PNG image.
 * 
 * @author Anthony DePaul
 */
public class Animation {

	/** The game this animation is in */
	protected Game game;
	/** Source image used for the animation */
	protected ImageSheet images;
	/** The update tick the animation starts on. Keeps track of how much time has passed */
	private int startTick = 0;
	/** True if the animation is playing. False if not */
	public boolean playing;
	/** Location of frames */
	protected Point[] locs;
	/** Width of a single frame in pixels */
	protected int width;
	/** Height of a single frame in pixels */
	protected int height;
	/** If the animation loops */
	protected boolean looped;

	/**
	 * For superclass.
	 */
	public Animation() {
	}

	/**
	 * A frame by frame animation through a PNG image.
	 * 
	 * @param imgsheet
	 * the images used
	 * @param w
	 * width of a frame in game.data.TILE_RES
	 * @param h
	 * height of a frame in game.data.TILE_RES
	 * @param lo
	 * if the animation loops
	 * @param g
	 * the game this object will refer to
	 * @param l
	 * the locations of the frames
	 */
	public Animation(ImageSheet imgsheet, int w, int h, boolean lo, Game g, Point... l) {
		images = imgsheet;
		locs = l;
		width = w * game.data.TILE_RES;
		height = h * game.data.TILE_RES;
		looped = lo;
		game = g;
	}

	/**
	 * Simplified animation constructor. Assumes the imagesheet is one column of images of height h and makes it an animation.
	 * 
	 * @param g
	 * the game this object will refer to
	 * @param path
	 * the path of the imagesheet
	 * @param h
	 * height of a frame (must be a multiple of game.data.TILE_RES)
	 * @param l
	 * if the animation loops
	 */
	public Animation(Game g, String path, int h, boolean o) {
		game = g;
		images = new ImageSheet(game, path);
		width = images.image.getWidth();
		height = h * game.data.TILE_RES;
		looped = o;
		int frames = images.image.getHeight() / height;
		locs = new Point[frames];
		for (int i = 0; i < frames; i++)
			locs[i] = new Point(0, i * height);
	}

	/**
	 * Starts the animation over.
	 */
	public void start() {
		playing = true;
		startTick = (int) game.data.animationTick;
	}

	/**
	 * Gets the current image of the animation.
	 * 
	 * @return the current image the animation is on
	 */
	public Image getImage() {
		// resets animation if looped
		if (!playing && looped) {
			playing = true;
			startTick = (int) game.data.animationTick;
		}

		int currentframe = (int) (game.data.animationTick - startTick);
		if (currentframe < locs.length && playing)
			return images.grabImage(locs[currentframe].x, locs[currentframe].y, width / game.data.TILE_RES, height / game.data.TILE_RES);

		playing = false;
		return images.grabImage(locs[locs.length - 1].x, locs[locs.length - 1].y, width / game.data.TILE_RES, height / game.data.TILE_RES);
	}
}
