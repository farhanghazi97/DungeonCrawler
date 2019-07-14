package unsw.dungeon;

import java.awt.Rectangle;
import java.util.List;

public class Treasure extends Entity{

	private String type = "Treasure";
	private int treasureCoins = 0;
	
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
    public void postMove(List<Entity> entitiesAtNew) {

    }

    @Override
	public boolean stepOver() {
    	System.out.println("In Treasure's stepOver");
    	System.out.println(toString());
    	this.treasureCoins++;
    	Mediator.getInstance().removeEntity(this);
		return true;
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
	public int geKeyID() {
		return -1;
	}
	
	@Override
	public boolean isIs_open() {
		return false;
	}
	
	@Override
	public String toString() {
		return "Treasure [Count =" + treasureCoins + ", collected=" + treasureCoins + "]";
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
		return "Treasure image";
	}

}
