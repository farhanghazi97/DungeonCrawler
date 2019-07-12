package unsw.dungeon;

import java.awt.Rectangle;
import java.io.FileNotFoundException;
import java.util.List;

import javafx.scene.layout.GridPane;

/**
 * The player entity
 * @author Robert Clifton-Everest
 *
 */
public class Player extends Entity implements CollisionDetector {

    private Dungeon dungeon;
    private String type = "PLAYER";
    
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
    
    @Override
    public String toString() {
		return String.format("Player object");
    }
    
    @Override
    public Rectangle getBounds(String direction) {
    	return new Rectangle(this.getX(), this.getY() , 32 , 32);
    }
    
    @Override
    public Player getObjectByType(String name) {
    	if(this.toString().equals(name)) {
    		return this;
    	} else {
    		return null;
    	}
    }
    
    @Override
    public boolean checkWallCollision(String direction , List<Entity> entities) {
    	if(CollisionHandler.PlayerToWallCollision(direction , this , entities)) {
    		System.out.println("Collision: PLAYER - WALL");
    		return true;
    	} else {
    		return false;
    	}
    }
    
    @Override
    public boolean checkBoulderCollision(GridPane squares , String direction , List<Entity> entities) throws FileNotFoundException {
    	if(CollisionHandler.PlayerToBoulderCollision(squares , direction, this, entities)) {
    		System.out.println("Collision: PLAYER - BOULDER");
    		return true;
    	} else {
    		return false;
    	}
    }
    
    public boolean checkCollision (GridPane squares , String direction , List<Entity> entities) throws FileNotFoundException {
    	if(!this.checkWallCollision(direction, entities) && !this.checkBoulderCollision(squares , direction, entities)) {
    		return false;
    	} else {
    		return true;
    	}
    }
    
    public boolean checkTreasureCollision(String direction , List<Entity> entities) {
    	if(CollisionHandler.PlayerToTreasureCollision(direction , this , entities)) {
    		System.out.println("Collision: PLAYER - TREASURE");
    		return true;
    	} else {
    		return false;
    	}
    }

	public Dungeon getDungeon() {
		return dungeon;
	}

	public void setDungeon(Dungeon dungeon) {
		this.dungeon = dungeon;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public void setType(String type) {
		this.type = type;
	}
	
}
