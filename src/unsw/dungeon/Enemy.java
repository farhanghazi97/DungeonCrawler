package unsw.dungeon;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Enemy extends Entity{
	
	private int difficultyLevel = 1;

	private boolean enemyStalled = false;
	private ArrayList<String> imageList = new ArrayList<String>(Arrays.asList("/deep_elf_master_archer.png"));
	private String imagePath = "/enemy.png";
	private String enemyAltImage = "/ice_form.png";
	
	private String gameOver = "game_over.wav";
	Media gameOverSound = new Media(new File(gameOver).toURI().toString());
	MediaPlayer game_over_sound = new MediaPlayer(gameOverSound);

	private boolean enemy_stalled = false;


	public Enemy(Dungeon dungeon, int x, int y) {
        super(dungeon, x, y);
    }
	
	@Override
	public EntityType getType() {
		return EntityType.ENEMY;
	}

	/**
	 * Method to check if enemy is blocked by entitesAtNew
	 * @param entitiesAtNew
	 * @return true if blocked, false otherwise
	 */
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
	public void postMove(List<Entity> entitiesAtNew) {}

	/**
	 * Method to perform the required action if a player moves on an enemy
	 * @return true
	 */
	@Override
	public boolean stepOver() {
		Entity potion = dungeon.getInventoryEntity(EntityType.POTION);
		if(potion != null) {
			//Player has a potion -> enemy dies
			dungeon.removeEntity(this);
		}else {
			//Player dies -> game over
			game_over_sound.play();
			dungeon.setGameOver(true);
		}
		return true;
	}

	@Override
	public String getImageID() {
		return "Enemy image";
	}
	
	@Override
	public String getImagePath() {
		return this.enemyAltImage;
	}

	@Override
	public ArrayList<String> getImageList() {
		return imageList;

	}
	

	/**
	 * Method to advance the enemy closer to the player's location (playerX, playerY)
	 *
	 * This method uses some math logic and tries to advance enemy closer to player.
	 * Site Referenced (https://stackoverflow.com/questions/2625021/game-enemy-move-towards-player)
	 *
	 * 1) If player is in vicinity of enemy, the enemy will try to move to the player's new location
	 * 2) Else, the enemy will try to move closer to player
	 *
	 * The difficulty level variable is responsible for number of sqaures the enemy can "jump" through so as to
	 * get to the player
	 * @param playerX
	 * @param playerY
	 */
	@Override
	public void moveTo(int playerX, int playerY , boolean flag){
	
		if(enemyStalled == false) {
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
		} else {
			return;
		}
	}

	public void setDifficultyLevel(int difficultyLevel) {
		this.difficultyLevel = difficultyLevel;
	}

	public boolean isEnemy_stalled() {
		return enemyStalled;
	}

	public void setEnemy_stalled(boolean enemy_stalled) {
		this.enemyStalled = enemy_stalled;
	}

}
