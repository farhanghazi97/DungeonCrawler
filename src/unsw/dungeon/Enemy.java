package unsw.dungeon;

import java.util.ArrayList;
import java.util.List;

public class Enemy extends Entity{
	
	private int difficultyLevel = 1;
	private ArrayList<String> image_list = new ArrayList<String>();

	private String image_path = "/enemy.png";

	public Enemy(Dungeon dungeon, int x, int y) {
        super(dungeon, x, y);
    }
	
	@Override
	public String toString() {
		return "ENEMY object " + this.getX() + " | " + this.getY();
		
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
		
	}

	@Override
	public boolean stepOver() {
		System.out.println("In Enemy's stepOver");
		Entity potion = dungeon.getInventoryEntity(EntityType.POTION);
		if(potion != null) {
			//Player has a potion -> enemy dies
			dungeon.removeEntity(this);
		}else {
			//Player dies -> game over
			dungeon.markGameOver();
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
	public ArrayList<String> getImageList() {
		return image_list;
	}
	
	//OUR AMAZING LOGIC FOR MOVING ENEMY
	@Override
	public void moveTo(int playerX, int playerY){
	
		int enemyX = this.getX();
		int enemyY = this.getY();
		
		//In Efforts to make it harder
		List<Entity> player = dungeon.entitiesInVicinity(enemyX, enemyY, EntityType.PLAYER);
		Entity potion = dungeon.getInventoryEntity(EntityType.POTION);
		if(player.size() == 1 && potion == null) {
			//Player is in vicinity of enemy and has no potion. 
			x().set(playerX);
			y().set(playerY);
			return;
		}
		
		int dirX = playerX - enemyX;
		int dirY = playerY - enemyY;

		double unit_vector = Math.atan2(dirY , dirX);
		
		enemyX = (int) (enemyX + (difficultyLevel * Math.cos(unit_vector)));
		enemyY = (int) (enemyY + (difficultyLevel * Math.sin(unit_vector)));
		
		List<Entity> entitiesAtCurrent = dungeon.getEntities(enemyX, enemyY);
		
		if(dungeon.outsideDungeon(enemyX, enemyY) ) {
			return;
		}

		if(entitiesAtCurrent.size() == 0) {
			if(this.isBlocked(entitiesAtCurrent) == false) {
				x().set(enemyX);
				y().set(enemyY);
				return;
			}
		}
		return;
	}
	
	public int getDifficultyLevel() {
		return difficultyLevel;
	}

	public void setDifficultyLevel(int difficultyLevel) {
		this.difficultyLevel = difficultyLevel;
	}

}
