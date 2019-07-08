package unsw.dungeon;

import java.awt.Rectangle;
import java.util.List;

public class Boulder extends Entity implements CollisionDetector {

	public Boulder(int x , int y) {
		super(x , y);
	}
	
	@Override
	public boolean checkWallCollision(String direction , List<Entity> entities) {
    	if(CollsionHandler.BoulderToWallCollision(direction , this , entities)) {
    		System.out.println("Boulder collision detected with WALL!");
    		return true;
    	} else {
    		return false;
    	}
    }
	
	@Override
	public boolean checkBouldertoBoulderCollision(String direction , List<Entity> entities) {
		if(CollsionHandler.BoulderToBoulderCollision(direction, this, entities)) {
			System.out.println("Boulder collision with boulder");
			return true;
		}  else {
			return false;
		}
	}
	
	@Override
	public boolean checkBoulderToWallCollision(String direction , List<Entity> entities) {
		if(CollsionHandler.BoulderToWallCollision(direction, this, entities)) {
			System.out.println("Boulder collision with wall");
			return true;
		}  else {
			return false;
		}
	}
	
	@Override
    public Rectangle getBounds(String direction) {
		return new Rectangle(this.getX(), this.getY() , 32 , 32);
    }
    
    @Override
    public String toString() {
		return String.format("Boulder object");
    }
    
    @Override
    public Boulder getObjectByType(String name) {
    	if(this.toString().equals(name)) {
    		return this;
    	} else {
    		return null;
    	}
    }
	
}
