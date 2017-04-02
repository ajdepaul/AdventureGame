package game.component.entities;

import game.Game;
import game.component.Inventory;
import game.component.Tile;
import game.component.Zone;
import game.component.entities.enemies.Enemy;
import game.component.entities.environment.Item;
import game.enums.Direction;
import game.enums.EntityState;
import game.enums.GameState;
import game.graphics.images.Animation;
import game.graphics.images.CustomAnimation;

import java.awt.Point;
import java.awt.geom.Point2D;

/**
 * The playable character of the game.
 * 
 * @author Anthony DePaul
 */
public class Player extends Entity {

	// --------------------------------------------------------- VARIABLE DECLARATIONS

	/** Stops control if the player is currently doing an action */
	public boolean canControl = true;

	/** Number of lives the player has */
	public int lives = 3;
	/** The last time the player died */
	public double lastDeathTick = -10000;
	/** Time it takes for the player to respawn */
	public int respawnDelay = 75;
	/** The zone the player came from */
	public int previousZone = 0;

	/** The damage values for the different swords */
	public int damageI = 15, damageII = 25, damageIII = 40;
	/** How far the player can reach with an attack */
	public double reach = 30;
	/** The direction the player is attacking in */
	public Direction attackDir;
	/** The duration of the attack */
	public int attackDur = 5;
	/** When the attack started */
	public long attackStartTick = -100;

	/** The animation for jumping east */
	public CustomAnimation jumpE;
	/** The animation for jumping south */
	public CustomAnimation jumpS;
	/** The animation for jumping west */
	public CustomAnimation jumpW;
	/** The animation for jumping north */
	public CustomAnimation jumpN;

	/** The animation for walking east */
	private CustomAnimation walkE;
	/** The animation for walking south */
	private CustomAnimation walkS;
	/** The animation for walking west */
	private CustomAnimation walkW;
	/** The animation for walking north */
	private CustomAnimation walkN;

	/** The animations for tier I attacking */
	public CustomAnimation attackNI, attackEI, attackSI, attackWI;
	/** The animations for tier II attacking */
	public CustomAnimation attackNII, attackEII, attackSII, attackWII;
	/** The animations for tier III attacking */
	public CustomAnimation attackNIII, attackEIII, attackSIII, attackWIII;

	/** The duration the player stays in the air for a jump */
	public double jumpDuration = 20;
	/** How fast the player jumps */
	public double jumpSpeed = speed;
	/** The tick when the player started the jump */
	public long jumpStartTick = -100;

	/** The last Enemy to hit the player */
	private Enemy lastHit;

	/** The current inventory of the player */
	public Inventory inventory;
	/** The current money of the player */
	public int money = 0;

	// --------------------------------------------------------- CONSTRUCTORS

	/**
	 * Creates the initial player.
	 * 
	 * @param game
	 * the game to the player is in
	 */
	public Player(Game g) {
		game = g;
		inventory = new Inventory();

		// //////////////////////// REMOVE THIS

		// inventory.pickUp(new Item(game, 1, 1, Item.SWORD_III));
		// inventory.pickUp(new Item(game, 1, 1, Item.KEY_I));
		// inventory.pickUp(new Item(game, 1, 1, Item.KEY_II));
		// inventory.pickUp(new Item(game, 1, 1, Item.KEY_III));
		// money = 1000;

		// //////////////////////// REMOVE THIS

		jumpN = game.images.playerJumpN;
		jumpE = game.images.playerJumpE;
		jumpS = game.images.playerJumpS;
		jumpW = game.images.playerJumpW;
		walkN = game.images.playerWalkN;
		walkE = game.images.playerWalkE;
		walkS = game.images.playerWalkS;
		walkW = game.images.playerWalkW;
		attackNI = game.images.playerAttackNI;
		attackEI = game.images.playerAttackEI;
		attackSI = game.images.playerAttackSI;
		attackWI = game.images.playerAttackWI;
		attackNII = game.images.playerAttackNII;
		attackEII = game.images.playerAttackEII;
		attackSII = game.images.playerAttackSII;
		attackWII = game.images.playerAttackWII;
		attackNIII = game.images.playerAttackNIII;
		attackEIII = game.images.playerAttackEIII;
		attackSIII = game.images.playerAttackSIII;
		attackWIII = game.images.playerAttackWIII;

		Zone temp = game.data.currentZone;
		Point2D.Double thing = temp.spawns.get(0);
		loc.setLocation(thing);
		damage = 20;
		images = game.images.player;
		updateImage();
		hp = maxhp;
	}

