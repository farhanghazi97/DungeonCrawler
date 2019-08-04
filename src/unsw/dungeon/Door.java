package unsw.dungeon;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

public class Door extends Entity {

	private String imagePath = "/open_door.png";
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
	public void moveTo(int newX, int newY , boolean flag) {}

	@Override
	public void postMove(List<Entity> entitiesAtNew) {}

	/**
	 * Method to check for key and door match, if the player has a key.
	 * If id matches, door is opened.
	 * @return true if id matches and false otherwise
	 */
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
		return null;
	}

	/**
	 * Update the 'door' entity to 'open' status change UI to openDoor
	 * @param entity
	 */
	private void updateDoorUI(Entity entity) {
		System.out.println("In update door function");
		Image openDoorImage = new Image(imagePath);
		ImageView image = dungeon.getImageByEntity(entity);
		image.setImage(openDoorImage);
	}

	/**
	 * Method to check if door and key id match
	 * @param key
	 * @return true if it matches and false otherwise
	 */
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

	
}
