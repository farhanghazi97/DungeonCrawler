package unsw.dungeon;

import java.awt.Rectangle;

public class Boulder extends Entity{

	public Boulder(int x , int y) {
		super(x , y);
	}
	
	@Override
    public Boulder getObjectByType(String name) {
    	if(this.toString().equals(name)) {
    		return this;
    	} else {
    		return null;
    	}
    }
	
	@Override
    public String toString() {
		return String.format("Boulder object");
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
	
}