	// --------------------------------------------------------- IMAGE

	/**
	 * Creates the image for the player based off its current situation.
	 */
	@Override
	public void updateImage() {
		// RIP //
		if (!isAlive) {
			currentImage = images.grabImage(4, 0, 1, 2);
			return;
		}
		// attacking //
		if (game.data.updateTick - attackStartTick <= attackDur) {
			Animation currentAttackN = null, currentAttackE = null, currentAttackS = null, currentAttackW = null;
			// get animation
			switch (game.player.inventory.getSelected().ID) {
			case Item.SWORD_I:
				currentAttackN = game.images.playerAttackNI;
				currentAttackE = game.images.playerAttackEI;
				currentAttackS = game.images.playerAttackSI;
				currentAttackW = game.images.playerAttackWI;
				break;
			case Item.SWORD_II:
				currentAttackN = game.images.playerAttackNII;
				currentAttackE = game.images.playerAttackEII;
				currentAttackS = game.images.playerAttackSII;
				currentAttackW = game.images.playerAttackWII;
				break;
			case Item.SWORD_III:
				currentAttackN = game.images.playerAttackNIII;
				currentAttackE = game.images.playerAttackEIII;
				currentAttackS = game.images.playerAttackSIII;
				currentAttackW = game.images.playerAttackWIII;
				break;
			}
			// set animation
			switch (attackDir) {
			case NORTH:
				currentImage = currentAttackN.getImage();
				break;
			case EAST:
				currentImage = currentAttackE.getImage();
				break;
			case SOUTH:
				currentImage = currentAttackS.getImage();
				break;
			case WEST:
				currentImage = currentAttackW.getImage();
				break;
			}
		} else
			switch (direction) {
			case SOUTH:
				// walking
				if (status == EntityState.GROUND && movingSouth) {
					currentImage = walkS.getImage();
					break;
				}
				// jumping
				if (status == EntityState.AIR) {
					currentImage = jumpS.getImage();
					break;
				}
				currentImage = images.grabImage(2, 0, 1, 2);
				break;
			case EAST:
				// walking
				if (status == EntityState.GROUND && movingEast) {
					currentImage = walkE.getImage();
					break;
				}
				// jumping
				if (status == EntityState.AIR) {
					currentImage = jumpE.getImage();
					break;
				}
				currentImage = images.grabImage(1, 0, 1, 2);
				break;
			case NORTH:
				// walking
				if (status == EntityState.GROUND && movingNorth) {
					currentImage = walkN.getImage();
					break;
				}
				// jumping
				if (status == EntityState.AIR) {
					currentImage = jumpN.getImage();
					break;
				}
				currentImage = images.grabImage(0, 0, 1, 2);
				break;
			case WEST:
				// walking
				if (status == EntityState.GROUND && movingWest) {
					currentImage = walkW.getImage();
					break;
				}
				// jumping
				if (status == EntityState.AIR) {
					currentImage = jumpW.getImage();
					break;
				}
				currentImage = images.grabImage(3, 0, 1, 2);
				break;
			}
		super.updateImage();
	}

	// --------------------------------------------------------- UPDATE

