package unsw.dungeon;

import java.util.List;

public class Key extends Entity {

	private String type = "Key";
	
	private int keyID;
	private boolean collected = false;
	
	public Key(int x, int y, int keyId) {
		super(x, y);
		this.keyID = keyId;
	}

	@Override
	public EntityType getType() {
		return EntityType.KEY;
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
		this.collected = true;
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
	public String toString() {
		return "Key [Key ID=" + keyID + ", collected=" + collected + "]";
	}
	
	@Override
	public int getDoorID() {
		return -1;
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
	public int geKeyID() {
		return keyID;
	}
	
	@Override
	public boolean isIs_open() {
		return false;
	}

	@Override
	public String getImageID() {
		
		return "Key image";
	}
	
	
}
