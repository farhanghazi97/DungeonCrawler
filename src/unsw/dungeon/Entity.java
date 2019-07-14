package unsw.dungeon;

import java.awt.Rectangle;
import java.io.FileNotFoundException;
import java.util.List;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * An entity in the dungeon.
 * @author Robert Clifton-Everest
 *
 */
public abstract class Entity{

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




    public abstract EntityType getType();
    public abstract boolean isBlocked(List<Entity> entitiesAtNew);

    public void moveTo(int newX, int newY){
        x().set(newX);
        y().set(newY);
    }
    
	public void setType(String type) {
		this.type = type;
	}
     
    public abstract void postMove(List<Entity> entitiesAtNew);
    
    public abstract boolean stepOver();
    
    public abstract void removeEntity();
    
    public abstract void generateEntity();

}
