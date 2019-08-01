package unsw.dungeon;

import java.util.ArrayList;
import java.util.List;

public class Treasure extends Entity{

	private int treasureCoins = 0;
	private String image_path = "gold_pile.png";
	
	public Treasure(Dungeon dungeon, int x, int y) {
        super(dungeon, x, y);
    }

    @Override
    public EntityType getType() {
        return EntityType.TREASURE;
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
    
    	treasureCoins++;
    	dungeon.getInventoryEntities().add(this);
		dungeon.removeEntity(this);
		return true;
	}

	@Override
	public String getImageID() {
		return "Treasure image";
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
