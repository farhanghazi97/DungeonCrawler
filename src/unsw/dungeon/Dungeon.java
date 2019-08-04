package unsw.dungeon;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.json.JSONObject;
import javafx.scene.image.ImageView;

/**
 * A dungeon in the interactive dungeon player.
 *
 * A dungeon can contain many entities, each occupy a square. More than one
 * entity can occupy the same square.
 *
 * Dungeon Design Consideration:
 * -> The dungeon class contains a DungeonController object and the DungeonController object contains a dungeon.
 * In this case, the DungeonController object dc has a owning reference to dungeon while Dungeon has a non-owning
 * reference to the Controller. If the dungeon is destroyed, the DungeonController still exists.
 *
 * @author Robert Clifton-Everest
 *
 */
public class Dungeon {

	private int width, height;
	private Player player;
	private DungeonController dc;
	private JSONObject goal;
	private Instant gameStart;
	private Instant gameFinish;
	private boolean gameOver = false;
	
	private List<Entity> playerInventory = new LinkedList<>();
	private List<Entity> entities;
	
	public Dungeon(int width, int height, JSONObject goal) {
		this.width = width;
		this.height = height;
		this.entities = new ArrayList<>();
		this.player = null;
		this.goal = goal;
		gameStart = Instant.now();
	}

	/**
	 * Method to move an entity from current coordinates (currentX, currentY) to new coordinates (newX, newY).
	 *
	 * The local variable entityToMove determines which entity should move. By default, entityToMove is set as the player.
	 * If player wants to move a boulder, then entityToMove is set as Boulder.
	 * @param currentX
	 * @param currentY
	 * @param newX
	 * @param newY
	 * @return true if player succesfully moved to new location and false otherwise
	 */
	public boolean moveTo(int currentX, int currentY, int newX, int newY) {
		if (gameOver) {
			return false;
		}

		if (outsideDungeon(newX, newY)) {
			return false;
		}

		// At start, entity to move is the player
		Entity entityToMove = player;

		List<Entity> fromEntities = getEntities(currentX, currentY);
		List<Entity> toEntities = getEntities(newX, newY);
		List<Entity> bouldersAtCurrent = getEntities(currentX, currentY, Boulder.class);
		List<Entity> enemies = getEntities(EntityType.ENEMY);

		if (!bouldersAtCurrent.isEmpty()) {
			// there is a boulder at currentX and currentY
			// We will move boulder instead of player
			entityToMove = bouldersAtCurrent.get(0);
		}

		if (entityToMove.isBlocked(toEntities)) {
			return false;
		}

		entityToMove.moveTo(newX, newY);

		// Calling entitesAtCurrent stepOver on a loop
		for (Entity entity : fromEntities) {
			//Do not trigger if player steps on switch
			if (entity.getType() != EntityType.SWITCH) entity.stepOver();
		}

		// Calling all enemies to move
		if (!enemies.isEmpty()) {
			for (Entity enemy : enemies) {
				((Enemy) enemy).moveTo(newX, newY);
			}
		}
		entityToMove.postMove(toEntities);
		return true;
	}

	/**
	 * Method to handle things after game is over.
	 * Stops the timer and displays winner/loser message depending on the scenario
	 */
	public void postGameOver() {
		if (isGameOver()) {
			gameFinish = Instant.now();
			long timeElapsed = Duration.between(gameStart, gameFinish).getSeconds();
			List<Entity> exits = getEntities(EntityType.EXIT);
			Exit exit = (Exit) exits.get(0);
			if (player.getX() == exit.getX() && player.getY() == exit.getY()) {
				dc.showMessageBox("You've beat the game!\n Time Taken (sec) : " + timeElapsed, "Congratulations!", null);
			} else {
				dc.showMessageBox("The developers have beat you!\n GamePlay time (sec) : " + timeElapsed, "Sorry!", null);
			}
		}

	}

	/**
	 * Method to check if a given coordinate value is outside the boundaries of the dungeon
	 * @param x
	 * @param y
	 * @return true if (x,y) is outside dungeon and false otherwise
	 */
    public boolean outsideDungeon(int x, int y) {
        if(x == -1|| y == -1)  return true;
        if (x + 1 > width || y + 1 > height)  return true;
        return false;
    }

	/**
	 * Method to set the difficulty of the game. Takes in integer values of 1,2 or 3 as input parameters.
	 * @param difficulty
	 */
	public void setDifficulty(int difficulty) {
    	List<Entity> enemies = getEntities(EntityType.ENEMY);
    	if(enemies.size() > 0) {
    		for(Entity enemy: enemies) {
    			((Enemy)enemy).setDifficultyLevel(difficulty);
    		}
    	}
    }

	/**
	 * Method called when player presses 'U' key on keyboard
	 * Attempts to unlock the door at current location (currentX, currentY)
 	 */
	public void handleKeyPressU(int currentX, int currentY) {
		List<Entity> doors = entitiesInFront(currentX, currentY, EntityType.DOOR);
		if (!doors.isEmpty()) {
			System.out.println("Found door in vicnity");
			Entity door = doors.get(0);
			door.stepOver();
		}
	}

	/**
	 * Method called when player presses 'S' key on keyboard
	 * Attempts to swing a sword
	 * @param x
	 * @param y
	 */
	public void handleKeyPressS(int x, int y) {
		System.out.println("Dungeon: In handleKeyPressS");
		Entity sword = getInventoryEntity(EntityType.SWORD);
		if (sword != null)  ((Sword) sword).swing(x,y);
	}

