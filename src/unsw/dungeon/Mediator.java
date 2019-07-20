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

		if (MediatorHelper.outsideDungeon(dungeon, newX, newY)) {
			// Whether moving boulder or player, outside dungeon boundaries is prohibited.
			return false;
		}
	

		// At start, entity to move is the player
		Entity entityToMove = dungeon.getPlayer();
		List<Entity> fromEntities = MediatorHelper.getEntities(dungeon, currentX, currentY);
		List<Entity> toEntities = MediatorHelper.getEntities(dungeon, newX, newY);
		List<Entity> bouldersAtCurrent = MediatorHelper.getEntities(dungeon, currentX, currentY, Boulder.class);
		List<Entity> enemies =getEntityOfType(EntityType.ENEMY);
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
			List<Entity> switchAtCurrent = MediatorHelper.getEntities(dungeon, currentX, currentY, Switch.class);
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
			((Enemy) enemy).moveEnemy(newX, newY);
		}
		
		

		entityToMove.postMove(toEntities);
		
		
		
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
				List<Entity> enemies = MediatorHelper.entitiesInVicinity(dungeon, x, y, EntityType.ENEMY);
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
		List<Entity> doors = MediatorHelper.doorInFront(dungeon, currentX, currentY);
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
			Entity new_bomb = spawnBombAtCurrentLocation(x, y);
			startBombSelfDestruct(new_bomb, 1000);
		}
	}

	// Manages the UI for changing bomb image
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

	// Manages image changes and bomb timer
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
			List<Entity> entities_to_remove = MediatorHelper.entitiesInVicinity(dungeon,
					new_bomb.getX(), new_bomb.getY(), EntityType.ENEMY,
					EntityType.BOULDER, EntityType.PLAYER);
			if (entities_to_remove.contains(dungeon.getPlayer())) {
				Entity potion = this.getCollected(EntityType.POTION);
				// If player does not have potion, bomb effective
				if (potion == null) {
					this.markGameOver();
				} else {
					entities_to_remove.remove(dungeon.getPlayer());
				}
			}
			// Remove all entities in 3 X 3 area
			if (!entities_to_remove.isEmpty()) {
				for (int i = 0; i < entities_to_remove.size(); i++) {
					Entity enemy = entities_to_remove.get(i);
					this.removeEntity(enemy);
				}
			}
			// Remove bomb
			removeEntity(new_bomb);
			System.out.println("Bomb removed");
		});

		new Thread(task).start();
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
	
	// Returns entity if the player already has an entity of given type
	public List<Entity> getEntityOfType(EntityType entityType) {
		List<Entity> list = new LinkedList<>();
		for (Entity entity : dungeon.getEntities()) {
			if (entity.getType() == entityType) {
				list.add(entity);
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
					//Removing from screen
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

		int width = dungeon.getWidth();
		int height = dungeon.getHeight();

		Pair location = null;
		while (location == null) {
			location = MediatorHelper.getUniqueSpawnLocation(dungeon, width, height);
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

	// UI
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

}