package unsw.dungeon;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Sword extends Entity {
	
	private String image_path = "greatsword_1_new.png";
	private ArrayList<String> image_list = new ArrayList<String>();
	private String pickupSword = "sword.wav"; 
	private String swingSword = "sword_hit.wav";
	
	Media pick_up_sound = new Media(new File(pickupSword).toURI().toString());
	Media swing_sound = new Media(new File(swingSword).toURI().toString());
    
	MediaPlayer pick_up_sword = new MediaPlayer(pick_up_sound);

	
	private int swingsRemaining = 5;
	private boolean collected = false;
	
	public Sword(Dungeon dungeon, int x, int y) {
        super(dungeon, x, y);
    }

    @Override
    public EntityType getType() {
        return EntityType.SWORD;
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
    
	@Override
	public boolean stepOver() {
		System.out.println("In Sword's stepOver");
		this.swingsRemaining = 5;
		//Add sword to collected entities
		Entity tempSword =dungeon.getInventoryEntity(EntityType.SWORD);
		
		if(tempSword != null) {
			//Player already has a sword
			return false;
		}else {
			//Add new sword
			pick_up_sword.play();
			if(dungeon.getInventoryEntities().add(this)) {
				this.collected = true;
				updatePlayerUI();
				System.out.println(this.toString());
				dungeon.removeEntity(this);
				return true;
			}
		}
		return false;
	}
	
	//Swings sword once and reduces swingsRemaining
	public boolean swing(int x, int y) {
		MediaPlayer hit_sword = new MediaPlayer(swing_sound);
		hit_sword.play();
		// Check if enemy is in vicinity
		List<Entity> enemies = dungeon.entitiesInVicinity(x, y, EntityType.ENEMY);
		if (enemies != null) {
			// If true -> remove enemy
			System.out.println(enemies);
			for (Entity enemy : enemies) {
				dungeon.removeEntity(enemy);
			}
		}
		
		swingsRemaining--;
		if(swingsRemaining > 0) {
			System.out.println(this.toString());
			return true;
		}else {
			//Remove sword from player's collected entities
			this.collected = false;
			updatePlayerUI();
			dungeon.getInventoryEntities().remove(this);
			System.out.println(this.toString());
			return false;
		}
	}

	@Override
	public String getImageID() {
		return "Sword image";
	}
	
	@Override
	 public String getImagePath() {
		return this.image_path;
	}

	public int getSwingsRemaining() {
		return swingsRemaining;
	}

	@Override
	public ArrayList<String> getImageList() {
		return image_list;
	}
	
	
	private void updatePlayerUI( ) {
		Player player = dungeon.getPlayer();
		ArrayList<String> images = player.getImageList();
		
		Image humanSword;
		if(collected) {
			humanSword = new Image(images.get(2));
		} else {
			humanSword = new Image(images.get(0));
		}

		ImageView image = dungeon.getImageByEntity(player);
		image.setImage(humanSword);
	}

}