	/**
	 * Method called when player presses 'B' key on keyboard
	 * Drops a bomb entity at location (x,y)
	 * @param x
	 * @param y
	 */
	public void handleKeyPressB(int x, int y) {
		System.out.println("Dungeon: In ignite bomb");
		Entity oldBomb = getInventoryEntity(EntityType.BOMB);
		if (oldBomb != null) {
			playerInventory.remove(oldBomb);
			Bomb newBomb = new Bomb(this,x,y);
			dc.generateImage(newBomb);
			newBomb.startBombSelfDestruct(1000);
		}
	}

	/**
	 * Method to get all entities of a given type, which are directly adjacent to location (x,y)
	 * @param x
	 * @param y
	 * @param type
	 * @return List</Entity> is a list of all entities found adjacent to (x,y)
	 */
	public List<Entity> entitiesInVicinity(int x, int y, EntityType... type) {
		List<Entity> list = new LinkedList<>();
		for (EntityType aType : type) {
			for (Entity entity : this.getEntities()) {
				if (entity.getType() == aType && ((entity.getX() == x + 1 && entity.getY() == y)
						|| (entity.getX() == x + 1 && entity.getY() == y - 1)
						|| (entity.getX() == x + 1 && entity.getY() == y + 1)
						|| (entity.getX() == x && entity.getY() == y + 1)
						|| (entity.getX() == x && entity.getY() == y - 1)
						|| (entity.getX() == x - 1 && entity.getY() == y)
						|| (entity.getX() == x - 1 && entity.getY() == y - 1)
						|| (entity.getX() == x + 1 && entity.getY() == y + 1)
						|| (entity.getX() == x && entity.getY() == y))) {
					list.add(entity);
				}
			}
		}
		return list;
	}

	/**
	 * Method to get all entities of a given type, exactly one square above or one square below the location (x,y)
	 * @param x
	 * @param y
	 * @param type
	 * @return  List</Entity> is a list of all entities found directly above or below (x,y)
	 */
	public List<Entity> entitiesInFront(int x, int y, EntityType type) {
		List<Entity> list = new LinkedList<>();
        for (Entity entity : entities) {
            if (entity.getType() == type && ((entity.getY() == y - 1 && entity.getX() == x))) {
                    list.add(entity);
            }
        }
        return list;
	}

	/**
	 * Method to remove a given entity by:
	 * 1) Removing from the dungeon's list of entities
	 * 2) Removing entity image from the frontend
	 * @param entity
	 */
	public void removeEntity(Entity entity) {
		dc.removeEntity(entity);
		if (entities.contains(entity)) {
			entities.remove(entity);
		}
	}

	//Getters for entities
	/**
	 * Getter for entities array
	 * @return
	 */
	public List<Entity> getEntities() {
		return entities;
	}

	/**
	 * Method to get entities at location(x,y)
	 * @param x
	 * @param y
	 * @return list of entites at (x,y)
	 */
	public List<Entity> getEntities(int x, int y) {
		List<Entity> list = new LinkedList<>();
		for (Entity entity : entities) {
			if (entity.getX() == x && entity.getY() == y) {
				list.add(entity);
			}
		}
		return list;
	}

	/**
	 * Method to get entities of class type clazz at location(x,y)
	 * @param x
	 * @param y
	 * @param clazz
	 * @return returns a list of entities of class type clazz at location(x,y)
	 */
	public List<Entity> getEntities(int x, int y, Class clazz) {
		List<Entity> list = new LinkedList<>();
		for (Entity entity : entities) {
			if (entity.getX() == x && entity.getY() == y && clazz.isInstance(entity)) {
				list.add(entity);
			}
		}
		return list;
	}


	/**
	 * Method to get all entities of EntityType entityType
	 * @param entityType
	 * @return returns a list of entities of EntityType entityType
	 */
	public List<Entity> getEntities(EntityType entityType) {
		List<Entity> list = new LinkedList<>();
		for (Entity entity : entities) {
			if (entity.getType() == entityType) {
				list.add(entity);
			}
		}
		return list;
	}
	
	//Getters for inventory

	/**
	 * Getter for entites that player has collected
	 * @return List</Entity>
	 */
	public List<Entity> getInventoryEntities() {
		return playerInventory;
	}

	/**
	 * Getter for entities of EntityType entityType that player has collected
	 * @param entityType
	 * @return List</Entity>
	 */
	public Entity getInventoryEntity(EntityType entityType) {
		for (Entity collected : playerInventory) {
			if (collected.getType() == entityType) {
				return collected;
			}
		}
		return null;
	}

	/**
	 * To add an entity to player's inventory bag (.i.e. entities collected by player)
	 * @param entity
	 */
	public void addInventory(Entity entity) {
		playerInventory.add(entity);
	}

	/**
	 * Method that uses DungeonController's getter to get Images for a given entity
	 * @param entity
	 * @return returns an ImageView image of given entity
	 */
	public ImageView getImageByEntity( Entity entity) {
		return dc.getImageByEntity(entity);
	}

	/**
	 * Method that uses DungeonController's getUniqueMazeCoordinates to get a location (x,y)
	 * This method ensures that the location it returns is not occupied by another entity
	 * @return location (x,y) of type Pair
	 */
	public Pair getUniqueCoordinates() {
		Pair coordinates = null;
		while (coordinates == null) {
			coordinates = dc.getUniqueMazeCoordinates();
		}
		return coordinates;
	}

	/**
	 * Method that uses DungeonController's generateEntity function
	 * @param entity
	 */
	public void generateEntity(Entity entity) {
		dc.generateImage(entity);
	}

	//Getters and setters for some dungeon fields
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Player getPlayer() {
		return player;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}


	public void setDungeonController(DungeonController dc) {
		this.dc = dc;
	}
	
	public void addEntity(Entity entity) {
		entities.add(entity);
	}

	public JSONObject getGoal() {
		return goal;
	}

	public boolean isGameOver() {
		return gameOver;
	}

	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
		postGameOver();
	}
}
