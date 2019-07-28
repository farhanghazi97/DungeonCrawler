package unsw.dungeon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The player entity
 *
 */
public class Player extends Entity {
	
    private Dungeon dungeon;
    private String image_path = "/human_new.png";
    private ArrayList<String> image_list = new ArrayList<String>(Arrays.asList("/human_new.png" , "/daeva.png"));
    
    /**
     * Create a player positioned in square (x,y)
     * @param x
     * @param y
     */
    public Player(Dungeon dungeon, int x, int y) {
        super(x, y);
        this.dungeon = dungeon;
    }
    
    @Override
    public String toString() {
    	return "PLAYER object";
    }

	@Override
	public EntityType getType(){return EntityType.PLAYER;}

	@Override
	public boolean isBlocked(List<Entity> entitiesAtNew){
		for (Entity entity : entitiesAtNew) {
			if (entity.getType()==EntityType.WALL){
				return true;
			} else if(entity.getType() == EntityType.DOOR) {
				//Since entity type is a door has been checked, we can safely cast the entity to Door type.
				Door door = (Door) entity;
				if (!door.isDoorOpen()){
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void moveTo(int newX, int newY) {
        x().set(newX);
        y().set(newY);
	}

	@Override
	public void postMove(List<Entity> entitiesAtNew){
		for (Entity entity : entitiesAtNew) {
			if (entity.getType()==EntityType.EXIT){
				if(entity.stepOver()) {
					Mediator.getInstance().markGameOver();
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
		return this.image_path;
	}

	@Override
	public ArrayList<String> getImage_list() {
		return image_list;
	}
	
}
