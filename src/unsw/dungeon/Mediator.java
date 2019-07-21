package unsw.dungeon;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.json.JSONObject;

import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

// The Mediator class is implemented as a Singleton Class because it coordinates the working of the rest of 
//the classes and exactly one object of it is required to be created at any given time.

public class Mediator {

	// List of entities collected by player - made 
	private List<Entity> collectedEntities = new LinkedList<>();
	
	private static Mediator mediator = new Mediator();
	
	
	private Mediator() {
	}
	public static Mediator getInstance() {
		return mediator;
	}

	private Dungeon dungeon;
	private GridPane squares;

	private List<ImageView> imageEntities;
	private boolean gameOver = false;
	private JSONObject goal;

	/**
	 * Getters for testability.
	 * 
	 * @return
	 */
	
	public void setDungeon(Dungeon dungeon, GridPane squares, List<ImageView> imageEntities , JSONObject goal) {
		this.dungeon = dungeon;
		this.squares = squares;
		this.imageEntities = imageEntities;
		this.goal = goal;
	}

	// To move from old coordinates to new coordinates
	public boolean moveTo(int currentX, int currentY, int newX, int newY) {

		if (gameOver) {
			return false;
		} 
		
		if (MediatorHelper.outsideDungeon(newX, newY)) {
			// Whether moving boulder or player, outside dungeon boundaries is prohibited.
			return false;
		}

		// At start, entity to move is the player
		Entity entityToMove = dungeon.getPlayer();

		List<Entity> fromEntities = MediatorHelper.getEntities(currentX, currentY);
		List<Entity> toEntities = MediatorHelper.getEntities(newX, newY);
		List<Entity> bouldersAtCurrent = MediatorHelper.getEntities(currentX, currentY, Boulder.class);
		List<Entity> enemies = MediatorHelper.getEntityOfType(EntityType.ENEMY);
		List<Entity> exitAtCurrent = MediatorHelper.getEntities(currentX , currentY , Exit.class);
		
		Random rand = new Random();
		
		if (!bouldersAtCurrent.isEmpty()) {
			// there is a boulder at currentX and currentY
			// We will move boulder instead of player
			entityToMove = bouldersAtCurrent.get(0);
		}

		if (entityToMove.isBlocked(toEntities)) {
			return false;
		}

		entityToMove.moveTo(newX, newY);
		
		if (!bouldersAtCurrent.isEmpty()) {
			// there is a boulder at currentX and currentY
			// We will move boulder instead of player
			entityToMove = bouldersAtCurrent.get(0);
		}

		// Calling entitesAtCurrent stepOver on a loop
		for (Entity entity : fromEntities) {
			if (entity.getType() != EntityType.SWITCH)
				entity.stepOver();
		}
		
		//Calling all enemies to move
		if(!enemies.isEmpty()) {
			for (Entity enemy : enemies) {
				((Enemy) enemy).moveTo(newX, newY);
			}
		}

		entityToMove.postMove(toEntities);

		return true;
	}

	// Method to mark end of game
	public void markGameOver() {
		System.out.println("Here");
		gameOver = true;
	}

	// Called when player presses the 'S' key on the keyboard
	public void swingSword(int x, int y) {
		System.out.println("Mediator: In swing sword");
		Entity sword = getCollected(EntityType.SWORD);
		if (sword != null) {
			if (((Sword) sword).swing()) {
				// Check if enemy is in vicinity
				List<Entity> enemies = MediatorHelper.entitiesInVicinity(x, y, EntityType.ENEMY);
				if (enemies != null) {
					// If true -> remove enemy
					// If false ->do nothing
					for (Entity enemy : enemies) {
						MediatorHelper.removeEntity(enemy);
					}
				}
			}
		}
	}

	// Called when player presses 'U' key on keyboard
	// Attempts to unlock the door at current location
	public void unlockDoor(int currentX, int currentY) {
		List<Entity> doors = MediatorHelper.doorInFront(currentX, currentY);
		if (!doors.isEmpty()) {
			Entity door = doors.get(0);
			door.stepOver();
		}
	}
	
	
	// Called when player presses the 'B' key on keyboard
	// Bomb timer is started
	public void igniteBomb(int x, int y) {
		System.out.println("Mediator: In ignite bomb");
		Entity old_bomb = getCollected(EntityType.BOMB);
		if (old_bomb != null) {
			Mediator.getInstance().collectedEntities.remove(old_bomb);
			Bomb new_bomb = spawnBombAtCurrentLocation(x, y);
			new_bomb.startBombSelfDestruct(1000);
		}
	}

	// Method to bring up a new bomb at (x,y) location
	private Bomb spawnBombAtCurrentLocation(int x, int y) {
		Bomb new_bomb = new Bomb(x, y);
		MediatorHelper.setupImage(new_bomb);
		return new_bomb;

	}

	// Returns true if the player already has newEntity in collected bag
	private boolean isCollected(Entity newEntity) {
		for (Entity collected : collectedEntities) {
			if (collected.getType() == newEntity.getType()) {
				return true;
			}
		}
		return false;
	}

	// Returns entity if the player already has an entity of given type in collected bag
	public Entity getCollected(EntityType entityType) {
		for (Entity collected : collectedEntities) {
			if (collected.getType() == entityType) {
				return collected;
			}
		}
		return null;
	}
	
	/**
	 * Getters for testability.
	 *
	 */
	public Dungeon getDungeon() {
		return dungeon;
	}

	public GridPane getSquares() {
		return squares;
	}

	public List<ImageView> getImageEntities() {
		return imageEntities;
	}
	
	public List<Entity> getCollectedEntities() {
		return collectedEntities;
	}
	
	public boolean getIsCollected(Entity newEntity) {
		return isCollected(newEntity);
	}

	public boolean getGameOver() {
		return gameOver;
	}

	public JSONObject getGoal() {
		return goal;
	}



}