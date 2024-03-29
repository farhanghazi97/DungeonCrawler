package unsw.dungeon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * The player entity
 *
 */
public class Player extends Entity {
	
    private String imagePath = "/human_new.png";
    private ArrayList<String> imageList = new ArrayList<String>(Arrays.asList("/human_new.png" , "/daeva.png", "/human_sword.png"));
    
    /**
     * Create a player positioned in square (x,y)
     * @param x
     * @param y
     */
    public Player(Dungeon dungeon, int x, int y) {
        super(dungeon, x, y);
    }
    
	@Override
	public EntityType getType(){return EntityType.PLAYER;}

	/**
	 * Method to check if player is blocked by entitesAtNew
	 * @param entitiesAtNew
	 * @return true if blocked, false otherwise
	 */
	@Override
	public boolean isBlocked(List<Entity> entitiesAtNew){
		for (Entity entity : entitiesAtNew) {
			if (entity.getType()==EntityType.WALL){
				return true;
			} else if(entity.getType() == EntityType.DOOR) {
				//Since entity type is a door has been checked, we can safely cast the entity to Door type.
				Door door = (Door) entity;
				if (!door.isOpen()){
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void moveTo(int newX, int newY , boolean flag) {
        x().set(newX);
        y().set(newY);
	}

	@Override
	public void postMove(List<Entity> entitiesAtNew){
		for (Entity entity : entitiesAtNew) {
			if (entity.getType()==EntityType.EXIT){
				if(entity.stepOver()) {
					dungeon.setGameOver(true);
					break;
				}
			}
		}
	}

	@Override
	public boolean stepOver() {
		return false;
	}

	@Override
	public String getImageID() {
		return "Player image";
	}
	
	@Override
	 public String getImagePath() {
		return this.imagePath;
	}

	@Override
	public ArrayList<String> getImageList() {
		return imageList;
	}
	
}
