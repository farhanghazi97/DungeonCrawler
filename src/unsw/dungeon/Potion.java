package unsw.dungeon;

import java.awt.Rectangle;
import java.util.List;

public class Potion extends Entity {

	private boolean collected = false;
	private String type = "Potion";
	private int count = 0;
	
	public Potion(int x, int y) {
        super(x, y);
    }

    @Override
    public EntityType getType() {
        return EntityType.POTION;
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
    	System.out.println("Inside Potion's stepOver");
    
		Entity tempPotion = Mediator.getInstance().getCollected(EntityType.POTION);

		if(tempPotion != null) {
			//Player already has a potion
			return false;
		}else {
			//Add new sword
			if(Mediator.getInstance().collectedEntities.add(this)) {
				this.collected = true;
				this.count++;
				Mediator.getInstance().removeEntity(this);
				System.out.println(this.toString());
				return true;
			}
		}
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
	public int geKeyID() {
		return -1;
	}
	
	@Override
	public boolean isIs_open() {
		return false;
	}
	
	@Override
	public String toString() {
		return "Potion [count=" + count + ", collected=" + collected + "]";
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
		// TODO Auto-generated method stub
		return "Potion image";
	}

}
