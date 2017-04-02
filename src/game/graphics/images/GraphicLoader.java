package game.graphics.images;

import game.Game;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * All of the image resources used in a game.
 * 
 * @author Anthony DePaul
 */
public class GraphicLoader {

	// --------------------------------------------------------- VARIABLE DECLARATIONS

	// --- menus --- //
	// main
	public Image mainPlayButton, mainQuitButton, mainControlsButton, mainBackground;
	// pause
	public Image pauseBackground, pauseMainMenuButton, pauseResumeButton, pauseSelector;
	// win
	public Image winBackground, winMainMenuButton;
	// lose
	public Image loseBackground, loseMainMenuButton;
	// scene
	public Image sceneBackground;
	// controls
	public Image controlsBackground, controlsMainMenuButton;

	// --- creatures --- //
	// player
	public ImageSheet player;
	public CustomAnimation playerJumpN, playerJumpE, playerJumpS, playerJumpW;
	public CustomAnimation playerWalkN, playerWalkE, playerWalkS, playerWalkW;
	public CustomAnimation playerAttackNI, playerAttackEI, playerAttackSI, playerAttackWI;
	public CustomAnimation playerAttackNII, playerAttackEII, playerAttackSII, playerAttackWII;
	public CustomAnimation playerAttackNIII, playerAttackEIII, playerAttackSIII, playerAttackWIII;
	// basic enemy
	public ImageSheet basicenemy;
	public CustomAnimation basicEnemyWalkN, basicEnemyWalkE, basicEnemyWalkS, basicEnemyWalkW;
	// mini boss
	public ImageSheet miniBoss;
	public CustomAnimation miniBossWalkN, miniBossWalkE, miniBossWalkS, miniBossWalkW;
	// final boss
	public ImageSheet finalBoss;
	public CustomAnimation finalBossWalkN, finalBossWalkE, finalBossWalkS, finalBossWalkW;

	// --- environment --- //
	// items
	public Image blankItem, swordI, swordII, swordIII, keyI, keyII, keyIII;
	// money
	public Image moneyI, moneyII, moneyIII;
	// tiles
	public ImageSheet lockI, lockII, lockIII;
	// destructibles
	public ImageSheet destructible1, destructible2, destructible3;

