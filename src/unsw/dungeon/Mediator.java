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

	private Dungeon dungeon;
	private GridPane squares;
	 private List<ImageView> imageEntities;

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

		if (!bouldersAtCurrent.isEmpty()) {
			// there is a boulder at currentX and currentY
			// We will move boulder instead of player
			entityToMove = bouldersAtCurrent.get(0);
		}

		if (entityToMove.isBlocked(entitiesAtNew)) {
			return false;
		}

		entityToMove.moveTo(newX, newY);
		//Calling entitesAtCurrent stepOver on a loop
		for(Entity entity: entitiesAtCurrent) {
			entity.stepOver();
		}
		
		entityToMove.postMove(entitiesAtNew);

		return true;
	}
	
	//Called when player presses the 'S' key on the keyboard
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
	
	// Check if player can pick up key
	// If player can pick up key, add it to player inventory
	// If player cannot pick up key, do nothing.
	public void pickUpKey(int currentX, int currentY) {
		List<Entity> keyAtCurrent = getEntities(currentX, currentY, Key.class);
		if (!keyAtCurrent.isEmpty()) {
			Entity key = keyAtCurrent.get(0);
			// Check if player already has key or not
			if (!isCollected(key)) {
				// If not, add to inventory
				collectedEntities.add(key);
				System.out.println("Key collected");
				// Update 'key' object internal data
				key.stepOver();
				// Remove the 'key' image from screen
				removeEntity(key);

				// Check if inventory is what it should be
				System.out.println(collectedEntities);
			}
		}
	}
	
	private boolean gameOver = false;

	public void markGameOver() {
		gameOver = true;
	}

	
	// HELPER OPERATIONS BELOW

	// Returns true if the new coordinates given are outside the boundaries of the
	// dungeon
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


	
	public void pickUpPotion(int currentX , int currentY) {
		List<Entity> entity_objects = dungeon.getEntities();
		List<Entity> potionAtCurrent = getEntities(currentX , currentY , Potion.class);
		if(!potionAtCurrent.isEmpty()) {
			// Get potion entity at current (X , Y)
			Entity potion = potionAtCurrent.get(0);
			// Add to inventory
			collectedEntities.add(potion);
			System.out.println("Potion collected");
			// Update 'potion' object internal data
			potion.stepOver();
			// Remove image of 'potion' from screen
			removeEntity(potion);
			if(entity_objects.contains(potion)) {
				entity_objects.remove(potion);
			}
			// Check if inventory checks out
			System.out.println(collectedEntities);
		}
	}
	
	
	// Attempts to unlock the door at current location
	public void unlockDoor(int currentX , int currentY) {
		
		boolean key_exists = false;
		List<Entity> doorAtCurrent = getEntities(currentX , currentY , Door.class);
		
		Entity e = null;
		int remove_key_index = -1;
		
		// Check if player has a key
		// Size of this list should always be 1 (as player can only carry one key at a time)
		for(int i = 0; i < collectedEntities.size(); i++) {
			e = collectedEntities.get(i).getObjectByType("Key");
			if(e != null) {
				remove_key_index = i;
				key_exists = true;
				break;
			}
		}
		
		// Use the key to unlock door. 
		// If key fits lock , remove key from player inventory and unlock door (UI update).
		// If key does not fit lock , do nothing.
		if(key_exists) {
			if(!doorAtCurrent.isEmpty()) {
				Entity d = doorAtCurrent.get(0);
				if(d.getDoorID() == e.geKeyID()) {
					System.out.println("Door matches key!");
					d.stepOver();
					collectedEntities.remove(remove_key_index);
					updateDoorUI(d);
					System.out.println(collectedEntities);
				} else {
					System.out.println("Key dosen't fit lock!");
				}
			}
		}
	}
	
	// Update the 'door' entity to 'open' status
	private void updateDoorUI(Entity entity) {
		Image open_door = new Image("/open_door.png");
		System.out.println("In update door function");
		for(int i = 0; i < imageEntities.size(); i++) {
			ImageView image = imageEntities.get(i);
			if(GridPane.getColumnIndex(image) == entity.getX() && GridPane.getRowIndex(image) == entity.getY()) {
				if(image.getId().equals("Door image")) {
					image.setImage(open_door);
					break;
				}
			}
		}
	}

	
}


//public void pickUpTreasure(int currentX , int currentY) {
//	
//	List<Entity> treasureAtCurrent = getEntities(currentX , currentY , Treasure.class);
//	//if(!treasureAtCurrent.isEmpty()) {
//		// Get treasure entity at current (X , Y)
//		//Entity treasure = treasureAtCurrent.get(0);
//		// Add to inventory
//		//collectedEntities.add(treasure);
//		//System.out.println("Treasure collected");
//		// Update 'treasure' object internal data
//		treasure.stepOver();
//		// Remove image of 'treasure' from screen
//		//
//		// Check if inventory checks out
//		//System.out.println(collectedEntities);
//	//}
//}
