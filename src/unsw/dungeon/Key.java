package unsw.dungeon;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleIntegerProperty;

public class Key extends Entity {

	private String image_path = "/key.png";
	
	private int keyID;
	private boolean collected = false;

	public Key(Dungeon dungeon, int x, int y, int keyId) {
        super(dungeon, x, y);
        this.keyID = keyId;
    }

	@Override
	public EntityType getType() {
		return EntityType.KEY;
	}

	@Override
	public boolean isBlocked(List<Entity> entitiesAtNew) {
		return false;
	}

	@Override
	public void moveTo(int newX, int newY) {}

	@Override
	public void postMove(List<Entity> entitiesAtNew) {}

	@Override
	public boolean stepOver() {
		//Add sword to collected entities
		Entity tempKey = dungeon.getInventoryEntity(EntityType.KEY);
		
		if(tempKey != null) {
			//Player already has a key
			return false;
		}else {
			//Pick up key
			if(dungeon.getInventoryEntities().add(this)) {
				this.collected = true;
				System.out.println(this.toString());
				dungeon.removeEntity(this);
				return true;
			}
		}
		return false;
	}
	
	public int getKeyID() {
		return keyID;
	}
	
	@Override
	public String getImageID() {
		return "Key image";
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
