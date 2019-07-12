package unsw.dungeon;

import java.awt.Rectangle;
import java.io.FileNotFoundException;
import java.util.List;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.layout.GridPane;

/**
 * An entity in the dungeon.
 * @author Robert Clifton-Everest
 *
 */
public class Entity implements CollisionDetector {

    // IntegerProperty is used so that changes to the entities position can be
    // externally observed.
    private IntegerProperty x, y;
    private String type = "ENTITY";
    
    /**
     * Create an entity positioned in square (x,y)
     * @param x
     * @param y
     */
    public Entity(int x, int y) {
        this.x = new SimpleIntegerProperty(x);
        this.y = new SimpleIntegerProperty(y);
    }

    public IntegerProperty x() {
        return x;
    }

    public IntegerProperty y() {
        return y;
    }

    public int getY() {
        return y().get();
    }

    public int getX() {
        return x().get();
    }

    public void setY(int y) {
    	 y().set(y);
    }
    
    public void setX(int x) {
    	x().set(x);
    }

    public Entity getObjectByType(String name) {
    	if(this.toString().equals(name)) {
    		return this;
    	} else {
    		return null;
    	}
    }
    
    public boolean checkWallCollision(String direction , List<Entity> entities) {
		return false;
    }
    
	public boolean checkBoulderCollision(GridPane squares, String direction, List<Entity> entities) throws FileNotFoundException {
		return false;
	}
    
    public boolean checkBoulderOnPressurePlate(String direction , List<Entity> entities) {
		return false;
	}
    
    public Rectangle getBounds(String direction) {
		return null;
    }

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
     
}
