package unsw.dungeon;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

public class Door extends Entity {

	private String imagePath = "/open_door.png";
	private ArrayList<String> imageList = new ArrayList<String>();
	private int doorId;
	private boolean open = false;
	
	public Door(Dungeon dungeon, int x, int y, int door_id) {
        super(dungeon, x, y);
        this.doorId = door_id;
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
	public void moveTo(int newX, int newY) {}

	@Override
	public void postMove(List<Entity> entitiesAtNew) {}

	@Override
	public boolean stepOver() {
		//If Player has a key in the collected bag
		Key key = (Key) dungeon.getInventoryEntity(EntityType.KEY);
		if(key != null) {
			if(matchKey(key)) {
				//Key and Door id match
				updateDoorUI(this);
				dungeon.getInventoryEntities().remove(key);
				this.setIsOpen(true);
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
		return this.imagePath;
	}

	@Override
	public ArrayList<String> getImageList() {
		return imageList;
	}

	// Update the 'door' entity to 'open' status
	private void updateDoorUI(Entity entity) {
		System.out.println("In update door function");
		Image openDoorImage = new Image(imagePath);
		ImageView image = dungeon.getImageByEntity(dungeon.getImageEntities(), entity);
		image.setImage(openDoorImage);
	}
	
	private boolean matchKey(Key key) {
		if(this.getDoorId() == key.getKeyID()) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isOpen() {
		return open;
	}

	public void setIsOpen(boolean is_open) {
		this.open = is_open;
	}
	public int getDoorId() {
		return doorId;
	}

	@Override
	public String toString() {
		return "DOOR object [Door ID=" + doorId + ", Open?=" + open + "]";
	}

	
}
