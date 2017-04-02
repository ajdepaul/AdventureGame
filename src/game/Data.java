package game;

import game.component.Tile;
import game.component.Zone;
import game.enums.GameState;
import game.graphics.images.GraphicLoader;
import game.graphics.screens.Scene;

import java.util.ArrayList;

/**
 * A whole lot of information stored for the game
 * 
 * @author Anthony DePaul
 */
public class Data {

	/** The game this data is a part of */
	private Game game;

	/**
	 * A whole lot of information stored for the game
	 */
	public Data(Game g) {
		game = g;
	}

	// -----------------------------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------- CONSTANTS ---------------------------------------------------------
	// -----------------------------------------------------------------------------------------------------------------------------

	/** The title of the game */
	public final String GAME_TITLE = "Elemental Game";

	/** How many times the game "ticks" per second */
	public final int UPDATES_PER_SECOND = 30;

	/** How many times the game creates a tick for animation per second. Must be < UPDATES_PER_SECOND */
	public final int ANIMATION_FPS = 10;
	/** How many update ticks occur before an animation tick occurs */
	public final int AMIMATION_SPEED = UPDATES_PER_SECOND / ANIMATION_FPS;

	/** Width of a single tile (in pixels) */
	public final int TILE_RES = 32;

	/** The amount of tiles wide the canvas is */
	public final int TILES_WIDE = 30;
	/** The amount of tiles high the canvas is */
	public final int TILES_HIGH = 20;

	/** Width of canvas (in pixels) */
	public final int WIDTH = TILES_WIDE * TILE_RES;
	/** Height of canvas (in pixels) */
	public final int HEIGHT = TILES_HIGH * TILE_RES;

	// ---------------------------------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------- UPDATED DATA ----------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------

	// --------------------------------------------------------- VARIABLE DECLARATIONS

	/** Current state of the game */
	public GameState status = GameState.MAIN_MENU;

	/** Scale that changes depending on window size */
	public double scale = 1.0;
	/** Offsets so content centers on screen on the x axis */
	public int frameXOffset = 0;
	/** Offsets so content centers on screen on the y axis */
	public int frameYOffset = 0;

	/** The zone the player is in inside the currentWorld */
	public Zone currentZone;

	/** Tick counter to keep track of animation frames */
	public long animationTick = 1;
	/** Tick counter to keep track of update frames */
	public long updateTick = 1;
	/** Current Entity talking, if any */
	public Scene currentScene;

	/** Set true to attempt to activate a scene */
	public boolean sceneActivate = false;

	// --------------------------------------------------------- UPDATE

	/** Update the value of some variables */
	public void update() {
		// offsets for the screen
		if (!GameLauncher.isApplet) {
			frameXOffset = (int) ((GameLauncher.game.frame.getSize().width - (WIDTH * scale)) / 2);
			frameYOffset = (int) ((GameLauncher.game.frame.getSize().height - (HEIGHT * scale)) / 2);
			// sets the scale
			double xScale = (double) GameLauncher.game.frame.getSize().width / WIDTH;
			double yScale = (double) GameLauncher.game.frame.getSize().height / (HEIGHT);

			if (xScale < yScale)
				scale = xScale;
			else
				scale = yScale;
		}
		// get current zone
		currentZone = zones.get(currentZoneNumber);
		if (currentZoneNumber == zones.size() - 1 && !currentZone.containsBosses())
			status = GameState.WIN;
	}

	// -------------------------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------- ZONES ---------------------------------------------------------
	// -------------------------------------------------------------------------------------------------------------------------

	// --------------------------------------------------------- VARIABLE DECLARATIONS

	/** All of the zones */
	public ArrayList<Zone> zones = new ArrayList<Zone>();
	/** The zone the player is in inside the currentWorld */
	public int currentZoneNumber = 0;

	/**
	 * Initializes the zones and adds them to the worlds to be used.
	 */
	public void initZones() {
		zones.add(new Zone(GraphicLoader.getImage("zones/zone1/top.png"), GraphicLoader.getImage("zones/zone1/bottom.png"), GraphicLoader.getImage("zones/zone1/layout.png"), 0, game));

		int counter = 2;
		while (true) {
			try {
				zones.add(new Zone(GraphicLoader.getImage("zones/zone" + counter + "/top.png"), GraphicLoader.getImage("zones/zone" + counter + "/bottom.png"), GraphicLoader.getImage("zones/zone" + counter + "/layout.png"), counter - 1, game));
				counter++;
			} catch (Exception e) {
				break;
			}
		}
		currentZone = zones.get(currentZoneNumber);
	}

	/**
	 * Teleports the player
	 * 
	 * @param tp
	 * the teleport tile the player stood on
	 */
	public void playerTeleport(Tile tp) {
		if (tp.tpZone < game.data.zones.size()) {
			game.player.previousZone = currentZoneNumber;
			currentZoneNumber = tp.tpZone;
			currentZone = zones.get(currentZoneNumber);

			// custom spawn
			if (zones.get(currentZoneNumber).spawns.get(game.player.previousZone + 1) != null)
				game.player.loc.setLocation(zones.get(currentZoneNumber).spawns.get(game.player.previousZone + 1));
			// default spawn
			else
				game.player.loc.setLocation(zones.get(currentZoneNumber).spawns.get(0));
		}
	}
}
