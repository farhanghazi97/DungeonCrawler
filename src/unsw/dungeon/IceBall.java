package unsw.dungeon;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.concurrent.Task;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class IceBall extends Entity {

	private String imagePath = "/misc_crystal.png";
	private boolean collected = false;
	
	private String ice_bomb_trigger = "ice.wav"; 
	
	Media iceSound = new Media(new File(ice_bomb_trigger).toURI().toString());
    MediaPlayer ice_sound_player = new MediaPlayer(iceSound);
	
	public IceBall(Dungeon dungeon, int x, int y) {
		super(dungeon, x, y);
		
	}

	@Override
	public void moveTo(int newX, int newY , boolean flag) {
		// Not implemented
	}

	@Override
	public void postMove(List<Entity> entitiesAtNew) {
		// Not implemented
	}

	@Override
	public boolean stepOver() {
		Entity tempIB =dungeon.getInventoryEntity(EntityType.ICEBALL);
		
		if(tempIB != null) {
			//Player already has a ice ball
			return false;
		}else {
			//Add new iceball
			if(dungeon.getInventoryEntities().add(this)) {
				this.collected = true;
				dungeon.removeEntity(this);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isBlocked(List<Entity> entitiesAtNew) {
		return true;
	}

	@Override
	public EntityType getType() {
		return EntityType.ICEBALL;
	}

	@Override
	public String getImageID() {
		return "IceBall Image";
	}

	@Override
	public String getImagePath() {
		return this.imagePath;
	}

	@Override
	public ArrayList<String> getImageList() {
		return null;
	}
	
	
	public void activateIceBomb(long time) {

		ice_sound_player.play();
		System.out.println("In activateIceBomb()");

		Entity tempIB =dungeon.getInventoryEntity(EntityType.ICEBALL);
		List<Entity> enemies = dungeon.getEntities(EntityType.ENEMY);
		if (tempIB != null) {
			for (Entity e : enemies) {
				ImageView imageToUpdate = dungeon.getImageByEntity(e);
				imageToUpdate.setImage(new Image(e.getImagePath()));
				((Enemy) e).setEnemy_stalled(true);
			}
			Task<Void> task = new Task<Void>() {
				@Override
				protected Void call() {
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							System.out.println("Thread was interrupted!");
						}
						return null;
					}
			};
			task.setOnSucceeded(e -> {
				dungeon.getInventoryEntities().remove(this);
				for (Entity en : enemies) {
					ArrayList<String> images = en.getImageList();
					((Enemy) en).setEnemy_stalled(false);
					ImageView imageToUpdate = dungeon.getImageByEntity(en);
					imageToUpdate.setImage(new Image(images.get(0)));
				}
			});
			new Thread(task).start();
		} 
	}
}
