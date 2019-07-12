package unsw.dungeon;

import java.awt.Rectangle;

public class PressurePlate extends Entity {

	public PressurePlate(int x, int y) {
        super(x, y);
    }

    @Override
    public String toString() {
		return String.format("Switch object");
    }
    
    @Override
    public Rectangle getBounds(String direction) {
    	return new Rectangle(this.getX() , this.getY() , 32 , 32);
    }
    
    @Override
    public PressurePlate getObjectByType(String name) {
    	if(this.toString().equals(name)) {
    		return this;
    	} else {
    		return null;
    	}
    }
	
}
