package unsw.dungeon;

import java.util.List;

public class Bomb extends Entity {

	private String type = "Bomb";
	private String image_path = "/bomb_unlit.png";
	
	public Bomb(int x , int y) {
		super(x , y);
	}

	public EntityType getType() {
		return EntityType.BOMB;
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
	public Entity getObjectByType(String s) {
		if(s.equals(type)) {
			return this;
		} else {
			return null;
		}
	}

	@Override
	public String getImageID() {
		return "Bomb image";
	}
	
	@Override
	 public String getImagePath() {
		return this.image_path;
	}

}