	/**
	 * Gets an image from a path.
	 * 
	 * @param path
	 * location of image
	 * @return Image object of the image at that location
	 */
	public static Image getImage(String path) {
		File f = null;

		try {
			f = new File(Game.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}

		String finalpath = f.getParent() + "/game_resources/" + path;
		try {
			return ImageIO.read(new File(finalpath));
		} catch (IOException e2) {
			if (!finalpath.contains("zone"))
				e2.printStackTrace();
		}
		return null;
	}

	/**
	 * Gets an imageicon from a path.
	 * 
	 * @param path
	 * location of imageicon
	 * @return
	 * ImageIcon object of the image at that location
	 */
	public static ImageIcon getImageIcon(String path) {
		File f = null;
		try {
			f = new File(Game.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
		} catch (Exception e) {
			e.printStackTrace();
		}
		String finalpath = f.getParent() + "/game_resources/" + path;
		return new ImageIcon(finalpath);
	}

	// --------------------------------------------------------- INIT

	/**
	 * Initializes all images/animations needed throughout the game.
	 */
	public GraphicLoader(Game game) {

		// --- menus --- //
		// main
		mainPlayButton = getImage("menus/main/play.png");
		mainQuitButton = getImage("menus/main/quit.png");
		mainControlsButton = getImage("menus/main/controls.png");
		mainBackground = getImage("menus/main/background.png");
		// pause
		pauseBackground = getImage("menus/pause/background.png");
		pauseMainMenuButton = getImage("menus/pause/main_menu.png");
		pauseResumeButton = getImage("menus/pause/resume.png");
		pauseSelector = getImage("menus/pause/selector.png");
		// win
		winBackground = getImage("menus/win/background.png");
		winMainMenuButton = getImage("menus/win/main_menu.png");
		// lose
		loseMainMenuButton = getImage("menus/lose/main_menu.png");
		loseBackground = getImage("menus/lose/background.png");
		// scene
		sceneBackground = getImage("menus/scene/background.png");
		// controls
		controlsBackground = getImage("menus/controls/background.png"); 
		controlsMainMenuButton = getImage("menus/controls/main_menu.png");

		// --- creatures --- //
		// player
		player = new ImageSheet(game, "entities/player/main.png");
		playerJumpN = new CustomAnimation(game, "entities/player/jump/N", 2, false);
		playerJumpE = new CustomAnimation(game, "entities/player/jump/E", 2, false);
		playerJumpS = new CustomAnimation(game, "entities/player/jump/S", 2, false);
		playerJumpW = new CustomAnimation(game, "entities/player/jump/W", 2, false);
		playerWalkN = new CustomAnimation(game, "entities/player/walk/N", 2, true);
		playerWalkE = new CustomAnimation(game, "entities/player/walk/E", 2, true);
		playerWalkS = new CustomAnimation(game, "entities/player/walk/S", 2, true);
		playerWalkW = new CustomAnimation(game, "entities/player/walk/W", 2, true);
		playerAttackNI = new CustomAnimation(game, "entities/player/attackI/N", 3, false);
		playerAttackEI = new CustomAnimation(game, "entities/player/attackI/E", 3, false);
		playerAttackSI = new CustomAnimation(game, "entities/player/attackI/S", 3, false);
		playerAttackWI = new CustomAnimation(game, "entities/player/attackI/W", 3, false);
		playerAttackNII = new CustomAnimation(game, "entities/player/attackII/N", 3, false);
		playerAttackEII = new CustomAnimation(game, "entities/player/attackII/E", 3, false);
		playerAttackSII = new CustomAnimation(game, "entities/player/attackII/S", 3, false);
		playerAttackWII = new CustomAnimation(game, "entities/player/attackII/W", 3, false);
		playerAttackNIII = new CustomAnimation(game, "entities/player/attackIII/N", 3, false);
		playerAttackEIII = new CustomAnimation(game, "entities/player/attackIII/E", 3, false);
		playerAttackSIII = new CustomAnimation(game, "entities/player/attackIII/S", 3, false);
		playerAttackWIII = new CustomAnimation(game, "entities/player/attackIII/W", 3, false);
		// basic enemy
		basicenemy = new ImageSheet(game, "entities/basic_enemy/main.png");
		basicEnemyWalkN = new CustomAnimation(game, "entities/basic_enemy/walk/N", 2, true);
		basicEnemyWalkE = new CustomAnimation(game, "entities/basic_enemy/walk/E", 2, true);
		basicEnemyWalkS = new CustomAnimation(game, "entities/basic_enemy/walk/S", 2, true);
		basicEnemyWalkW = new CustomAnimation(game, "entities/basic_enemy/walk/W", 2, true);
		// mini boss
		miniBoss = new ImageSheet(game, "entities/mini_boss/main.png");
		miniBossWalkN = new CustomAnimation(game, "entities/mini_boss/walk/N", 3, true);
		miniBossWalkE = new CustomAnimation(game, "entities/mini_boss/walk/E", 3, true);
		miniBossWalkS = new CustomAnimation(game, "entities/mini_boss/walk/S", 3, true);
		miniBossWalkW = new CustomAnimation(game, "entities/mini_boss/walk/W", 3, true);
		// final boss
		finalBoss = new ImageSheet(game, "entities/final_boss/main.png");
		finalBossWalkN = new CustomAnimation(game, "entities/final_boss/walk/N", 3, true);
		finalBossWalkE = new CustomAnimation(game, "entities/final_boss/walk/E", 3, true);
		finalBossWalkS = new CustomAnimation(game, "entities/final_boss/walk/S", 3, true);
		finalBossWalkW = new CustomAnimation(game, "entities/final_boss/walk/W", 3, true);

		// --- environment --- //
		// items
		blankItem = getImage("entities/items/blank.png");
		swordI = getImage("entities/items/sword_i.png");
		swordII = getImage("entities/items/sword_ii.png");
		swordIII = getImage("entities/items/sword_iii.png");
		keyI = getImage("entities/items/key_i.png");
		keyII = getImage("entities/items/key_ii.png");
		keyIII = getImage("entities/items/key_iii.png");
		// money
		moneyI = getImage("entities/items/money_i.png");
		moneyII = getImage("entities/items/money_ii.png");
		moneyIII = getImage("entities/items/money_iii.png");
		// tiles
		lockI = new ImageSheet(game, "zones/tiles/lock_i.png");
		lockII = new ImageSheet(game, "zones/tiles/lock_ii.png");
		lockIII = new ImageSheet(game, "zones/tiles/lock_iii.png");
		// destructibles
		destructible1 = new ImageSheet(game, "entities/destructables/object1.png");
		destructible2 = new ImageSheet(game, "entities/destructables/object2.png");
		destructible3 = new ImageSheet(game, "entities/destructables/object3.png");
	}
}