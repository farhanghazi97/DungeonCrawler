package unsw.dungeon;

import java.util.ArrayList;
import java.util.List;

public class Enemy extends Entity{

	private ArrayList<String> image_list = new ArrayList<String>();
	
	public Enemy(int x, int y) {
		super(x, y);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "ENEMY object";
		
	}
	
	@Override
	public EntityType getType() {
		return EntityType.ENEMY;
	}

	@Override
	public boolean isBlocked(List<Entity> entitiesAtNew) {
		for (Entity entity : entitiesAtNew) {
			if (entity.getType()==EntityType.WALL || entity.getType() == EntityType.BOULDER || entity.getType() == EntityType.ENEMY){
				return true;
			}
		}
		return false;
	}

	@Override
	public void postMove(List<Entity> entitiesAtNew) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean stepOver() {
		System.out.println("In Enemy's stepOver");
		Entity potion = Mediator.getInstance().getCollected(EntityType.POTION);
		if(potion != null) {
			//Player has a potion -> enemy dies
			Mediator.getInstance().removeEntity(this);
		}else {
			//Player dies -> game over
			Mediator.getInstance().markGameOver();
		}
		return true;
	}

	@Override
	public String getImageID() {
		return "Enemy image";
	}
	
	@Override
	 public String getImagePath() {
		return "";
	}

	@Override
	public ArrayList<String> getImage_list() {
		return image_list;
	}
	
	
	public boolean moveEnemy(int playerX, int playerY) {
		System.out.println("Enemy: In moveEnemy ");
		int enemyX = Math.abs(playerX - this.getX());
		int enemyY = Math.abs(playerY - this.getY());
		System.out.println("Player is at :" + playerX + " "+ playerY);
		System.out.println("Enemy move to :" + enemyX + " "+ enemyY);
		if(MediatorHelper.outsideDungeon(Mediator.getInstance().getDungeon(), enemyX, enemyY) ) {
			return false;
		}
		List<Entity> entitiesAtCurrent = MediatorHelper.getEntities(Mediator.getInstance().getDungeon(), enemyX, enemyY);
		if(entitiesAtCurrent.size() == 0 && this.isBlocked(entitiesAtCurrent) == false) {
			
			this.moveTo(enemyX, enemyY);
			System.out.println("Move enemy now!");
			return true;
		}	
		return false;
		
	}
}
