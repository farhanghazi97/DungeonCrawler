package unsw.dungeon;

import java.awt.Rectangle;
import java.util.List;

public class Wall extends Entity  {

	private String type = "WALL";
	
    public Wall(int x, int y) {
        super(x, y);
    }

    @Override
    public String toString() {
		return String.format("Wall object");
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

}
