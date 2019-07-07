package unsw.dungeon;

import java.awt.Rectangle;

public interface CollisionHandler {

	 public Rectangle getBounds(String direction);
	 public Entity getObjectByType(String name);
	
}
