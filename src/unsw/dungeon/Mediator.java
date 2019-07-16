package unsw.dungeon;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

//Singleton class
public class Mediator {

	// List of entities collected by player
	public List<Entity> collectedEntities = new LinkedList<>();

	private static Mediator mediator = new Mediator();

	private Mediator() {}

	public static Mediator getInstance() {
		return mediator;
	}

	/**
	 * Getters for testability.
	 * @return
	 */
	public Dungeon getDungeon() {
		return dungeon;
	}

	public GridPane getSquares() {
		return squares;
	}
	
	private Dungeon dungeon;
	private GridPane squares;
	private List<ImageView> imageEntities;
	private boolean gameOver = false;

	public void setDungeon(Dungeon dungeon, GridPane squares , List<ImageView> imageEntities) {
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
		List<Entity> switchAtCurrent = getEntities(currentX , currentY , Switch.class);

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
			if(!switchAtCurrent.isEmpty()) {
				if(rand.nextInt(3) == 1) triggerSwitchEvent();
			}
		}
		
		//Calling entitesAtCurrent stepOver on a loop
		for(Entity entity: entitiesAtCurrent) {
			entity.stepOver();
		}
		
		entityToMove.postMove(entitiesAtNew);

		return true;
	}
	
	// HELPER OPERATIONS BELOW
	
	private void triggerSwitchEvent() {
		spawnItems();
	}
	
	private void spawnItems() {
		
		// Need to randomise generation of objects further
		Random rand = new Random();
		int generator_key = rand.nextInt(2);
		if(generator_key == 0) {
			generateObject(EntityType.TREASURE);
		} else if(generator_key == 1) {
			generateObject(EntityType.POTION);
		}
	}
	
	public void generateObject(EntityType type) {
		
		Random rand = new Random();
		int new_object_width  = rand.nextInt(dungeon.getWidth());
		int new_object_height = rand.nextInt(dungeon.getHeight());
		
		Entity new_object = null;
		if(type == EntityType.TREASURE) {
			new_object = new Treasure(new_object_width , new_object_height);
		} else if(type == EntityType.POTION) {
			new_object = new Potion(new_object_width , new_object_height);
		}
		
		this.dungeon.getEntities().add(new_object);
		
		Image new_image = new Image(new_object.getImagePath());
		ImageView new_view = new ImageView(new_image);
		new_view.setId(new_object.getImageID());
		GridPane.setColumnIndex(new_view, new_object.getX());
		GridPane.setRowIndex(new_view , new_object.getY());
		
		imageEntities.add(new_view);
		squares.getChildren().add(new_view);
		
	}
	
	// Called when player presses the 'S' key on the keyboard
	public void swingSword(int x, int y) {
		System.out.println("Mediator: In swing sword");
		Entity sword = getCollected(EntityType.SWORD);
		if (sword != null) {
			if(((Sword) sword).swing()) {
				//Check if enemy is in vicinity
				List<Entity> enemies = enemiesInVicinity(x, y);
				if(enemies != null) {
					//If true -> remove enemy
					//If false ->do nothing
					System.out.println(enemies);
					for(Entity enemy : enemies) {
						removeEntity(enemy);
					}
				}
			}
		}
	}
	
	public void markGameOver() {
		gameOver = true;
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
	private List<Entity> getEntities(int x, int y, Class clazz) {
		List<Entity> list = new LinkedList<>();
		for (Entity entity : dungeon.getEntities()) {
			if (entity.getX() == x && entity.getY() == y && clazz.isInstance(entity)) {
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
	
	// Checks if there is a door in front of player
	private List<Entity> doorInVicinity(int x , int y) {
		List<Entity> list = new LinkedList<>();
		for(Entity entity : dungeon.getEntities()) {
			if(entity.getType() == EntityType.DOOR) {
				if(entity.getY() == y - 1 && entity.getX() == x) {
					list.add(entity);
				}
			}
		}
		return list;
	}
	
	//Returns a list of enemies if they are in adjacent squares
	private List<Entity> enemiesInVicinity(int x, int y) {
		//System.out.println("Inside enemiesInVicinity");
		List<Entity> list = new LinkedList<>();
		
		for (Entity entity : dungeon.getEntities()) {
			if (entity.getType() == EntityType.ENEMY && 
			   ((entity.getX() == x+1 && entity.getY() == y) ||
			   (entity.getX() == x+1 && entity.getY() == y-1) ||
			   (entity.getX() == x+1 && entity.getY() == y+1) ||
			   (entity.getX() == x && entity.getY() == y+1) ||
			   (entity.getX() == x && entity.getY() == y-1) ||
			   (entity.getX() == x-1 && entity.getY() == y) ||
			   (entity.getX() == x-1 && entity.getY() == y-1)||
			   (entity.getX() == x+1 && entity.getY() == y+1)))
			    {
				    list.add(entity);
			}
		}
		return list;
	}

	// Removes UI element and object corresponding to given entity
	public void removeEntity(Entity entity) {
		System.out.println("In remove entity function");
		for(int i = 0; i < imageEntities.size(); i++) {
			ImageView image = imageEntities.get(i);
			// Map GridPane co-ords to entity co-ords
			if(GridPane.getColumnIndex(image) == entity.getX() && GridPane.getRowIndex(image) == entity.getY()) {
				if(image.getId().equals(entity.getImageID())) {
					//Removing from screen
					squares.getChildren().remove(image);
				} 
			}
		}
		//To remove the object
		if(dungeon.getEntities().contains(entity)) {
			dungeon.getEntities().remove(entity);
		}
	}

	// Called when player presses 'U' key on keyboard
	// Attempts to unlock the door at current location
	public void unlockDoor(int currentX , int currentY) {
		List<Entity> door = doorInVicinity(currentX , currentY);
		if(!door.isEmpty()) {
			Entity d = door.get(0);
			if(d.stepOver()) {
				for(int i = 0; i < collectedEntities.size(); i++) {
					Entity e = collectedEntities.get(i).getObjectByType("Key");
					if(e != null) {
						collectedEntities.remove(i);
						break;
					}
				}
			}
		}
	}

	// Update the 'door' entity to 'open' status
	public void updateDoorUI(Entity entity) {
		String open_door_image_path = entity.getImagePath();
		Image open_door = new Image(open_door_image_path);
		System.out.println("In update door function");
		for(int i = 0; i < imageEntities.size(); i++) {
			ImageView image = imageEntities.get(i);
			if(GridPane.getColumnIndex(image) == entity.getX() && GridPane.getRowIndex(image) == entity.getY()) {
				if(image.getId().equals(entity.getImageID())) {
					image.setImage(open_door);
					break;
				}
			}
		}
	}
}