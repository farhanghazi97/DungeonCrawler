package unsw.dungeon;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

public class Door extends Entity {

	private String image_path = "/open_door.png";
	private ArrayList<String> image_list = new ArrayList<String>();
	private int door_id;
	private boolean open = false;
	
//	public Door(int x, int y , int door_id) {
//		super(x, y);
//		this.door_id = door_id;
//	}

	public Door(Dungeon dungeon, int x, int y, int door_id) {
        super(dungeon, x, y);
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
		
	}

	@Override
	public boolean stepOver() {
		//If Player has a key in the collected bag
		Key key = (Key) dungeon.getInventoryEntity(EntityType.KEY);
		if(key != null) {
			if(matchKey(key)) {
				//Key and Door id match
				updateDoorUI(this);
				dungeon.getInventoryEntities().remove(key);
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


	@Override
	public String getImageID() {
		return "Door image";
	}
	
	@Override
	 public String getImagePath() {
		return this.image_path;
	}

	@Override
	public ArrayList<String> getImageList() {
		return image_list;
	}

	// Update the 'door' entity to 'open' status
	private void updateDoorUI(Entity entity) {
		String open_door_image_path = entity.getImagePath();
		Image open_door = new Image(open_door_image_path);
		System.out.println("In update door function");
		ImageView image = dungeon.getImageByEntity(Mediator.getInstance().getImageEntities(), entity);
		image.setImage(open_door);
	}
	
	private boolean matchKey(Key key) {
		if(this.getDoor_id() == key.getKeyID()) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isOpen() {
		return open;
	}

	public void setIs_open(boolean is_open) {
		this.open = is_open;
	}
	public int getDoor_id() {
		return door_id;
	}

	@Override
	public String toString() {
		return "DOOR object [Door ID=" + door_id + ", Open?=" + open + "]";
	}


	
	
}
