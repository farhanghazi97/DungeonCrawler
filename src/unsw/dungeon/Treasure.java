package unsw.dungeon;

import java.util.ArrayList;
import java.util.List;

public class Treasure extends Entity{

	private static int treasureCoins = 0;
	private String image_path = "gold_pile.png";
	private ArrayList<String> image_list = new ArrayList<String>();
	//Dungeon dungeon;
	
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
	public void moveTo(int newX, int newY , boolean flag) {
		//Nothing here
	}

    @Override
    public void postMove(List<Entity> entitiesAtNew) {

    }

    @Override
	public boolean stepOver() {
    	
    	System.out.println("In Treasure's stepOver");
    	Treasure.treasureCoins++;
    	System.out.println(toString());
    	if(dungeon == null) {
    		System.out.println("Dungeon is null");
    	}
    	dungeon.getInventoryEntities().add(this);
		dungeon.removeEntity(this);
		return true;
	}

	@Override
	public String toString() {
		return "TREASURE object [Count =" + treasureCoins + ", collected=" + treasureCoins + "]";
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
		return image_list;
	}
	
}
