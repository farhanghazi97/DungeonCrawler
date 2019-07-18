package unsw.dungeon;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class Wall extends Entity  {

	private String type = "Wall";
	private String image_path = "/brick_brown.png";
	private ArrayList<String> image_list = new ArrayList<String>();
    
	public Wall(int x, int y) {
        super(x, y);
    }

    @Override
    public String toString() {
		return String.format("WALL object");
    }

    @Override
    public EntityType getType(){return EntityType.WALL;}

    @Override
    public boolean isBlocked(List<Entity> entitiesAtNew) {
        return true;
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
	public void removeEntity() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void generateEntity() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public int getDoorID() {
		return -1;
	}
	
	@Override
	public int getKeyID() {
		return -1;
	}
	
	@Override
	public boolean isIs_open() {
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
		return "Wall image";
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
