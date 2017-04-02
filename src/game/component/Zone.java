package game.component;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import game.Game;
import game.component.entities.Entity;
import game.component.entities.enemies.BasicEnemy;
import game.component.entities.enemies.FinalBoss;
import game.component.entities.enemies.MiniBoss;
import game.component.entities.environment.Destructible;
import game.component.entities.environment.Item;
import game.component.entities.environment.Money;
import game.component.entities.friendlies.Friendly;
import game.enums.Direction;
import game.graphics.images.ImageSheet;
import game.graphics.screens.Scene;

/**
 * A zone containing tiles and entities.
 * 
 * @author Anthony DePaul
 */
public class Zone {

	// --------------------------------------------------------- VARIABLE DECLARATIONS

	/** The grid of tiles this zone will represent */
	public Tile[][] layout;
	/** The image displayed on top of the entities */
	public Image topImage;
	/** The image displayed underneath the entities */
	public Image bottomImage;
	/** All the entities in the zone */
	public ArrayList<Entity> entities;
	/** The game this zone is in */
	private Game game;
	/** Location of all the spawns */
	public Map<Integer, Point2D.Double> spawns = new HashMap<Integer, Double>();
	/** Width of zone in pixels */
	public int WIDTH;
	/** Height of zone in pixels */
	public int HEIGHT;
	/** The number the zone is in the zones list */
	public int zoneNumber;

	// --------------------------------------------------------- CONSTRUCTORS

	/**
	 * Constructor specifying the images and layout.
	 * 
	 * @param top
	 * the top image to be displayed on top of the entities
	 * @param bottom
	 * the bottom image to be displayed beneath the entities
	 * @param layoutImage
	 * the grid of tiles this zone will represent
	 * @param zn
	 * the zone number this zone is in the zones list
	 * @param g
	 * the game this zone is in
	 */
	public Zone(Image top, Image bottom, Image layoutImage, int zn, Game g) {
		topImage = top;
		bottomImage = bottom;
		entities = new ArrayList<Entity>();
		zoneNumber = zn;
		game = g;

		createLayout(layoutImage);
	}

	/**
	 * Gets the spawn location coming from the specified zone.
	 * 
	 * @param zone
	 * the zone the player is coming from
	 * @return
	 * the location the player should spawn in
	 */
	public Point2D.Double getSpawn(int zone) {
		return spawns.get(zone);
	}

	/**
	 * Returns the bottom image of this zone.
	 */
	public Image getBottomImage() {
		Image image = Game.copyImage(bottomImage);
		Graphics2D g = (Graphics2D) image.getGraphics();
		for (int row = 0; row < layout.length; row++)
			for (int col = 0; col < layout[0].length; col++) {
				Tile temp = layout[row][col];

				switch (temp.type) {
				case LOCK_I:
					if (temp.isCollidable)
						Game.drawRotated((BufferedImage) game.images.lockI.grabImage(0, 0, 1, 1), col * game.data.TILE_RES, row * game.data.TILE_RES, temp.dir, g);
					else
						Game.drawRotated((BufferedImage) game.images.lockI.grabImage(0, 1, 1, 1), col * game.data.TILE_RES, row * game.data.TILE_RES, temp.dir, g);
					break;
				case LOCK_II:
					if (temp.isCollidable)
						Game.drawRotated((BufferedImage) game.images.lockII.grabImage(0, 0, 1, 1), col * game.data.TILE_RES, row * game.data.TILE_RES, temp.dir, g);
					else
						Game.drawRotated((BufferedImage) game.images.lockII.grabImage(0, 1, 1, 1), col * game.data.TILE_RES, row * game.data.TILE_RES, temp.dir, g);
					break;
				case LOCK_III:
					if (temp.isCollidable)
						Game.drawRotated((BufferedImage) game.images.lockIII.grabImage(0, 0, 1, 1), col * game.data.TILE_RES, row * game.data.TILE_RES, temp.dir, g);
					else
						Game.drawRotated((BufferedImage) game.images.lockIII.grabImage(0, 1, 1, 1), col * game.data.TILE_RES, row * game.data.TILE_RES, temp.dir, g);
					break;
				default:
					break;
				}
			}
		return image;
	}

