package unsw.dungeon;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Switch extends Entity{

	private String type = "Switch";
	private String image_path = "/pressure_plate.png";
	private ArrayList<String> image_list = new ArrayList<String>();
	
	public Switch(int x, int y) {
        super(x, y);
    }

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
    public void postMove(List<Entity> entitiesAtNew) {

    }

    @Override
	public boolean stepOver() {
		// Need to randomise generation of objects further
		Random rand = new Random();
		int generator_key = rand.nextInt(2);
		if (generator_key == 0) {
			Mediator.getInstance().generateObject(EntityType.TREASURE);
		} else if (generator_key == 1) {
			Mediator.getInstance().generateObject(EntityType.POTION);
		}
		return true;
	}

	
	@Override
	public int getKeyID() {
		return -1;
	}
	
	@Override
	public boolean isDoorOpen() {
		return false;
	}
	
	@Override
	public Entity getObjectByType(String s) {
		if(s.equals(type)) {
			return this;
		} else {
			return null;
		}
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

}
