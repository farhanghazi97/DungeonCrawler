package unsw.dungeon;

import java.util.List;

public class Exit extends Entity{

    public Exit(int x, int y) {
        super(x, y);
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