	/**
	 * Updates player status based off other its situation.
	 */
	@Override
	public void update() {
		// pits & death //
		super.update();

		canControl = true;
		if (isAlive) {
			// attack //
			if (game.data.updateTick - attackStartTick > attackDur)
				attackDir = null;
			if (game.data.updateTick - attackStartTick <= attackDur)
				canControl = false;
			// jumps //
			if (game.data.updateTick - jumpStartTick < jumpDuration) {
				canControl = false;
				game.player.status = EntityState.AIR;
				switch (direction) {
				case NORTH:
					for (int i = 1; i <= jumpSpeed; i++)
						move(0, -1);
					break;
				case EAST:
					for (int i = 1; i <= jumpSpeed; i++)
						move(1, 0);
					break;
				case SOUTH:
					for (int i = 1; i <= jumpSpeed; i++)
						move(0, 1);
					break;
				case WEST:
					for (int i = 1; i <= jumpSpeed; i++)
						move(-1, 0);
					break;
				}
			}
			// end jump
			if (game.data.updateTick - jumpStartTick >= jumpDuration)
				status = EntityState.GROUND;
			// locks //
			lockInfo lock = touchingLock();
			Item selected = inventory.getSelected();
			if (lock != null && selected != null) {
				if (lock.type == Tile.Type.LOCK_I && selected.ID == Item.KEY_I) {
					game.data.currentZone.layout[lock.y][lock.x].openLock();
					inventory.removeSelected();
				}
				if (lock.type == Tile.Type.LOCK_II && selected.ID == Item.KEY_II) {
					game.data.currentZone.layout[lock.y][lock.x].openLock();
					inventory.removeSelected();
				}
				if (lock.type == Tile.Type.LOCK_III && selected.ID == Item.KEY_III) {
					game.data.currentZone.layout[lock.y][lock.x].openLock();
					inventory.removeSelected();
				}
			}
			// teleport //
			if (game.data.currentZone.layout[tilePos.y][tilePos.x].type == Tile.Type.TELEPORT)
				game.data.playerTeleport(game.data.currentZone.layout[tilePos.y][tilePos.x]);
			// hit //
			if (game.data.currentZone.entities.size() != 0) {
				for (Entity e : game.data.currentZone.entities) {
					if (e instanceof Enemy) {
						Enemy e2 = (Enemy) e;
						double distanceFromEnemy = Math.sqrt((e2.loc.x - loc.x) * (e2.loc.x - loc.x) + (e2.loc.y - loc.y) * (e2.loc.y - loc.y));
						if (distanceFromEnemy <= size / 2 + e2.size / 2 && game.data.updateTick - startHitTick >= hitDelay && e2.isAlive) {
							hp -= e2.damage;
							startHitTick = game.data.updateTick;
							lastHit = e2;
						}
					}
				}
				// knock back
				if (game.data.updateTick - startHitTick <= hitDelay) {
					// remove controls and cancel jumps
					canControl = false;
					jumpStartTick = -100;
					game.player.status = EntityState.GROUND;
					// movement values
					double x = loc.x - lastHit.loc.x;
					double y = loc.y - lastHit.loc.y;
					double angle = Math.abs(Math.atan(y / x));
					double movex = Math.cos(angle) * speed;
					double movey = Math.sin(angle) * speed;
					// knock back
					if (loc.x - lastHit.loc.x <= 0 && loc.y - lastHit.loc.y <= 0) // down right
						move(-movex, -movey);
					else if (loc.x - lastHit.loc.x >= 0 && loc.y - lastHit.loc.y <= 0) // down left
						move(movex, -movey);
					else if (loc.x - lastHit.loc.x <= 0 && loc.y - lastHit.loc.y >= 0) // up right
						move(-movex, movey);
					else if (loc.x - lastHit.loc.x >= 0 && loc.y - lastHit.loc.y >= 0) // up left
						move(movex, movey);
				}
			}
			// movement //
			if (canControl) {
				double angleSpeed = Math.sin(Math.toRadians(45));

				if (movingNorth && movingWest) {
					for (int i = 1; i <= speed; i++)
						move(-angleSpeed, -angleSpeed);
					direction = Direction.WEST;
				} else if (movingNorth && movingEast) {
					for (int i = 1; i <= speed; i++)
						move(angleSpeed, -angleSpeed);
					direction = Direction.EAST;
				} else if (movingSouth && movingWest) {
					for (int i = 1; i <= speed; i++)
						move(-angleSpeed, angleSpeed);
					direction = Direction.WEST;
				} else if (movingSouth && movingEast) {
					for (int i = 1; i <= speed; i++)
						move(angleSpeed, angleSpeed);
					direction = Direction.EAST;
				}

				else if (movingNorth) {
					for (int i = 1; i <= speed; i++)
						move(0, -1);
					direction = Direction.NORTH;
				} else if (movingSouth) {
					for (int i = 1; i <= speed; i++)
						move(0, 1);
					direction = Direction.SOUTH;
				} else if (movingEast) {
					for (int i = 1; i <= speed; i++)
						move(1, 0);
					direction = Direction.EAST;
				} else if (movingWest) {
					for (int i = 1; i <= speed; i++)
						move(-1, 0);
					direction = Direction.WEST;
				}
			}

			if (game.player.inventory.getSelected() != null) {
				switch (game.player.inventory.getSelected().ID) {
				case Item.SWORD_I:
					damage = damageI;
					break;
				case Item.SWORD_II:
					damage = damageII;
					break;
				case Item.SWORD_III:
					damage = damageIII;
					break;
				}
			}
		} else if (lives >= 0 && lastDeathTick == -10000) {
			lastDeathTick = game.data.updateTick;
		} else if (lives >= 0 && respawnDelay <= game.data.updateTick - lastDeathTick) {
			// tp //
			// custom spawn
			if (game.data.zones.get(game.data.currentZoneNumber).spawns.get(game.player.previousZone + 1) != null)
				game.player.loc.setLocation(game.data.zones.get(game.data.currentZoneNumber).spawns.get(game.player.previousZone + 1));
			// default spawn
			else
				game.player.loc.setLocation(game.data.zones.get(game.data.currentZoneNumber).spawns.get(0));
			// reset //
			isAlive = true;
			hp = maxhp;
			lives--;
			lastDeathTick = -10000;
			movingNorth = false;
			movingEast = false;
			movingSouth = false;
			movingWest = false;
		} else if (lives <= 0) {
			game.data.status = GameState.LOSE;
		}
	}

