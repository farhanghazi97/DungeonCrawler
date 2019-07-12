package unsw.dungeon;

import java.awt.Rectangle;
import java.util.List;

import javafx.scene.layout.GridPane;

public class Boulder extends Entity implements CollisionDetector {

	private String type = "BOULDER";
	
	public Boulder(int x , int y) {
		super(x , y);
	}
	
	@Override
    public String toString() {
		return String.format("Boulder object");
    }
	
	@Override
    public Rectangle getBounds(String direction) {
		return new Rectangle(this.getX(), this.getY() , 32 , 32);
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
	public boolean checkWallCollision(String direction , List<Entity> entities) {
		return false;
    }
	
	@Override
	public boolean checkBoulderCollision(GridPane squares , String direction , List<Entity> entities) {
    	if(CollisionHandler.BoulderToBoulderCollision(direction , this , entities)) {
    		System.out.println("Collision: BOULDER - BOULDER");
    		return true;
    	} else {
    		return false;
    	}
    }
	
	public boolean checkBoulderOnPressurePlate(String direction , List<Entity> entities) {
		if(CollisionHandler.BoulderOnPressurePlate(direction , this , entities)) {
			System.out.println("Collision: BOULDER - SWITCH");
			return true;
		} else {
			return false;
		}
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
