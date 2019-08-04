package unsw.dungeon;

import java.util.ArrayList;
import java.util.List;

public class Wall extends Entity  {

	private String image_path = "/brick_brown.png";

	public Wall(Dungeon dungeon, int x, int y) {
        super(dungeon, x, y);
    }

	
	@Override
    public EntityType getType(){return EntityType.WALL;}

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
		return false;
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
	public ArrayList<String> getImageList() {
		return null;
	}	
	
}
