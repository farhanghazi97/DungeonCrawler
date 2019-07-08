package unsw.dungeon;

import java.awt.Rectangle;

public interface CollisionDetector {

	 public Rectangle getBounds(String direction);
	 public Entity getObjectByType(String name);
	
}
