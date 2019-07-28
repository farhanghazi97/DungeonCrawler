package unsw.dungeon;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Switch extends Entity{

	private String image_path = "/pressure_plate.png";
	private ArrayList<String> image_list = new ArrayList<String>();
	private boolean triggered = false;

	public Switch(Dungeon dungeon, int x, int y) {
        super(dungeon, x, y);
    }
//	public Switch(int x, int y) {
//        super(x, y);
//    }

	@Override
	public String toString() {
		return "SWITCH object";
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
	public void moveTo(int newX, int newY) {
		//Nothing here
	}

    @Override
    public void postMove(List<Entity> entitiesAtNew) {

    }

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
			MediatorHelper.generateObject(EntityType.TREASURE);
		} else if (generator_key == 1) {
			MediatorHelper.generateObject(EntityType.POTION);
		}
		return true;
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
	public ArrayList<String> getImage_list() {
		return image_list;
	}

	public boolean isTriggered() {
		return triggered;
	}

	public void setTriggered(boolean triggered) {
		this.triggered = triggered;
	}
	
	

}
