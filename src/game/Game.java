package game;

import game.component.entities.Entity;
import game.component.entities.Player;
import game.enums.Direction;
import game.enums.GameState;
import game.graphics.Camera;
import game.graphics.images.GraphicLoader;
import game.graphics.screens.ControlsMenu;
import game.graphics.screens.LoseScreen;
import game.graphics.screens.MainMenu;
import game.graphics.screens.PauseMenu;
import game.graphics.screens.WinScreen;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;

/**
 * An adventure game
 * 
 * @author Anthony DePaul
 */
@SuppressWarnings("serial")
public class Game extends Canvas implements Runnable {

	// --------------------------------------------------------- VARIABLE DECLARATIONS

	/** Thread that updates and rendering uses */
	private Thread thread;
	/** Keeps track of the game's running state */
	public boolean running = false;
	/** If the game is resetting */
	public boolean resetting = false;
	/** A counter used to slow/speed up animation */
	public int animationTimer = 1;
	/** Window used by this canvas */
	public JFrame frame;
	/** Image displayed on screen. Will be translated by camera */
	public BufferedImage level;
	/** Final image displayed */
	public BufferedImage scaled;

	/** Keeps track of a lot of info */
	public Data data;
	/** Holds the images */
	public GraphicLoader images;
	/** The player used for the game */
	public Player player;
	/** Used to offset the world to the screen */
	public Camera camera;

	/** The pause menu of the game */
	public PauseMenu pause;
	/** The main menu of the game */
	public MainMenu main;
	/** The win screen of the game */
	public WinScreen win;
	/** The lose screen of the game */
	public LoseScreen lose;
	/** The menu that displays controls */
	public ControlsMenu controls;
	/** The input class */
	public Input input;

	// --------------------------------------------------------- CONSTRUCTORS

	/**
	 * An adventure game.
	 */
	public Game() {
		data = new Data(this);
		images = new GraphicLoader(this);
		data.initZones();
		player = new Player(this);
		camera = new Camera(this);

		pause = new PauseMenu(this);
		main = new MainMenu(this);
		win = new WinScreen(this);
		lose = new LoseScreen(this);
		controls = new ControlsMenu(this);

		setMaximumSize(new Dimension(data.WIDTH, data.HEIGHT));
		setMinimumSize(new Dimension(data.WIDTH, data.HEIGHT));
		setPreferredSize(new Dimension(data.WIDTH, data.HEIGHT));

		input = new Input(this);

		addKeyListener(input);
		addMouseListener(input);
		addMouseMotionListener(input);
	}

	/**
	 * Resets the entire game.
	 */
	public void reset() {
		resetting = true;
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		data = new Data(this);
		images = new GraphicLoader(this);
		data.initZones();
		player = new Player(this);
		camera = new Camera(this);
		pause = new PauseMenu(this);
		main = new MainMenu(this);
		win = new WinScreen(this);
		lose = new LoseScreen(this);

		removeKeyListener(input);
		removeMouseListener(input);
		removeMouseMotionListener(input);

		input = new Input(this);

		addKeyListener(input);
		addMouseListener(input);
		addMouseMotionListener(input);

		resetting = false;
	}

	// ------------------------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------- MAIN ---------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------

