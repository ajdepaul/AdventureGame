package game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import game.component.entities.environment.Item;
import game.enums.Direction;
import game.enums.EntityState;
import game.enums.GameState;
import game.graphics.screens.Button;
import game.graphics.screens.Scene;

/**
 * @author Anthony DePaul
 */
public class Input implements KeyListener, MouseListener, MouseMotionListener {

	/** The game this class will refer to */
	private Game game;

	public Input(Game g) {
		game = g;
	}

	// ------------------------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------- KEYS ---------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------

	/**
	 * Occurs when a button is pressed.
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();

		if (game.data.status == GameState.PAUSED) {
			// wasd
			if ((key == KeyEvent.VK_UP || key == KeyEvent.VK_W) && game.player.inventory.selector.y > 0)
				game.player.inventory.selector.y--;
			else if ((key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) && game.player.inventory.selector.x < game.player.inventory.items[0].length - 1)
				game.player.inventory.selector.x++;
			else if ((key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) && game.player.inventory.selector.y < game.player.inventory.items.length - 1)
				game.player.inventory.selector.y++;
			else if ((key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) && game.player.inventory.selector.x > 0)
				game.player.inventory.selector.x--;
		}

		if (game.player.isAlive) {
			// wasd
			if (key == KeyEvent.VK_W)
				game.player.movingNorth = true;
			else if (key == KeyEvent.VK_D)
				game.player.movingEast = true;
			else if (key == KeyEvent.VK_S)
				game.player.movingSouth = true;
			else if (key == KeyEvent.VK_A)
				game.player.movingWest = true;
		}

		Item selected = game.player.inventory.getSelected();

		// player controls
		if (game.player.isAlive && game.player.canControl && game.data.status == GameState.PLAYING) {

			// jump
			if (key == KeyEvent.VK_SPACE) {
				if (game.player.status == EntityState.GROUND) {
					game.player.jumpStartTick = game.data.updateTick;
					game.player.jumpN.start();
					game.player.jumpE.start();
					game.player.jumpS.start();
					game.player.jumpW.start();
				}
			}

			// attack!
			if (selected != null) {
				if (selected.ID == Item.SWORD_I || selected.ID == Item.SWORD_II || selected.ID == Item.SWORD_III) {
					if (key == KeyEvent.VK_UP) {
						game.player.attackDir = Direction.NORTH;
						game.player.attackStartTick = game.data.updateTick;
						game.player.canControl = false;
						game.player.attackNI.start();
					} else if (key == KeyEvent.VK_RIGHT) {
						game.player.attackDir = Direction.EAST;
						game.player.attackStartTick = game.data.updateTick;
						game.player.canControl = false;
						game.player.attackEI.start();
					} else if (key == KeyEvent.VK_DOWN) {
						game.player.attackDir = Direction.SOUTH;
						game.player.attackStartTick = game.data.updateTick;
						game.player.canControl = false;
						game.player.attackSI.start();
					} else if (key == KeyEvent.VK_LEFT) {
						game.player.attackDir = Direction.WEST;
						game.player.attackStartTick = game.data.updateTick;
						game.player.canControl = false;
						game.player.attackWI.start();
					}
				}
			}
		}

		// pause
		if (key == KeyEvent.VK_ESCAPE) {
			if (game.data.status == GameState.PLAYING)
				game.data.status = GameState.PAUSED;
			else if (game.data.status == GameState.PAUSED)
				game.data.status = GameState.PLAYING;
		}

		// interact key
		else if (key == KeyEvent.VK_E && game.data.status == GameState.PLAYING)
			game.data.sceneActivate = true;

		boolean scenePress = false;

		// scene
		if (game.data.status == GameState.SCENE) {
			scenePress = true;
			if (game.data.currentScene.textCounter < game.data.currentScene.text.size() - 1) {
				game.data.currentScene.textCounter++;
			} else {
				game.data.currentScene.playing = false;
				game.data.currentScene.textCounter = 0;
				game.data.status = GameState.PLAYING;
			}
		}

		if (!scenePress) {
			// fail attack
			if (game.player.isAlive && game.player.canControl && game.data.status == GameState.PLAYING)
				if (selected == null || !(selected.ID == Item.SWORD_I || selected.ID == Item.SWORD_II || selected.ID == Item.SWORD_III))
					if (key == KeyEvent.VK_UP || key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_DOWN || key == KeyEvent.VK_LEFT) {
						game.data.status = GameState.SCENE;
						game.data.currentScene = new Scene(game, "You need to have a weapon equipped\nto do that.");
					}

			// reset
			// if (key == KeyEvent.VK_BACK_QUOTE)
			// game.reset();
		}
	}

	/**
	 * Occurs when a button is released.
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();

		if (game.player.isAlive) {
			if (key == KeyEvent.VK_W)
				game.player.movingNorth = false;
			else if (key == KeyEvent.VK_D)
				game.player.movingEast = false;
			else if (key == KeyEvent.VK_S)
				game.player.movingSouth = false;
			else if (key == KeyEvent.VK_A)
				game.player.movingWest = false;
		}
	}

	/**
	 * Occurs when a button is typed. (what does that even mean?)
	 */
	@Override
	public void keyTyped(KeyEvent e) {
		// nothin here
	}

	// -------------------------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------- MOUSE ---------------------------------------------------------
	// -------------------------------------------------------------------------------------------------------------------------

	// --------------------------------------------------------- CLICKS

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		allMouseEvents(e, Button.Status.PRESSED);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		allMouseEvents(e, Button.Status.RELEASED);
	}

	// --------------------------------------------------------- MOTION

	@Override
	public void mouseDragged(MouseEvent e) {
		allMouseEvents(e, Button.Status.PRESSED);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// System.out.println("Mouse detected");
		allMouseEvents(e, Button.Status.HOVERED);
	}

	/**
	 * Updates the buttons in the currently opened menu
	 * 
	 * @param e
	 * the MouseEvent that says where the mouse is
	 * @param s
	 * the status of the mouse (hovered?, pressed?, released?)
	 */
	public void allMouseEvents(MouseEvent e, Button.Status s) {
		switch (game.data.status) {
		case MAIN_MENU:
			if (game.running)
				game.main.updateButtons(e, s);
			break;
		case PAUSED:
			game.pause.updateButtons(e, s);
			break;
		case WIN:
			game.win.updateButtons(e, s);
			break;
		case LOSE:
			game.lose.updateButtons(e, s);
			break;
		case CONTROLS_MENU:
			game.controls.updateButtons(e, s);
			break;
		default:
			break;
		}
	}
}
