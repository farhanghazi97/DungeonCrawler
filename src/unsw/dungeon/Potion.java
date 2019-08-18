package unsw.dungeon;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.concurrent.Task;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Potion extends Entity {

	private boolean collected = false;
	private String image_path = "/brilliant_blue_new.png";
	private String musicFile = "potion.wav"; 
	
	Media sound = new Media(new File(musicFile).toURI().toString());
    MediaPlayer mediaPlayer = new MediaPlayer(sound);

	public Potion(Dungeon dungeon, int x, int y) {
        super(dungeon, x, y);
    }

    @Override
    public EntityType getType() {
        return EntityType.POTION;
    }

    @Override
    public boolean isBlocked(List<Entity> entitiesAtNew) {
        return true;
    }

    @Override
    public void postMove(List<Entity> entitiesAtNew) {}

    @Override
	public void moveTo(int newX, int newY , boolean flag) {
		//Nothing here
	}

	/**
	 * Method to add a potion to the player's inventory bag and perform required action on it
	 * @return true if potion successfully added, false otherwise
	 */
    @Override
	public boolean stepOver() { 
		Entity tempPotion = dungeon.getInventoryEntity(EntityType.POTION);

		if(tempPotion != null ) {
			//Player already has a potion
			return false;
		}else {
			mediaPlayer.play();
			//Add new potion
			if(dungeon.getInventoryEntities().add(this)) {
				collected = true;
				this.updatePlayerUI();
				//Start potion timer function
				startSelfDestruct(6000);
				dungeon.removeEntity(this);
				return true;
			}
		}
		return false;
	}

	/**
	 * Method to manage potion usage timer of ~ 5 seconds from time of pickup
	 * @param time
	 */
    private void startSelfDestruct(long time) {
		Player entity = dungeon.getPlayer();
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() {
                try {
                    Thread.sleep(time);
                } catch (InterruptedException e) {
                }
                return null;
            }
        };
       
        task.setOnSucceeded(e -> {
        	dungeon.getInventoryEntities().remove(this);
        	collected = false;
            this.updatePlayerUI();
        });

        new Thread(task).start();
    }

	@Override
	public String getImageID() {
		return "Potion image";
	}
	
	@Override
	 public String getImagePath() {
		return this.image_path;
	}

	@Override
	public ArrayList<String> getImageList() {
		return null;
	}

	/**
	 * Method to update UI as potion is collected
	 */
	private void updatePlayerUI() {
		Player player = dungeon.getPlayer();
		ArrayList<String> images = player.getImageList();
		
		Image potion_effect;
		if(collected) {
			potion_effect = new Image(images.get(1));
		} else {
			 potion_effect = new Image(images.get(0));
		}
		
		ImageView image = dungeon.getImageByEntity(player);
		image.setImage(potion_effect);
	}

}
