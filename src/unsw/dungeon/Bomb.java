package unsw.dungeon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.concurrent.Task;

public class Bomb extends Entity {
	
	private String type = "Bomb";
	private String image_path = "/bomb_lit_1.png";
	private boolean collected = false;
	private boolean is_destroyed = false;
	private ArrayList<String> image_list = new ArrayList<String>(Arrays.asList
			(
					"/bomb_lit_1.png" ,
					"/bomb_lit_2.png",
					"/bomb_lit_3.png",
					"/bomb_lit_4.png",
					"/BombExploding.png"
			)
	);
	
	public Bomb(int x , int y) {
		super(x , y);
	}

	public EntityType getType() {
		return EntityType.BOMB;
	}

	@Override
	public boolean isBlocked(List<Entity> entitiesAtNew) {
		return true;
	}

	@Override
	public void postMove(List<Entity> entitiesAtNew) {

	}
	
	@Override
	public String toString( ) {
		return String.format("Bomb object | X = %d | Y = %d" , this.getX() , this.getY());
	}

	@Override
	public boolean stepOver() {
		System.out.println("In Bomb's stepOver");
		Entity bomb = Mediator.getInstance().getCollected(EntityType.BOMB);
		if(bomb != null && is_destroyed == false) {
			return false;
		} else {
	    	Mediator.getInstance().collectedEntities.add(this);
	    	this.collected = true;
	    	Mediator.getInstance().removeEntity(this);
			return true;
		}
	}
	
	@Override
	public int getDoorID() {
		return -1;
	}
	
	@Override
	public int getKeyID() {
		return -1;
	}
	
	@Override
	public boolean isIs_open() {
		return false;
	}
	
	@Override
	public Entity getObjectByType(String s) {
		if(s.equals(type)) {
			return this;
		} else {
			return null;
		}
	}

	@Override
	public String getImageID() {
		return "Bomb image";
	}
	
	@Override
	 public String getImagePath() {
		return this.image_path;
	}

	@Override
	public ArrayList<String> getImage_list() {
		return image_list;
	}
	
	

}
