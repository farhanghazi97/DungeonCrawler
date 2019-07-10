package unsw.dungeon;

import java.awt.Rectangle;
import java.util.List;

public class Wall extends Entity implements CollisionDetector {

    public Wall(int x, int y) {
        super(x, y);
    }

    @Override
    public String toString() {
		return String.format("Wall object");
    }
    
    @Override
    public Rectangle getBounds(String direction) {
    	if(direction.equals("RIGHT")) {
    		return new Rectangle(this.getX() - 1 , this.getY() , 32 , 32);
    	} else if(direction.equals("LEFT")) {
    		return new Rectangle(this.getX() + 1 , this.getY() , 32 , 32);
    	} else if(direction.equals("UP")){
    		return new Rectangle(this.getX() , this.getY() + 1, 32 , 32);
    	} else if(direction.equals("DOWN")){
    		return new Rectangle(this.getX() , this.getY() - 1, 32 ,32);
    	} else {
    		return null;
    	}
    }
    
    @Override
    public Wall getObjectByType(String name) {
    	if(this.toString().equals(name)) {
    		return this;
    	} else {
    		return null;
    	}
    }

}
