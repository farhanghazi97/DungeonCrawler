package unsw.dungeon;


import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;


/**
 * An entity in the dungeon.
 * @author Robert Clifton-Everest
 *
 * Entity Design:
 * The entity class is an abstract class and concreteEntities inherit it and override it's methods
 */
public abstract class Entity {
	
    // IntegerProperty is used so that changes to the entities position can be
    // externally observed.
	public Dungeon dungeon;
    private IntegerProperty x, y;
    /**
     * Create an entity positioned in square (x,y)
     * @param x
     * @param y
     */
    public Entity(Dungeon dungeon, int x, int y) {
    	
    	this.dungeon = dungeon;
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

    /**
     * Move an entityToMove to new location (newX, newY)
     * @param newX
     * @param newY
     */
    public abstract void moveTo(int newX, int newY , boolean flag);

    /**
     * Method called after an entityToMove moves to check for any operations that are to be done on entitiesAtNew after
     * the move is completed
     * @param entitiesAtNew are the entities at the new location
     */
    public abstract void postMove(List<Entity> entitiesAtNew);

    /**
     * Method called when an entityToMove is at the same location as another entity.
     * Eg: If player moves to location (x1, y1), then a stepOver is called on all entities at location (x1,y1)
     * since player has "stepped over" that entity
     * @return
     */
    public abstract boolean stepOver();

    /**
     * Method to check if an entityToMove is blocked by entities at the new location
     * @param entitiesAtNew
     * @return true if blocked, false otherwise
     */
    public abstract boolean isBlocked(List<Entity> entitiesAtNew);

    public abstract EntityType getType();
    public abstract String getImageID();
    public abstract String getImagePath();
	public abstract ArrayList<String> getImageList();


}
