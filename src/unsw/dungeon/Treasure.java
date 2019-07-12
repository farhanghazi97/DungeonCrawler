package unsw.dungeon;

import java.awt.Rectangle;

public class Treasure extends Entity{

	public Treasure(int x, int y) {
        super(x, y);
    }
	
	@Override
    public String toString() {
		return String.format("Treasure object");
    }
	
	@Override
    public Rectangle getBounds(String direction) {
		return new Rectangle(this.getX(), this.getY() , 32 , 32);
    }
	
	@Override
    public Treasure getObjectByType(String name) {
    	if(this.toString().equals(name)) {
    		return this;
    	} else {
    		return null;
    	}
    }
	
}
