package unsw.dungeon;

import java.util.ArrayList;
import java.util.List;

public class Door extends Entity {

	private String type = "Door";
	private String image_path = "/open_door.png";
	private ArrayList<String> image_list = new ArrayList<String>();
	
	private int door_id;
	private boolean is_open = false;
	
	public Door(int x, int y , int door_id) {
		super(x, y);
		this.door_id = door_id;
	}

	public int getDoor_id() {
		return door_id;
	}

	public void setDoor_id(int door_id) {
		this.door_id = door_id;
	}

	@Override
	public EntityType getType() {
		return EntityType.DOOR;
	}

	@Override
	public boolean isBlocked(List<Entity> entitiesAtNew) {
		return true;
	}

	@Override
	public void postMove(List<Entity> entitiesAtNew) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean stepOver() {
		Entity key = Mediator.getInstance().getCollected(EntityType.KEY);
		if(key != null) {
			if(matchKey(key)) {
				//Mediator.getInstance().updateDoorUI(this);
				this.setIs_open(true);
				return true;
			} else {
				return false;
			}
		}
		return false;
	}
	
	private boolean matchKey(Entity e) {
		if(this.getDoor_id() == e.getKeyID()) {
			return true;
		} else {
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
	
	public boolean isIs_open() {
		return is_open;
	}

	public void setIs_open(boolean is_open) {
		this.is_open = is_open;
	}

	@Override
	public String toString() {
		return "DOOR object [Door ID=" + door_id + ", Open?=" + is_open + "]";
	}
	
	@Override
	public int getDoorID() {
		return door_id;
	}
	
	@Override
	public int getKeyID() {
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
	public String getImageID() {
		return "Door image";
	}
	
	@Override
	 public String getImagePath() {
		return this.image_path;
	}

	@Override
	public ArrayList<String> getImage_list() {
		return image_list;
	}

	
	
}