	/**
	 * Return the type of lock if colliding with one
	 * 
	 * @return a string with the type and location
	 */
	private lockInfo touchingLock() {
		Point NP = new Point((int) (loc.x), (int) (loc.y - size / 2));
		Tile NT = game.data.currentZone.layout[(NP.y - 1) / game.data.TILE_RES][NP.x / game.data.TILE_RES];
		Point EP = new Point((int) (loc.x + size / 2), (int) (loc.y));
		Tile ET = game.data.currentZone.layout[EP.y / game.data.TILE_RES][(EP.x + 1) / game.data.TILE_RES];
		Point SP = new Point((int) (loc.x), (int) (loc.y + size / 2));
		Tile ST = game.data.currentZone.layout[(SP.y + 1) / game.data.TILE_RES][SP.x / game.data.TILE_RES];
		Point WP = new Point((int) (loc.x - size / 2), (int) (loc.y));
		Tile WT = game.data.currentZone.layout[WP.y / game.data.TILE_RES][(WP.x - 1) / game.data.TILE_RES];

		if (NT.isCollidable)
			if (NT.type == Tile.Type.LOCK_I || NT.type == Tile.Type.LOCK_II || NT.type == Tile.Type.LOCK_III)
				return new lockInfo(NP.x / game.data.TILE_RES, (NP.y - 1) / game.data.TILE_RES, NT.type);
		if (ET.isCollidable)
			if (ET.type == Tile.Type.LOCK_I || ET.type == Tile.Type.LOCK_II || ET.type == Tile.Type.LOCK_III)
				return new lockInfo((EP.x + 1) / game.data.TILE_RES, EP.y / game.data.TILE_RES, ET.type);
		if (ST.isCollidable)
			if (ST.type == Tile.Type.LOCK_I || ST.type == Tile.Type.LOCK_II || ST.type == Tile.Type.LOCK_III)
				return new lockInfo(SP.x / game.data.TILE_RES, (SP.y + 1) / game.data.TILE_RES, ST.type);
		if (WT.isCollidable)
			if (WT.type == Tile.Type.LOCK_I || WT.type == Tile.Type.LOCK_II || WT.type == Tile.Type.LOCK_III)
				return new lockInfo((WP.x - 1) / game.data.TILE_RES, WP.y / game.data.TILE_RES, WT.type);
		return null;
	}

	private class lockInfo {
		public int x, y;
		public Tile.Type type;

		public lockInfo(int x, int y, Tile.Type type) {
			this.x = x;
			this.y = y;
			this.type = type;
		}
	}
}
