package unsw.dungeon;

import javafx.concurrent.Task;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Bomb extends Entity {
	
	private String imagePath = "/bomb_lit_1.png";
	private boolean isDestroyed = false;
	private ArrayList<String> imageList = new ArrayList<String>(Arrays.asList
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
	public void postMove(List<Entity> entitiesAtNew) {}


	/**
	 * Method to add bomb to player's inventory bag and to remove it from the entities list
	 *
	 * This method ensures the player can carry 1 bomb at a time
	 * @return true if bomb added to inventory succesfully, false otherwise
	 */
	@Override
	public boolean stepOver() {
		Entity bomb = dungeon.getInventoryEntity(EntityType.BOMB);
		if(bomb != null && isDestroyed == false) {
			return false;
		} else {
	    	dungeon.addInventory(this);
			dungeon.removeEntity(this);
			return true;
		}
	}

	/**
	 * Manages bomb image changes and bomb timer
	 * @param time
	 */

	public void startBombSelfDestruct(long time) {
		System.out.println("In self destruct");
		Entity bomb = this;
		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() {
				System.out.println("In call");
				ImageView imageToUpdate = dungeon.getImageByEntity(bomb);
				if (imageToUpdate != null) {
					for (int j = 0; j < imageList.size(); j++) {
						try {
							Thread.sleep(time);
						} catch (InterruptedException e) {
							System.out.println("Thread was interrupted!");
						}
						Image new_state = new Image(imageList.get(j));
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
			System.out.println("in setOn succeded");
			List<Entity> entities_to_remove = dungeon.entitiesInVicinity(
					bomb.getX(), bomb.getY(), EntityType.ENEMY,
					EntityType.BOULDER, EntityType.PLAYER);
			if (entities_to_remove.contains(dungeon.getPlayer())) {
				Entity potion = dungeon.getInventoryEntity(EntityType.POTION);
				// If player does not have potion, bomb effective
				if (potion == null) {
					dungeon.setGameOver(true);
					dungeon.postGameOver();
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
	public void moveTo(int newX, int newY) {}

	@Override
	public String getImageID() {
		return "Bomb image";
	}
	
	@Override
	 public String getImagePath() {
		return this.imagePath;
	}

	@Override
	public ArrayList<String> getImageList() {
		return imageList;
	}
	
}
