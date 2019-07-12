package unsw.dungeon;

import java.awt.Rectangle;
import java.io.FileNotFoundException;
import java.util.List;

import javafx.scene.layout.GridPane;

public interface CollisionDetector {

	 public Rectangle getBounds(String direction);
	 public Entity getObjectByType(String name);
	 public boolean checkBoulderCollision(GridPane squares , String direction , List<Entity> entities) throws FileNotFoundException;
	 public boolean checkWallCollision(String direction , List<Entity> entities);
	
}
