package unsw.dungeon;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Switch extends Entity{

	private String image_path = "/pressure_plate.png";
	private boolean triggered = false;

	public Switch(Dungeon dungeon, int x, int y) {
        super(dungeon, x, y);
    }
	
    @Override
    public EntityType getType() {
        return EntityType.SWITCH;
    }

    @Override
    public boolean isBlocked(List<Entity> entitiesAtNew) {
        return true;
    }

	@Override
	public void moveTo(int newX, int newY) {}

    @Override
    public void postMove(List<Entity> entitiesAtNew) {}

    @Override
	public boolean stepOver() {
   	
    	if(triggered) {
    		//The switch has already been triggered previously
    		return false;
    	}
    	
    	triggered = true;
 
		Random rand = new Random();
		int generator_key = rand.nextInt(2);
		if (generator_key == 0) {
			switchEvent(EntityType.TREASURE);
		} else if (generator_key == 1) {
			switchEvent(EntityType.POTION);
		}
		return true;
	}
    
    public void switchEvent(EntityType type) {
        Pair coordinates = dungeon.getUniqueCoordinates();
        Entity newObject = null;
        if (type == EntityType.TREASURE) {
            newObject = new Treasure(dungeon, coordinates.getX(), coordinates.getY());
        } else if (type == EntityType.POTION) {
            newObject = new Potion(dungeon, coordinates.getX(), coordinates.getY());
        }
        dungeon.addEntity(newObject);
        dungeon.generateEntity(newObject);
    }

	@Override
	public String getImageID() {
		return "Switch image";
	}
	
	@Override
	 public String getImagePath() {
		return this.image_path;
	}

	@Override
	public ArrayList<String> getImageList() {
		return null;
	}

	public boolean isTriggered() {
		return triggered;
	}
}
