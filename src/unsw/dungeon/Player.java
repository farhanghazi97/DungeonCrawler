package unsw.dungeon;

import java.awt.Rectangle;
import java.util.List;

import javafx.scene.layout.GridPane;

/**
 * The player entity
 * @author Robert Clifton-Everest
 *
 */
public class Player extends Entity implements CollisionHandler {

    private Dungeon dungeon;

    /**
     * Create a player positioned in square (x,y)
     * @param x
     * @param y
     */
    public Player(Dungeon dungeon, int x, int y) {
        super(x, y);
        this.dungeon = dungeon;
    }

    public void moveUp() {
        if (getY() > 0)
            y().set(getY() - 1);
    }

    public void moveDown() {
        if (getY() < dungeon.getHeight() - 1)
            y().set(getY() + 1);
    }

    public void moveLeft() {
        if (getX() > 0)
            x().set(getX() - 1);
    }

    public void moveRight() {
        if (getX() < dungeon.getWidth() - 1)
            x().set(getX() + 1);
    }
    
    public boolean checkWallCollision(String direction , List<Entity> entities) {
    	if(CollisionDetector.WallCollision(direction , this , entities)) {
    		System.out.println("Collision detected!");
    		return true;
    	} else {
    		return false;
    	}
    }
    
    @Override
    public Rectangle getBounds(String direction) {
    	if(direction.equals("RIGHT")) {
    		return new Rectangle(this.getX() + 1 , this.getY() , 32 , 32);
    	} else if(direction.equals("LEFT")) {
    		return new Rectangle(this.getX() - 1 , this.getY() , 32 , 32);
    	} else if(direction.equals("UP")){
    		return new Rectangle(this.getX() , this.getY() - 1, 32 , 32);
    	} else if(direction.equals("DOWN")){
    		return new Rectangle(this.getX() , this.getY() + 1, 32 ,32);
    	} else {
    		return null;
    	}
    }
    
    @Override
    public String toString() {
		return String.format("Player object");
    }
    
    @Override
    public Player getObjectByType(String name) {
    	if(this.toString().equals(name)) {
    		return this;
    	} else {
    		return null;
    	}
    }
    
}
