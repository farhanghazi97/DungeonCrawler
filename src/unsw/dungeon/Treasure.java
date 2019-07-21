package unsw.dungeon;

import java.util.ArrayList;
import java.util.List;

public class Treasure extends Entity{

	private static int treasureCoins = 0;
	private String image_path = "gold_pile.png";
	private ArrayList<String> image_list = new ArrayList<String>();
	
	public Treasure(int x, int y) {
        super(x, y);
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
	public void moveTo(int newX, int newY) {
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
    	Mediator.getInstance().collectedEntities.add(this);
		MediatorHelper.removeEntity(this);
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
	public ArrayList<String> getImage_list() {
		return image_list;
	}
	
}
