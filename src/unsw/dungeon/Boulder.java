package unsw.dungeon;

import java.util.ArrayList;
import java.util.List;

public class Boulder extends Entity {

	private String image_path = "/boulder.png";

	public Boulder(Dungeon dungeon, int x, int y) {
        super(dungeon, x, y);
    }

	@Override
	public EntityType getType(){return EntityType.BOULDER;}

	@Override
	public boolean isBlocked(List<Entity> entitiesAtNew){
		for (Entity entity : entitiesAtNew) {
			if (entity.getType() == EntityType.DOOR) {
				//Since entity type is a door has been checked, we can safely cast the entity to Door type.
				Door door = (Door) entity;
				if(door.isOpen()){
					return false;
				}
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
	public void moveTo(int newX, int newY) {
		x().set(newX);
		y().set(newY);
	}

	@Override
	public void postMove(List<Entity> entitiesAtNew) {
		for (Entity entity: entitiesAtNew) {
			if(entity.getType() == EntityType.SWITCH) {
				entity.stepOver();
			}
		}
	}

	@Override
	public boolean stepOver() {
		return false;
	}

	@Override
	public String getImageID() {
		return "Boulder image";
	}
	
	@Override
	 public String getImagePath() {
		return this.image_path;
	}
	
	@Override
	public ArrayList<String> getImageList() {
		return null;
	}


}
