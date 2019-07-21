package unsw.dungeon;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javafx.concurrent.Task;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

//Singleton class
public class Mediator {

	// List of entities collected by player
	public List<Entity> collectedEntities = new LinkedList<>();

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

	/**
	 * Getters for testability.
	 * 
	 * @return
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

	public boolean getGameOver() {
		return gameOver;
	}

	public void setDungeon(Dungeon dungeon, GridPane squares, List<ImageView> imageEntities) {
		this.dungeon = dungeon;
		this.squares = squares;
		this.imageEntities = imageEntities;
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
		System.out.println(enemies);
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
			// Activating switch, if it exists
			List<Entity> switchAtCurrent = MediatorHelper.getEntities(currentX, currentY, Switch.class);
			if (!switchAtCurrent.isEmpty()) {
				Entity swi = switchAtCurrent.get(0);
				if (rand.nextInt(3) == 1)
					swi.stepOver();
			}
		}

		// Calling entitesAtCurrent stepOver on a loop
		for (Entity entity : fromEntities) {
			if (entity.getType() != EntityType.SWITCH)
				entity.stepOver();
		}
		
		for (Entity enemy : enemies) {
			((Enemy) enemy).moveTo(newX, newY);
		}

		entityToMove.postMove(toEntities);

		return true;
	}

	// Method to mark end of game
	public void markGameOver() {
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
	
	
	//THE BOMB CODE IS MESSY
	// Called when player presses the 'B' key on keyboard
	// Bomb timer begins
	public void igniteBomb(int x, int y) {
		System.out.println("Mediator: In ignite bomb");
		Entity old_bomb = getCollected(EntityType.BOMB);
		if (old_bomb != null) {
			Mediator.getInstance().collectedEntities.remove(old_bomb);
			Bomb new_bomb = spawnBombAtCurrentLocation(x, y);
			new_bomb.startBombSelfDestruct(1000);
		}
	}

	// Manages the UI for changing bomb image
	private Bomb spawnBombAtCurrentLocation(int x, int y) {

		Bomb new_bomb = new Bomb(x, y);

		/*
		 * Commented this section out for backend testing
		 */

		MediatorHelper.setupImage(new_bomb);
		return new_bomb;

	}

	// Returns true if the player already has newEntity in collected
	public boolean isCollected(Entity newEntity) {
		for (Entity collected : collectedEntities) {
			if (collected.getType() == newEntity.getType()) {
				return true;
			}
		}
		return false;
	}

	// Returns entity if the player already has an entity of given type
	public Entity getCollected(EntityType entityType) {
		for (Entity collected : collectedEntities) {
			if (collected.getType() == entityType) {
				return collected;
			}
		}
		return null;
	}




}