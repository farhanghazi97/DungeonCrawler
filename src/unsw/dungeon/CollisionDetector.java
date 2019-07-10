package unsw.dungeon;

import java.awt.Rectangle;
import java.util.List;

public interface CollisionDetector {

	 public Rectangle getBounds(String direction);
	 public Entity getObjectByType(String name);
	 public boolean checkBoulderCollision(String direction , List<Entity> entities);
	 public boolean checkWallCollision(String direction , List<Entity> entities);
	
}
