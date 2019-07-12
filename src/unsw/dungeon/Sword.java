package unsw.dungeon;

import java.awt.Rectangle;

public class Sword extends Entity {
	
	//Create a sword positioned in x,y
	public Sword(int x, int y) {
        super(x, y);
    }
	
	@Override
    public String toString() {
		return String.format("Sword object");
    }
	
	@Override
    public Sword getObjectByType(String name) {
    	if(this.toString().equals(name)) {
    		return this;
    	} else {
    		return null;
    	}
    }
	
	@Override
    public Rectangle getBounds(String direction) {
		return new Rectangle(this.getX(), this.getY() , 32 , 32);
    }
	
	
	
	
}
