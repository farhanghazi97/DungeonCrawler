package unsw.dungeon;


import java.util.ArrayList;
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
    private String imageID;
    private String image_path;
    private ArrayList<String> image_list = new ArrayList<String>();
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

    public void moveTo(int newX, int newY){
        x().set(newX);
        y().set(newY);
    }
    
    public abstract void postMove(List<Entity> entitiesAtNew);
    public abstract int getDoorID();
	public abstract int getKeyID();
	public abstract boolean isIs_open();
    public abstract boolean stepOver();    
    public abstract boolean isBlocked(List<Entity> entitiesAtNew);
    
    public abstract EntityType getType();
    public abstract String getImageID();
    public abstract String getImagePath();
	public abstract Entity getObjectByType(String s);
	public abstract ArrayList<String> getImage_list();

}
