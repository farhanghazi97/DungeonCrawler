package unsw.dungeon;

import java.util.ArrayList;
import java.util.List;

public class Exit extends Entity{
	
	private String image_path = "/exit.png";
	private ArrayList<String> image_list = new ArrayList<String>();
	
    public Exit(int x, int y) {
        super(x, y);
    }

    @Override
    public String toString() {
    	return "EXIT object";
    }
    
    @Override
    public EntityType getType() {
        return EntityType.EXIT;
    }

    @Override
    public boolean isBlocked(List<Entity> entitiesAtNew) {
        return false;
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getImageID() {
		return "Exit Image";
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
