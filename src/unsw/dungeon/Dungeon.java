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
	private List<Entity> entities;
	private Player player;
	private JSONObject goal;
	private DungeonController dc;
	private List<Entity> collectedEntities = new LinkedList<>();
	private boolean gameOver = false;

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
			if (entity.getType() != EntityType.SWITCH)
				entity.stepOver();
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

	public List<Entity> getInventoryEntities() {
		System.out.println("In dungeons: getInventory");
		return collectedEntities;
	}
	
	public Entity getInventoryEntity(EntityType entityType) {
		System.out.println("In dungeons: getInventoryEntity");
		for (Entity collected : collectedEntities) {
			if (collected.getType() == entityType) {
				return collected;
			}
		}
		return null;
	}


	public JSONObject getGoal() {
		return goal;
	}

	public void markGameOver() {
		System.out.println("Here");
		gameOver = true;
	}

	// Called when player presses 'U' key on keyboard
	// Attempts to unlock the door at current location
	public void handleKeyPressU(int currentX, int currentY) {
		List<Entity> doors = doorInFront(currentX, currentY);
		if (!doors.isEmpty()) {
			System.out.println("Found door in vicnity");
			Entity door = doors.get(0);
			door.stepOver();
		}
	}

	public void handleKeyPressS(int x, int y) {
		System.out.println("Mediator: In swing sword");
		Entity sword = getInventoryEntity(EntityType.SWORD);
		if (sword != null) {
			if (((Sword) sword).swing()) {
				// Check if enemy is in vicinity
				List<Entity> enemies = entitiesInVicinity(x, y, EntityType.ENEMY);
				if (enemies != null) {
					// If true -> remove enemy
					// If false ->do nothing
					System.out.println(enemies);
					for (Entity enemy : enemies) {
						removeEntity(enemy);
					}
				}
			}
		}
	}

	// Called when player presses the 'B' key on keyboard
	// Bomb timer is started
	public void handleKeyPressB(int x, int y) {
		System.out.println("Dungeon: In ignite bomb");
		Entity oldBomb = getInventoryEntity(EntityType.BOMB);
		if (oldBomb != null) {
			collectedEntities.remove(oldBomb);
			
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

	public void addEntity(Entity entity) {
		entities.add(entity);
	}

	public void setDungeonController(DungeonController dc) {
		this.dc = dc;
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

	public void removeEntity(Entity entity) {
		dc.removeEntity(entity);
		if (entities.contains(entity)) {
			entities.remove(entity);
		}
	}
	
    public boolean outsideDungeon(int newX, int newY) {
        if(newX == -1|| newY == -1) {
        	//If there was an entity , and it was destroyed, then coordinates change to -1
        	return true;
        }
        if (newX + 1 > width || newY + 1 > height) {
            return true;
        }
        return false;
    }
    
    // Checks if there is a door in front of player
    public List<Entity> doorInFront(int x, int y) {
        Dungeon dungeon = Mediator.getInstance().getDungeon();
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
    
    // Method to generate a new entity in the maze
    public void generateObject(EntityType type) {

        Pair location = null;
        while (location == null) {
            location = dc.getUniqueMazeCoordinates();
        }

        Entity newObject = null;
        if (type == EntityType.TREASURE) {
            newObject = new Treasure(this, location.getX(), location.getY());
        } else if (type == EntityType.POTION) {
            newObject = new Potion(this, location.getX(), location.getY());
        }

        entities.add(newObject);

        dc.generateImage(newObject);

    }
   

}
