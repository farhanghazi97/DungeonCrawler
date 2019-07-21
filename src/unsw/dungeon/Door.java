package unsw.dungeon;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

public class Door extends Entity {

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
	public void moveTo(int newX, int newY) {
		//Nothing here
	}

	@Override
	public void postMove(List<Entity> entitiesAtNew) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean stepOver() {
		//If Player has a key in the collected bag
		Key key = (Key) Mediator.getInstance().getCollected(EntityType.KEY);
		if(key != null) {
			if(matchKey(key)) {
				//Key and Door id match
				updateDoorUI(this);
				Mediator.getInstance().collectedEntities.remove(key);
				this.setIs_open(true);
				return true;
			} else {
				//Key and door ID do not match
				return false;
			}
		}
		//Returning false if player has no key in collected bag
		return false;
	}


	// Update the 'door' entity to 'open' status
	private void updateDoorUI(Entity entity) {
		String open_door_image_path = entity.getImagePath();
		Image open_door = new Image(open_door_image_path);
		System.out.println("In update door function");
		ImageView image = MediatorHelper.getImageByEntity(Mediator.getInstance().getImageEntities(), entity);
		image.setImage(open_door);
	}
	
	private boolean matchKey(Key key) {
		if(this.getDoor_id() == key.getKeyID()) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isDoorOpen() {
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