	/**
	 * Updates all the entities images in the zone.
	 */
	public void updateEntityImages() {
		if (entities.size() != 0)
			for (Entity e : entities)
				e.updateImage();
	}

	/**
	 * Updates all the entities in the zone.
	 */
	public void updateEntities() {
		if (entities.size() != 0)
			for (int i = 0; i < entities.size(); i++) {
				Entity temp = entities.get(i);
				temp.update();
				if ((temp instanceof Money || temp instanceof Item) && temp.hp == 0) { // <-------- do entity drops break this? does that even matter?
					entities.remove(i);
					i--;
				}
			}
	}

	/** Checks if the zone contains a boss */
	public boolean containsBosses() {
		for (Entity e : entities)
			if ((e instanceof FinalBoss.DividedBoss || e instanceof FinalBoss) && e.isAlive)
				return true;
		return false;
	}

	/**
	 * Creates layout based off of an image.
	 */
	private void createLayout(Image layoutImage) {
		layout = new Tile[layoutImage.getHeight(null)][layoutImage.getWidth(null)];
		WIDTH = layout[0].length * game.data.TILE_RES;
		HEIGHT = layout.length * game.data.TILE_RES;

		// for each pixel
		for (int row = 0; row < layoutImage.getHeight(null); row++) {
			for (int col = 0; col < layoutImage.getWidth(null); col++) {
				Color temp = new Color(((BufferedImage) layoutImage).getRGB(col, row));
				int r = temp.getRed(), g = temp.getGreen(), b = temp.getBlue();

				// black -> wall
				if (r == 0 && g == 0 && b == 0)
					layout[row][col] = Tile.WALL;
				// magenta -> pit
				else if (r == 255 && g == 0 && b == 255)
					layout[row][col] = Tile.PIT;
				// blue -> spawn
				else if (r == 0 && g <= 254 && b == 255) {
					layout[row][col] = Tile.OPEN;
					spawns.put(g, new Point2D.Double(col * game.data.TILE_RES + (game.data.TILE_RES / 2), row * game.data.TILE_RES + (game.data.TILE_RES / 2)));
				}
				// green -> friendly
				else if (g == 255 && b >= 0 && b <= 3 && checkFile("/game_resources/entities/friendly/" + r + "/main.png")) {
					layout[row][col] = Tile.OPEN;
					Friendly tempFriendly = new Friendly(game, new ImageSheet(game, "entities/friendly/" + r + "/main.png"), col * game.data.TILE_RES + (game.data.TILE_RES / 2), row * game.data.TILE_RES + (game.data.TILE_RES / 2), false, null, b);
					entities.add(tempFriendly);
					if (checkFile("/game_resources/entities/friendly/" + r + "/speech.txt"))
						tempFriendly.scene = new Scene(game, "/game_resources/entities/friendly/" + r + "/speech.txt", true);
				}
				// yellow -> tp
				else if (r == 255 && g == 255 && b == 0)
					layout[row][col] = new Tile(zoneNumber + 1);
				// other shade of yellow -> custom tp
				else if (r == 255 && g == 255 && b > 0 && b < 255)
					layout[row][col] = new Tile(b - 1);
				// red -> enemy
				else if (r == 255 && g == 0 && b == 0) {
					layout[row][col] = Tile.OPEN;
					entities.add(new BasicEnemy(game, col * game.data.TILE_RES + (game.data.TILE_RES / 2), row * game.data.TILE_RES + (game.data.TILE_RES / 2)));
				}
				// cyan -> mini boss
				else if (r == 0 && g == 255 && b == 255) {
					entities.add(new MiniBoss(game, col * game.data.TILE_RES + (game.data.TILE_RES / 2), row * game.data.TILE_RES + (game.data.TILE_RES / 2)));
					layout[row][col] = Tile.OPEN;
				}
				// cyanish -> final boss
				else if (r == 1 && g == 255 && b == 255) {
					entities.add(new FinalBoss(game, col * game.data.TILE_RES + (game.data.TILE_RES / 2), row * game.data.TILE_RES + (game.data.TILE_RES / 2)));
					layout[row][col] = Tile.OPEN;
				}
				// money
				else if (r == 100 && g == 150 && b <= 3) {
					switch (b) {
					case 1:
						entities.add(new Money(game, col * game.data.TILE_RES + (game.data.TILE_RES / 2), row * game.data.TILE_RES + (game.data.TILE_RES / 2), 1));
						break;
					case 2:
						entities.add(new Money(game, col * game.data.TILE_RES + (game.data.TILE_RES / 2), row * game.data.TILE_RES + (game.data.TILE_RES / 2), 5));
						break;
					case 3:
						entities.add(new Money(game, col * game.data.TILE_RES + (game.data.TILE_RES / 2), row * game.data.TILE_RES + (game.data.TILE_RES / 2), 10));
						break;
					}
					layout[row][col] = Tile.OPEN;
				}
				// items
				else if (r >= 100 && g == 150) {
					entities.add(new Item(game, col * game.data.TILE_RES + (game.data.TILE_RES / 2), row * game.data.TILE_RES + (game.data.TILE_RES / 2), b, (r - 100) * 5));
					layout[row][col] = Tile.OPEN;
				}
				// locks N
				else if (r == 51 && b == 150) {
					if (g == 51)
						layout[row][col] = new Tile(Tile.Type.LOCK_I, Direction.NORTH, true);
					else if (g == 52)
						layout[row][col] = new Tile(Tile.Type.LOCK_II, Direction.NORTH, true);
					else if (g == 53)
						layout[row][col] = new Tile(Tile.Type.LOCK_III, Direction.NORTH, true);
				}
				// locks E
				else if (r == 52 && b == 150) {
					if (g == 51)
						layout[row][col] = new Tile(Tile.Type.LOCK_I, Direction.EAST, true);
					else if (g == 52)
						layout[row][col] = new Tile(Tile.Type.LOCK_II, Direction.EAST, true);
					else if (g == 53)
						layout[row][col] = new Tile(Tile.Type.LOCK_III, Direction.EAST, true);
				}
				// locks S
				else if (r == 53 && b == 150) {
					if (g == 51)
						layout[row][col] = new Tile(Tile.Type.LOCK_I, Direction.SOUTH, true);
					else if (g == 52)
						layout[row][col] = new Tile(Tile.Type.LOCK_II, Direction.SOUTH, true);
					else if (g == 53)
						layout[row][col] = new Tile(Tile.Type.LOCK_III, Direction.SOUTH, true);
				}
				// locks W
				else if (r == 54 && b == 150) {
					if (g == 51)
						layout[row][col] = new Tile(Tile.Type.LOCK_I, Direction.WEST, true);
					else if (g == 52)
						layout[row][col] = new Tile(Tile.Type.LOCK_II, Direction.WEST, true);
					else if (g == 53)
						layout[row][col] = new Tile(Tile.Type.LOCK_III, Direction.WEST, true);
				}
				// destructibles
				else if (g == 150 && b == 0) {
					if (r == 1) {
						entities.add(new Destructible(game, game.images.destructible1, col * game.data.TILE_RES + (game.data.TILE_RES / 2), row * game.data.TILE_RES + (game.data.TILE_RES / 2), new Money(game, 1), new Money(game, 5), null));
						layout[row][col] = Tile.OPEN;
					} else if (r == 2) {
						entities.add(new Destructible(game, game.images.destructible2, col * game.data.TILE_RES + (game.data.TILE_RES / 2), row * game.data.TILE_RES + (game.data.TILE_RES / 2), new Money(game, 1), new Money(game, 5), null));
						layout[row][col] = Tile.OPEN;
					} else if (r == 3) {
						entities.add(new Destructible(game, game.images.destructible3, col * game.data.TILE_RES + (game.data.TILE_RES / 2), row * game.data.TILE_RES + (game.data.TILE_RES / 2), new Money(game, 1), new Money(game, 5), null));
						layout[row][col] = Tile.OPEN;
					}

				}
				// any other color -> open
				else
					layout[row][col] = Tile.OPEN;
			}
		}
	}

	/**
	 * Check to see if the file exists
	 * 
	 * @param path
	 * the location of the file to check
	 * @return
	 * if the files exists
	 */
	public static boolean checkFile(String path) {
		File temp = null;

		try {
			temp = new File(Game.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		String finalpath = temp.getParent() + path;
		File check = new File(finalpath);

		if (check.exists())
			return true;
		return false;

	}
}