	/**
	 * Plays music.
	 */
	private void playMusic() {
		// get file
		File f = null;
		try {
			f = new File(Game.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}

		String finalpath = f.getParent() + "/game_resources/sounds/music.wav";

		System.out.println(finalpath);

		// get audio file
		URL audioFilePath = null;
		try {
			audioFilePath = new File(finalpath).toURI().toURL();
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}

		// play music
		AudioInputStream audioStream = null;
		Clip audioClip = null;
		AudioFormat format;
		DataLine.Info info;

		try {
			audioStream = AudioSystem.getAudioInputStream(audioFilePath);
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		format = audioStream.getFormat();
		info = new DataLine.Info(Clip.class, format);
		try {
			audioClip = (Clip) AudioSystem.getLine(info);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		try {
			audioClip.open(audioStream);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		audioClip.start();
		audioClip.loop(Clip.LOOP_CONTINUOUSLY);
	}

	/**
	 * Sets running to true, initializes everything and starts thread.
	 */
	public synchronized void start() {
		playMusic();

		data.update();

		running = true;
		thread = new Thread(this);
		thread.start();
	}

	/**
	 * Sets running to false and joins thread.
	 */
	public synchronized void stop() {
		running = false;
		try {
			thread.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.exit(1);
	}

	// --------------------------------------------------------- THREAD

	/**
	 * Updates game 30 times a second. Renders the game. Keeps track of ups (updates per second) and fps.
	 */
	@Override
	public void run() {
		long lastTime = System.nanoTime();
		final double ns = 1000000000 / data.UPDATES_PER_SECOND;
		double delta = 0;

		int frames = 0;
		int updates = 0;
		long timer = System.currentTimeMillis();

		while (running) {
			if (!resetting) {
				long now = System.nanoTime();
				delta += (now - lastTime) / ns;
				lastTime = now;

				// on an update
				while (delta >= 1) {
					update();
					updates++;

					// overall update tick count
					if (data.updateTick < Long.MAX_VALUE && data.status == GameState.PLAYING)
						data.updateTick++;
					else if (data.status == GameState.PLAYING)
						data.updateTick = 0;

					// overall animation tick count
					if (data.animationTick < Long.MAX_VALUE && data.status == GameState.PLAYING)
						if (animationTimer < data.AMIMATION_SPEED)
							animationTimer++;
						else {
							data.animationTick++;
							animationTimer = 1;
						}
					else if (data.status == GameState.PLAYING)
						data.animationTick = 1;
					delta--;
				}
				// slows down fps (acts strangely)
				if (data.status == GameState.MAIN_MENU)
					try {
						Thread.sleep(2);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				render();
				frames++;

				// fps and ups
				if (System.currentTimeMillis() - timer > 1000) {
					timer += 1000;
					System.out.println(data.GAME_TITLE + " (" + updates + " ups, " + frames + " fps)");
					frames = 0;
					updates = 0;
				}
			} else
				System.out.println("Resetting...");
		}
		stop();
	}

	// --------------------------------------------------------- EACH TICK

	/**
	 * What the game does each tick.
	 */
	public void update() {

		data.update();

		switch (data.status) {
		case PLAYING:
			player.update();
			data.currentZone.updateEntities();
			data.sceneActivate = false;
			break;
		case SCENE:
			break;
		case PAUSED:
			pause.update();
			break;
		case MAIN_MENU:
			main.update();
			break;
		case WIN:
			win.update();
			break;
		case LOSE:
			lose.update();
		case CONTROLS_MENU:
			controls.update();
			break;
		}
	}

	// ----------------------------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------- GRAPHICS ---------------------------------------------------------
	// ----------------------------------------------------------------------------------------------------------------------------

	/**
	 * Copies a BufferedImage. (did not write myself)
	 * 
	 * @param bi
	 * BufferedImage to be copied
	 * @return copied BufferedImage
	 */
	public static BufferedImage copyImage(Image img) {
		BufferedImage b = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics g = b.getGraphics();
		g.drawImage(img, 0, 0, null);
		g.dispose();
		return b;
	}

	/**
	 * Rotates an image and draws it. (did not write myself http://stackoverflow.com/questions/8639567/java-rotating-images)
	 * 
	 * @param image
	 * BufferedImage to rotate
	 */
	public static void drawRotated(BufferedImage image, int x, int y, Direction dir, Graphics2D g2d) {
		int degrees = 0;
		switch (dir) {
		case NORTH:
			degrees = 0;
			break;
		case EAST:
			degrees = 90;
			break;
		case SOUTH:
			degrees = 180;
			break;
		case WEST:
			degrees = 270;
			break;
		}
		// Rotation information
		double rotationRequired = Math.toRadians(degrees);
		double locationX = image.getWidth() / 2;
		double locationY = image.getHeight() / 2;
		AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		// Drawing the rotated image at the required drawing locations
		g2d.drawImage(op.filter(image, null), x, y, null);
	}

	/**
	 * Renders one frame of the game.
	 */
	public void render() {

		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		Graphics2D mainG = (Graphics2D) bs.getDrawGraphics(); // actually displayed
		mainG.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		switch (data.status) {
		case PLAYING:
		case SCENE:
		case PAUSED:
		case WIN:
		case LOSE:

			// --- CONTENT

			// the entire level
			level = new BufferedImage(data.currentZone.layout[0].length * data.TILE_RES, data.currentZone.layout.length * data.TILE_RES, BufferedImage.TYPE_INT_ARGB);
			Graphics2D levelG = level.createGraphics();

			levelG.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

			// background //
			levelG.setColor(new Color(0, 22, 51));
			levelG.fillRect(0, 0, level.getWidth(), level.getHeight());

			BufferedImage botZone;
			BufferedImage topZone;

			if (data.currentZone.WIDTH < data.WIDTH)
				if (data.currentZone.HEIGHT < data.HEIGHT) {
					// small x & y
					botZone = ((BufferedImage) data.currentZone.getBottomImage()).getSubimage(0, 0, data.currentZone.WIDTH, data.currentZone.HEIGHT);
					topZone = ((BufferedImage) data.currentZone.topImage).getSubimage(0, 0, data.currentZone.WIDTH, data.currentZone.HEIGHT);
				} else {
					// small x & big y
					botZone = ((BufferedImage) data.currentZone.getBottomImage()).getSubimage(0, camera.bigYOffset(), data.currentZone.WIDTH, data.HEIGHT);
					topZone = ((BufferedImage) data.currentZone.topImage).getSubimage(0, camera.bigYOffset(), data.currentZone.WIDTH, data.HEIGHT);
				}
			else if (data.currentZone.HEIGHT < data.HEIGHT) {
				// big x & small y
				botZone = ((BufferedImage) data.currentZone.getBottomImage()).getSubimage(camera.bigXOffset(), 0, data.WIDTH, data.currentZone.HEIGHT);
				topZone = ((BufferedImage) data.currentZone.topImage).getSubimage(camera.bigXOffset(), 0, data.WIDTH, data.currentZone.HEIGHT);
			} else {
				// big x & y
				botZone = ((BufferedImage) data.currentZone.getBottomImage()).getSubimage(camera.bigXOffset(), camera.bigYOffset(), data.WIDTH, data.HEIGHT);
				topZone = ((BufferedImage) data.currentZone.topImage).getSubimage(camera.bigXOffset(), camera.bigYOffset(), data.WIDTH, data.HEIGHT);
			}

			levelG.drawImage(botZone, 0, 0, null);
			renderEntities(levelG);
			levelG.drawImage(topZone, 0, 0, null);

			levelG.dispose();

			// --- DISPLAYED

			int xtranslate = 0;
			int ytranslate = 0;

			// scaled image //
			if (!GameLauncher.isApplet)
				scaled = new BufferedImage(frame.getSize().width, frame.getSize().height, BufferedImage.TYPE_INT_ARGB);
			else
				scaled = new BufferedImage((int) (data.WIDTH * GameLauncher.inputScale), (int) (data.HEIGHT * GameLauncher.inputScale), BufferedImage.TYPE_INT_ARGB);
			Graphics2D scaledG = scaled.createGraphics();

			// applet //
			if (GameLauncher.isApplet)
				mainG.fillRect(0, 0, (int) (data.WIDTH * data.scale), (int) (data.HEIGHT * data.scale)); // background

			// full screen //
			if (!GameLauncher.isApplet) {

				xtranslate = (int) ((GameLauncher.game.frame.getWidth() - (data.WIDTH * data.scale)) / 2);
				ytranslate = (int) ((GameLauncher.game.frame.getHeight() - (data.HEIGHT * data.scale)) / 2);

				scaledG.setColor(new Color(0, 0, 0));
				scaledG.fillRect(xtranslate, ytranslate, (int) (data.WIDTH * data.scale), (int) (data.HEIGHT * data.scale)); // background

				scaledG.translate(data.frameXOffset, data.frameYOffset);
				scaledG.scale(data.scale, data.scale);
			}

			// draw main //
			scaledG.drawImage(level, camera.smallXOffset(), camera.smallYOffset(), null);

			// scene //
			if (data.status == GameState.SCENE)
				data.currentScene.render(scaledG);

			mainG.drawImage(scaled, 0, 0, null);

			// full screen overlay //
			if (!GameLauncher.isApplet) {
				mainG.setColor(new Color(0, 0, 0));
				if (GameLauncher.game.frame.getWidth() > GameLauncher.game.frame.getHeight()) {
					mainG.fillRect(0, 0, xtranslate, GameLauncher.game.frame.getHeight());
					mainG.fillRect(xtranslate + (int) (data.WIDTH * data.scale), 0, xtranslate, GameLauncher.game.frame.getHeight());

				} else { // vertical/square screens need to be tested
					mainG.fillRect(0, 0, GameLauncher.game.frame.getWidth(), ytranslate);
					mainG.fillRect(0, ytranslate + (int) (data.HEIGHT * data.scale), GameLauncher.game.frame.getWidth(), ytranslate);
				}
			}

			// paused //
			if (data.status == GameState.PAUSED) {
				if (!GameLauncher.isApplet) {
					mainG.translate(data.frameXOffset, data.frameYOffset);
					mainG.scale(data.scale, data.scale);
				}
				pause.render(mainG);
			}
			// win //
			if (data.status == GameState.WIN) {
				if (!GameLauncher.isApplet) {
					mainG.translate(data.frameXOffset, data.frameYOffset);
					mainG.scale(data.scale, data.scale);
				}
				win.render(mainG);
			}

			// lose //
			if (data.status == GameState.LOSE) {
				if (!GameLauncher.isApplet) {
					mainG.translate(data.frameXOffset, data.frameYOffset);
					mainG.scale(data.scale, data.scale);
				}
				lose.render(mainG);
			}

			break;

		case MAIN_MENU:
			// background //
			mainG.setColor(new Color(0, 0, 0));

			if (!GameLauncher.isApplet) {
				mainG.fillRect(0, 0, frame.getSize().width, frame.getSize().height);
				mainG.translate(data.frameXOffset, data.frameYOffset);
				mainG.scale(data.scale, data.scale);
			} else {
				mainG.fillRect(0, 0, (int) (data.WIDTH * GameLauncher.inputScale), (int) (data.HEIGHT * GameLauncher.inputScale));
			}
			main.render(mainG);
			break;
		case CONTROLS_MENU:
			// background //
			mainG.setColor(new Color(0, 0, 0));

			if (!GameLauncher.isApplet) {
				mainG.fillRect(0, 0, frame.getSize().width, frame.getSize().height);
				mainG.translate(data.frameXOffset, data.frameYOffset);
				mainG.scale(data.scale, data.scale);
			} else {
				mainG.fillRect(0, 0, (int) (data.WIDTH * GameLauncher.inputScale), (int) (data.HEIGHT * GameLauncher.inputScale));
			}
			controls.render(mainG);
			break;
		}

		mainG.dispose();
		bs.show();
	}

	/**
	 * Draws all the entities in the current zone.
	 * 
	 * @param g
	 * the graphics object used to draw
	 */
	public void renderEntities(Graphics2D g) {

		// update for animation
		player.updateImage();
		data.currentZone.updateEntityImages();

		// list of ALL the entities in the current zone
		ArrayList<Entity> tempEntities = new ArrayList<Entity>(data.currentZone.entities);
		tempEntities.add(player);

		// sort all entities by y position
		Collections.sort(tempEntities);

		// draw all the entities
		for (Entity temp : tempEntities) {
			g.drawImage(temp.currentImage, temp.locXToScreen(), temp.locYToScreen(), null);
			temp.drawHp(g);
		}
	}
}
