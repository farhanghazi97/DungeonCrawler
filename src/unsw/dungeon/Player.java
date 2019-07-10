package unsw.dungeon;

import java.awt.Rectangle;
import java.util.List;

import javafx.scene.layout.GridPane;

/**
 * The player entity
 * @author Robert Clifton-Everest
 *
 */
public class Player extends Entity implements CollisionDetector {

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
    
    public boolean checkCollision (String direction , List<Entity> entities) {
    	if(!this.checkWallCollision(direction, entities) && !this.checkBoulderCollision(direction, entities)) {
    		return false;
    	} else {
    		return true;
    	}
    }
    
    public boolean checkWallCollision(String direction , List<Entity> entities) {
    	if(CollisionHandler.PlayerToWallCollision(direction , this , entities)) {
    		System.out.println("Collision: PLAYER - WALL");
    		return true;
    	} else {
    		return false;
    	}
    }
    
    public boolean checkBoulderCollision(String direction , List<Entity> entities) {
    	if(CollisionHandler.PlayerToBoulderCollision(direction, this, entities)) {
    		System.out.println("Collision: PLAYER - BOULDER");
    		return true;
    	} else {
    		return false;
    	}
    }
    
    @Override
    public Rectangle getBounds(String direction) {
    	return new Rectangle(this.getX(), this.getY() , 32 , 32);
    }
    
    @Override
    public String toString() {
		return String.format("Player object");
    }
    
}
