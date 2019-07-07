package unsw.dungeon;

import java.awt.Rectangle;

public class Wall extends Entity implements CollisionHandler {

    public Wall(int x, int y) {
        super(x, y);
    }

    @Override
    public Rectangle getBounds(String direction) {
		return new Rectangle(this.getX(), this.getY() , 32 , 32);
    }
    
    @Override
    public String toString() {
		return String.format("Wall object");
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
