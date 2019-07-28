/**
 *
 */
package unsw.dungeon;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

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
   
   
    //New added
    private List<Entity> collectedEntities = new LinkedList<>();
    //private GridPane squares;
    private boolean gameOver = false;
    //private List<ImageView> imageEntities;
    

    public Dungeon(int width, int height , JSONObject goal) {
        this.width = width;
        this.height = height;
        this.entities = new ArrayList<>();
        this.player = null;
        this.goal = goal;
        
    }
    
    
    public boolean moveTo(int currentX, int currentY, int newX, int newY) {
    	//dlm.getImageEntities();
    	System.out.println(collectedEntities);
		if (gameOver) {
			return false;
		} 
		
		if (MediatorHelper.outsideDungeon(newX, newY)) {
			//Whether moving boulder or player, outside dungeon boundaries is prohibited.
			return false;
		}

		// At start, entity to move is the player
		Entity entityToMove = player;

		List<Entity> fromEntities = getEntities(currentX, currentY);
		List<Entity> toEntities = getEntities(newX, newY);
		List<Entity> bouldersAtCurrent = getEntities(currentX, currentY, Boulder.class);
		List<Entity> enemies = getEntities(EntityType.ENEMY);
		List<Entity> exitAtCurrent =getEntities(currentX , currentY , Exit.class);
		
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
    
    public List<Entity> getEntities(int x, int y, Class clazz){
    	 //Dungeon dungeon = Mediator.getInstance().getDungeon();
         List<Entity> list = new LinkedList<>();
         for (Entity entity : entities) {
             if (entity.getX() == x && entity.getY() == y && clazz.isInstance(entity)) {
                 list.add(entity);
             }
         }
         return list;
    }
    
	public List<Entity> getCollectedEntities() {
		System.out.println("In dungeons: getCollectedEntitytes");
		return collectedEntities;
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

	
	public Entity getCollected(EntityType entityType) {
		for (Entity collected : collectedEntities) {
			if (collected.getType() == entityType) {
				return collected;
			}
		}
		return null;
	}

	public void setEntities(List<Entity> entities) {
		this.entities = entities;
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
		List<Entity> doors = MediatorHelper.doorInFront(currentX, currentY);
		if (!doors.isEmpty()) {
			System.out.println("Found door in vicnity");
			Entity door = doors.get(0);
			door.stepOver();
		}
	}
	
	public void handleKeyPressS(int x, int y) {
		System.out.println("Mediator: In swing sword");
		Entity sword = getCollected(EntityType.SWORD);
		if (sword != null) {
			if (((Sword) sword).swing()) {
				// Check if enemy is in vicinity
				List<Entity> enemies = MediatorHelper.entitiesInVicinity(x, y, EntityType.ENEMY);
				if (enemies != null) {
					// If true -> remove enemy
					// If false ->do nothing
					System.out.println(enemies);
					for (Entity enemy : enemies) {
						MediatorHelper.removeEntity(enemy);
					}
				}
			}
		}
	}

	// Called when player presses the 'B' key on keyboard
	// Bomb timer is started
	public void handleKeyPressB(int x, int y) {
		System.out.println("Mediator: In ignite bomb");
		Entity old_bomb = getCollected(EntityType.BOMB);
		if (old_bomb != null) {
			collectedEntities.remove(old_bomb);
			Bomb new_bomb = spawnBombAtCurrentLocation(x, y);
			new_bomb.startBombSelfDestruct(1000);
		}
	}

	// Method to bring up a new bomb at (x,y) location
	private Bomb spawnBombAtCurrentLocation(int x, int y) {
		System.out.println("In spawn bomb");
		Bomb new_bomb = new Bomb(this, x, y);
		MediatorHelper.setupImage(new_bomb);
		return new_bomb;

	}




}
