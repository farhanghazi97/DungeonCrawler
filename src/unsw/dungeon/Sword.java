package unsw.dungeon;

import java.awt.Rectangle;
import java.util.List;

public class Sword extends Entity {
	
	private int swingsRemaining = 5;
	private boolean collected = false;
	
	public Sword(int x, int y) {
        super(x, y);
    }

    @Override
    public EntityType getType() {
        return EntityType.SWORD;
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
		this.swingsRemaining = 5;
		this.collected = true;
		
		if(swingsRemaining > 0) {
			return true;
		}else {
			return false;
		}
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
	public String toString() {
		return "Sword [swingsRemaining=" + swingsRemaining + ", collected=" + collected + "]";
	}
	
	

}
