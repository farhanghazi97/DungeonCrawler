package unsw.dungeon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Bomb extends Entity {
	
	private String image_path = "/bomb_lit_1.png";
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
		return String.format("BOMB object | X = %d | Y = %d" , this.getX() , this.getY());
	}

	@Override
	public boolean stepOver() {
		System.out.println("In Bomb's stepOver");
		Entity bomb = Mediator.getInstance().getCollected(EntityType.BOMB);
		if(bomb != null && is_destroyed == false) {
			return false;
		} else {
	    	Mediator.getInstance().collectedEntities.add(this);
	    	Mediator.getInstance().removeEntity(this);
			return true;
		}
	}

	@Override
	public int getKeyID() {
		return -1;
	}
	
	@Override
	public boolean isDoorOpen() {
		return false;
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
