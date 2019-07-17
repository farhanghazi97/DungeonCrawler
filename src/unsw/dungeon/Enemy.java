package unsw.dungeon;

import java.util.List;

public class Enemy extends Entity{

	public Enemy(int x, int y) {
		super(x, y);
		// TODO Auto-generated constructor stub
	}

	@Override
	public EntityType getType() {
		return EntityType.ENEMY;
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
		//Mediator.getInstance().removeEntity(this);
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
	public Entity getObjectByType(String s) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getDoorID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getKeyID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isIs_open() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getImageID() {
		return "Enemy image";
	}
	
	@Override
	 public String getImagePath() {
		return "";
	}
	

}
