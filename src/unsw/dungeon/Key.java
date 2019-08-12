package unsw.dungeon;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Key extends Entity {

	private String image_path = "/key.png";
	
	private int keyID;
	private boolean collected = false;
	private String Key = "pick-up-item.wav";
	Media keySound = new Media(new File(Key).toURI().toString());
	MediaPlayer key_sound_player = new MediaPlayer(keySound);

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
	public void moveTo(int newX, int newY , boolean flag) {
		//Nothing here
	}

	@Override
	public void postMove(List<Entity> entitiesAtNew) {}

	/**
	 * Method to add a key to the player's inventory and bag and remove it from the dungeon entites.
	 * This method ensures only one key is added at a time
	 * @return true is key succesfully added
	 */
	@Override
	public boolean stepOver() {
		Entity tempKey = dungeon.getInventoryEntity(EntityType.KEY);
		
		if(tempKey != null) {
			return false;
		}else {
			//Pick up key
			key_sound_player.play();
			if(dungeon.getInventoryEntities().add(this)) {
				this.collected = true;
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
