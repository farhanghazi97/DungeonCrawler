package unsw.dungeon;

import java.util.ArrayList;
import java.util.HashSet;
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

		if (outsideDungeon(newX, newY)) {
			// Whether moving boulder or player, outside dungeon boundaries is prohibited.
			return false;
		}

		// At start, entity to move is the player
		Entity entityToMove = dungeon.getPlayer();
		List<Entity> entitiesAtCurrent = getEntities(currentX, currentY);
		List<Entity> entitiesAtNew = getEntities(newX, newY);
		List<Entity> bouldersAtCurrent = getEntities(currentX, currentY, Boulder.class);

		Random rand = new Random();

		if (!bouldersAtCurrent.isEmpty()) {
			// there is a boulder at currentX and currentY
			// We will move boulder instead of player
			entityToMove = bouldersAtCurrent.get(0);
		}

		if (entityToMove.isBlocked(entitiesAtNew)) {
			return false;
		}

		entityToMove.moveTo(newX, newY);

		if (!bouldersAtCurrent.isEmpty()) {
			// there is a boulder at currentX and currentY
			// We will move boulder instead of player
			entityToMove = bouldersAtCurrent.get(0);
			// Activating switch, if it exists
			List<Entity> switchAtCurrent = getEntities(currentX, currentY, Switch.class);
			if (!switchAtCurrent.isEmpty()) {
				Entity swi = switchAtCurrent.get(0);
				if (rand.nextInt(3) == 1)
					swi.stepOver();
			}
		}

		// Calling entitesAtCurrent stepOver on a loop
		for (Entity entity : entitiesAtCurrent) {
			if (entity.getType() != EntityType.SWITCH)
				entity.stepOver();
		}

		entityToMove.postMove(entitiesAtNew);

		return true;
	}

	// HELPER OPERATIONS BELOW

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
				List<Entity> enemies = entitiesInVicinity(x, y, EntityType.ENEMY);
				if (enemies != null) {
					// If true -> remove enemy
					// If false ->do nothing
					for (Entity enemy : enemies) {
						removeEntity(enemy);
					}
				}
			}
		}
	}
	

	// Called when player presses 'U' key on keyboard
	// Attempts to unlock the door at current location
	public void unlockDoor(int currentX, int currentY) {
		List<Entity> doors = doorInVicinity(currentX, currentY);
		if (!doors.isEmpty()) {
			Entity door = doors.get(0);
			door.stepOver();
		}
	}
	
	
	// Called when player presses the 'B' key on keyboard
	// Bomb timer begins 
	public void igniteBomb(int x, int y) {
		System.out.println("Mediator: In ignite bomb");
		Entity old_bomb = getCollected(EntityType.BOMB);
		if (old_bomb != null) {
			Mediator.getInstance().collectedEntities.remove(old_bomb);
			Entity new_bomb = spawnBombAtCurrentLocation(x, y);
			startBombSelfDestruct(new_bomb, 1000);
		}
	}


	//Manages the UI for changing bomb image
	private Entity spawnBombAtCurrentLocation(int x, int y) {

		Entity new_bomb = new Bomb(x, y);

		/*
		 * Commented this section out for backend testing
		 */

		Image new_image = new Image(new_bomb.getImagePath());
		ImageView new_view = new ImageView(new_image);
		new_view.setId(new_bomb.getImageID());
		GridPane.setColumnIndex(new_view, new_bomb.getX());
		GridPane.setRowIndex(new_view, new_bomb.getY());
		imageEntities.add(new_view);
		squares.getChildren().add(new_view);
		return new_bomb;

	}

	private void startBombSelfDestruct(Entity new_bomb, long time) {

		ArrayList<String> images = new_bomb.getImage_list();

		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() {
				ImageView imageToUpdate = getImageByEntity(imageEntities, new_bomb);
				if (imageToUpdate != null) {
					for (int j = 0; j < images.size(); j++) {
						try {
							Thread.sleep(time);
						} catch (InterruptedException e) {
							System.out.println("Thread was interrupted!");
						}
						Image new_state = new Image(images.get(j));
						imageToUpdate.setImage(new_state);
						System.out.println("Bomb image updated");
					}
				}
				return null;
			}
		};

		task.setOnSucceeded(e -> {
			// Grab all (in any) enemies , boulders , player in 3 X 3 area surrounding
			// bomb's location
			List<Entity> entities_to_remove = getEntitiesToRemove(new_bomb.getX(), new_bomb.getY(), 
					EntityType.ENEMY, EntityType.BOULDER, EntityType.PLAYER);
			if (entities_to_remove.contains(dungeon.getPlayer())) {
				// If player is in blast radius of bomb, end game
				Entity potion = this.getCollected(EntityType.POTION);
				// If player does not have potion, bomb effective
				if(potion == null) {
					this.markGameOver();
				} else {
				// If player havs potion, bomb ineffective
					entities_to_remove.remove(dungeon.getPlayer());
				}
			}
			if (!entities_to_remove.isEmpty()) {
				for (int i = 0; i < entities_to_remove.size(); i++) {
					Entity enemy = entities_to_remove.get(i);
					this.removeEntity(enemy);
				}
			}
			removeEntity(new_bomb);
			System.out.println("Bomb removed");
		});

		new Thread(task).start();
	}

	private ImageView getImageByEntity(List<ImageView> entities, Entity e) {
		ImageView image = new ImageView();
		for (int i = 0; i < entities.size(); i++) {
			image = entities.get(i);
			if (GridPane.getColumnIndex(image) == e.getX() && GridPane.getRowIndex(image) == e.getY()) {
				if (image.getId().equals(e.getImageID())) {
					break;
				}
			}
		}
		return image;
	}

	// Returns true if the new coordinates given are outside
	// the boundaries of the dungeon
	private boolean outsideDungeon(int newX, int newY) {
		int width = dungeon.getWidth();
		int height = dungeon.getHeight();
		if (newX + 1 > width || newY + 1 > height) {
			return true;
		}
		return false;
	}

	// Returns all entities on (x,y) coordinates

	private List<Entity> getEntities(int x, int y) {
		List<Entity> list = new LinkedList<>();
		for (Entity entity : dungeon.getEntities()) {
			if (entity.getX() == x && entity.getY() == y) {
				list.add(entity);
			}
		}
		return list;
	}

	// Returns a specific type of entity class if it at a given (x,y) coordinate
	// Made public so it can be used in the test file
	public List<Entity> getEntities(int x, int y, Class clazz) {
		List<Entity> list = new LinkedList<>();
		for (Entity entity : dungeon.getEntities()) {
			if (entity.getX() == x && entity.getY() == y && clazz.isInstance(entity)) {
				System.out.println("Adding to list");
				list.add(entity);
			}
		}
		return list;
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


	// Returns a list of entities of type "type" if they are in adjacent squares
	private List<Entity> entitiesInVicinity(int x, int y, EntityType type) {
		// System.out.println("Inside enemiesInVicinity");
		List<Entity> list = new LinkedList<>();

		for (Entity entity : dungeon.getEntities()) {
			if (entity.getType() == type && ((entity.getX() == x + 1 && entity.getY() == y)
					|| (entity.getX() == x + 1 && entity.getY() == y - 1)
					|| (entity.getX() == x + 1 && entity.getY() == y + 1)
					|| (entity.getX() == x && entity.getY() == y + 1) || (entity.getX() == x && entity.getY() == y - 1)
					|| (entity.getX() == x - 1 && entity.getY() == y)
					|| (entity.getX() == x - 1 && entity.getY() == y - 1)
					|| (entity.getX() == x + 1 && entity.getY() == y + 1))) {
				list.add(entity);
			}
		}
		return list;
	}

	// Checks if there is a door in front of player
	public List<Entity> doorInVicinity(int x, int y) {
		List<Entity> list = new LinkedList<>();
		for (Entity entity : dungeon.getEntities()) {
			if (entity.getType() == EntityType.DOOR) {
				if (entity.getY() == y - 1 && entity.getX() == x) {
					list.add(entity);
				}
			}
		}
		return list;
	}
	// UI FUNCTIONS
	// Removes UI element and object corresponding to given entity
	public void removeEntity(Entity entity) {
		System.out.println("In remove entity function " + entity);
		for (int i = 0; i < imageEntities.size(); i++) {
			ImageView image = imageEntities.get(i);
			// Map GridPane co-ords to entity co-ords
			if (GridPane.getColumnIndex(image) == entity.getX() && GridPane.getRowIndex(image) == entity.getY()) {
				if (image.getId().equals(entity.getImageID())) {
					// Removing from screen
					squares.getChildren().remove(image);
				}
			}
		}
		// To remove the object
		if (dungeon.getEntities().contains(entity)) {
			dungeon.getEntities().remove(entity);
		}
	}

	// Update the 'door' entity to 'open' status
	public void updateDoorUI(Entity entity) {
		String open_door_image_path = entity.getImagePath();
		Image open_door = new Image(open_door_image_path);
		System.out.println("In update door function");
		ImageView image = getImageByEntity(imageEntities, entity);
		image.setImage(open_door);
	}

	// Method to generate a new entity in the maze
	public void generateObject(EntityType type) {

		Random rand = new Random();

		int width = dungeon.getWidth();
		int height = dungeon.getHeight();

		Pair location = null;
		while (location == null) {
			location = getUniqueSpawnLocation(rand, width, height);
		}

		Entity new_object = null;
		if (type == EntityType.TREASURE) {
			new_object = new Treasure(location.getX(), location.getY());
		} else if (type == EntityType.POTION) {
			new_object = new Potion(location.getX(), location.getY());
		}

		this.dungeon.getEntities().add(new_object);

		Image new_image = new Image(new_object.getImagePath());
		ImageView new_view = new ImageView(new_image);
		new_view.setId(new_object.getImageID());
		GridPane.setColumnIndex(new_view, new_object.getX());
		GridPane.setRowIndex(new_view, new_object.getY());

		imageEntities.add(new_view);
		squares.getChildren().add(new_view);

	}
	

	public Pair getUniqueSpawnLocation(Random rand, int x, int y) {
		int rand_x = rand.nextInt(x);
		int rand_y = rand.nextInt(y);
		List<Entity> entitiesAtXY = getEntities(rand_x, rand_y);
		if (entitiesAtXY.size() == 0) {
			return new Pair(rand_x, rand_y);
		}
		return null;
	}
	
	private List<Entity> getEntitiesToRemove(int x, int y, EntityType... e_type) {
		List<Entity> entities_to_remove = new ArrayList<Entity>();
		for (int i = 0; i < e_type.length; i++) {
			List<Entity> entities = entitiesInVicinity(x, y, e_type[i]);
			entities_to_remove.addAll(entities);
		}
		return entities_to_remove;
	}
	
	
}