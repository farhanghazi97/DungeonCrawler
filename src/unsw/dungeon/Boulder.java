package unsw.dungeon;

import java.awt.Rectangle;
import java.util.List;

public class Boulder extends Entity {

	private String type = "Boulder";
	private String image_path = "/boulder.png";
	
	public Boulder(int x , int y) {
		super(x , y);
	}
	
	@Override
    public String toString() {
		return String.format("Boulder object");
    }

	@Override
	public EntityType getType(){return EntityType.BOULDER;}

	@Override
	public boolean isBlocked(List<Entity> entitiesAtNew){
		for (Entity entity : entitiesAtNew) {
			if (entity.getType() == EntityType.DOOR && entity.isIs_open() == true) {
				return false;
			} else if (entity.getType()   == EntityType.WALL	 || entity.getType()    ==EntityType.BOULDER
				|| entity.getType() == EntityType.DOOR || entity.getType()    == EntityType.TREASURE
				|| entity.getType() == EntityType.KEY 	 || entity.getType()    == EntityType.SWORD
				|| entity.getType() == EntityType.POTION || entity.getType() == EntityType.BOMB){
					return true;
			}
		}
		return false;
	}

	@Override
	public void postMove(List<Entity> entitiesAtNew) {
		for (Entity entity: entitiesAtNew) {
			if(entity.getType() == EntityType.SWITCH) {
				//Mediator.getInstance().triggerSwitch();
			}
		}
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
		return "Boulder image";
	}
	
	@Override
	 public String getImagePath() {
		return this.image_path;
	}

}
