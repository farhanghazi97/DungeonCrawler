package unsw.dungeon;

import java.util.ArrayList;
import java.util.List;

import javafx.concurrent.Task;

public class IceBall extends Entity {

	private String imagePath = "/misc_crystal.png";
	private boolean collected = false;
	
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
		Entity tempIceBall =dungeon.getInventoryEntity(EntityType.ICEBALL);
		
		if(tempIceBall != null) {
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
		System.out.println("In activateIceBomb()");
		Entity tempIB =dungeon.getInventoryEntity(EntityType.ICEBALL);
		List<Entity> enemies = dungeon.getEntities(EntityType.ENEMY);
		if (tempIB != null) {
			for (Entity e : enemies) {
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
				System.out.println("Enemies can move now!");
				dungeon.getInventoryEntities().remove(this);
				for (Entity en : enemies) {
					((Enemy) en).setEnemy_stalled(false);
				}
			});
			new Thread(task).start();
		} else {
			return;
		}
	}
}
