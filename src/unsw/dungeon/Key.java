package unsw.dungeon;

import java.util.List;

public class Key extends Entity {

	private int keyId;
	public Key(int x, int y, int keyId) {
		super(x, y);
		this.keyId = keyId;
	}

	@Override
	public EntityType getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isBlocked(List<Entity> entitiesAtNew) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void postMove(List<Entity> entitiesAtNew) {
		// TODO Auto-generated method stub
		
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
