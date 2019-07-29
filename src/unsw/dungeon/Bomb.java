package unsw.dungeon;

import javafx.concurrent.Task;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Bomb extends Entity {
	
	private String image_path = "/bomb_lit_1.png";
	private boolean is_destroyed = false;
	private ArrayList<String> image_list = new ArrayList<String>(Arrays.asList
			(
					"/bomb_lit_1.png" ,
					"/bomb_lit_2.png",
					"/bomb_lit_3.png",
					"/bomb_lit_4.png",
					"/BombExploding.png"
			)
	);
	
	public Bomb(Dungeon dungeon, int x, int y) {
        super(dungeon, x, y);
    }
	@Override
	public EntityType getType() {
		return EntityType.BOMB;
	}

	@Override
	public boolean isBlocked(List<Entity> entitiesAtNew) {
		return true;
	}

	@Override
	public void postMove(List<Entity> entitiesAtNew) {

	}
	
	@Override
	public String toString( ) {
		return String.format("BOMB object | X = %d | Y = %d" , this.getX() , this.getY());
	}

	@Override
	public boolean stepOver() {
		System.out.println("In Bomb's stepOver");
		Entity bomb = dungeon.getInventoryEntity(EntityType.BOMB);
		if(bomb != null && is_destroyed == false) {
			return false;
		} else {
	    	dungeon.getInventoryEntities().add(this);
			dungeon.removeEntity(this);
			return true;
		}
	}


	// Manages image changes and bomb timer
	public void startBombSelfDestruct(long time) {
		
		ArrayList<String> images = this.getImageList();
		Entity bomb = this;
		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() {
				ImageView imageToUpdate = dungeon.getImageByEntity(
						Mediator.getInstance().getImageEntities(), bomb);
				if (imageToUpdate != null) {
					for (int j = 0; j < images.size(); j++) {
						try {
							Thread.sleep(time);
						} catch (InterruptedException e) {
							System.out.println("Thread was interrupted!");
						}
						Image new_state = new Image(images.get(j));
						imageToUpdate.setImage(new_state);
						System.out.println("Bomb image updated");
					}
				}
				return null;
			}
		};

		task.setOnSucceeded(e -> {
			// Grab all (in any) enemies , boulders , player in 3 X 3 area surrounding
			// bomb's location

			
			List<Entity> entities_to_remove = dungeon.entitiesInVicinity(
					bomb.getX(), bomb.getY(), EntityType.ENEMY,
					EntityType.BOULDER, EntityType.PLAYER);
			if (entities_to_remove.contains(dungeon.getPlayer())) {
				Entity potion = dungeon.getInventoryEntity(EntityType.POTION);
				// If player does not have potion, bomb effective
				if (potion == null) {
					dungeon.markGameOver();
				} else {
					entities_to_remove.remove(dungeon.getPlayer());
				}
			}
			// Remove all entities in 3 X 3 area
			if (!entities_to_remove.isEmpty()) {
				for (int i = 0; i < entities_to_remove.size(); i++) {
					Entity enemy = entities_to_remove.get(i);
					dungeon.removeEntity(enemy);
				}
			}
			// Remove bomb
			dungeon.removeEntity(bomb);
			System.out.println("Bomb removed");
		});

		new Thread(task).start();
	}

	@Override
	public void moveTo(int newX, int newY) {
		//Nothing here
	}

	@Override
	public String getImageID() {
		return "Bomb image";
	}
	
	@Override
	 public String getImagePath() {
		return this.image_path;
	}

	@Override
	public ArrayList<String> getImageList() {
		return image_list;
	}
	
}
