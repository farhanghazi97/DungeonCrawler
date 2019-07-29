package unsw.dungeon;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.json.JSONObject;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

/**
 * A dungeon in the interactive dungeon player.
 *
 * A dungeon can contain many entities, each occupy a square. More than one
 * entity can occupy the same square.
 *
 * @author Robert Clifton-Everest
 *
 */
public class Dungeon {

	private int width, height;
	private boolean gameOver = false;
	private Player player;
	private JSONObject goal;
	private DungeonController dc;
	private List<Entity> playerInventory = new LinkedList<>();
	private List<Entity> entities;
	
	public Dungeon(int width, int height, JSONObject goal) {
		this.width = width;
		this.height = height;
		this.entities = new ArrayList<>();
		this.player = null;
		this.goal = goal;
	}

	public boolean moveTo(int currentX, int currentY, int newX, int newY) {
		if (gameOver) {
			return false;
		}

		if (outsideDungeon(newX, newY)) {
			// Whether moving boulder or player, outside dungeon boundaries is prohibited.
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

	public void markGameOver() {
		System.out.println("Here");
		gameOver = true;
	}
	
    public boolean outsideDungeon(int newX, int newY) {
        if(newX == -1|| newY == -1)  return true;
        if (newX + 1 > width || newY + 1 > height)  return true;
        return false;
    }


	// Called when player presses 'U' key on keyboard
	// Attempts to unlock the door at current location
	public void handleKeyPressU(int currentX, int currentY) {
		List<Entity> doors = entitiesInFront(currentX, currentY, EntityType.DOOR);
		if (!doors.isEmpty()) {
			System.out.println("Found door in vicnity");
			Entity door = doors.get(0);
			door.stepOver();
		}
	}

	public void handleKeyPressS(int x, int y) {
		System.out.println("Dungeon: In handleKeyPressS");
		Entity sword = getInventoryEntity(EntityType.SWORD);
		if (sword != null)  ((Sword) sword).swing(x,y);
	}

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
	

	public List<Entity> entitiesInFront(int x, int y, EntityType type) {
		List<Entity> list = new LinkedList<>();
        for (Entity entity : entities) {
            if (entity.getType() == type && ((entity.getY() == y - 1 && entity.getX() == x))) {
                    list.add(entity);
            }
        }
        return list;
	}

	public void removeEntity(Entity entity) {
		dc.removeEntity(entity);
		if (entities.contains(entity)) {
			entities.remove(entity);
		}
	}
    
    //Getters for entities
    public List<Entity> getEntities() {
		return entities;
	}

	// Returns all entities on (x,y) coordinates
	public List<Entity> getEntities(int x, int y) {
		// Dungeon dungeon = Mediator.getInstance().getDungeon();
		List<Entity> list = new LinkedList<>();
		for (Entity entity : entities) {
			if (entity.getX() == x && entity.getY() == y) {
				list.add(entity);
			}
		}
		return list;
	}

	public List<Entity> getEntities(int x, int y, Class clazz) {
		List<Entity> list = new LinkedList<>();
		for (Entity entity : entities) {
			if (entity.getX() == x && entity.getY() == y && clazz.isInstance(entity)) {
				list.add(entity);
			}
		}
		return list;
	}


	// Returns entity if the player already has an entity of given type
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
	public List<Entity> getInventoryEntities() {
		System.out.println("In dungeons: getInventory");
		return playerInventory;
	}
	
	public Entity getInventoryEntity(EntityType entityType) {
		//System.out.println("In dungeons: getInventoryEntity");
		for (Entity collected : playerInventory) {
			if (collected.getType() == entityType) {
				return collected;
			}
		}
		return null;
	}
	
	public void addInventory(Entity entity) {
		System.out.println("Adding to inventory:" +entity);
		playerInventory.add(entity);
	}
	
	public ImageView getImageByEntity(List<ImageView> entities, Entity e) {
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
	
	public List<ImageView> getImageEntities() {
		return dc.getInitialEntities();
	}
	
	public Pair getUniqueCoordinates() {
		Pair coordinates = null;
		while (coordinates == null) {
			coordinates = dc.getUniqueMazeCoordinates();
		}
		return coordinates;
	}
	
	public void generateEntity(Entity entity) {
		dc.generateImage(entity);
	}

	
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
	
}
