package unsw.dungeon;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Treasure extends Entity{

	private int treasureCoins = 0;
	private String image_path = "gold_pile.png";
	private String musicFile = "pick-up-item.wav"; 
	
	Media sound = new Media(new File(musicFile).toURI().toString());
    MediaPlayer mediaPlayer = new MediaPlayer(sound);
	
	public Treasure(Dungeon dungeon, int x, int y) {
        super(dungeon, x, y);
    }

    @Override
    public EntityType getType() {
        return EntityType.TREASURE;
    }

    @Override
    public boolean isBlocked(List<Entity> entitiesAtNew) {
        return true;
    }

	@Override
	public void moveTo(int newX, int newY , boolean flag) {
		//Nothing here
	}

    @Override
    public void postMove(List<Entity> entitiesAtNew) {}

	/**
	 * Method to collect and add player's treasure.
	 * @return
	 */
	@Override
	public boolean stepOver() {
    
        mediaPlayer.play();
		
    	treasureCoins++;
    	dungeon.getInventoryEntities().add(this);
		dungeon.removeEntity(this);
		return true;
	}

	@Override
	public String getImageID() {
		return "Treasure image";
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
