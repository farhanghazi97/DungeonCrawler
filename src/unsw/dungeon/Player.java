package unsw.dungeon;

import java.awt.Rectangle;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Bounds;
import javafx.scene.Node;

import javafx.scene.layout.GridPane;

/**
 * The player entity
 * @author Robert Clifton-Everest
 *
 */
public class Player extends Entity {
	
    private Dungeon dungeon;
    private String type = "Player";
    private String image_path = "/human_new.png";
    private ArrayList<String> image_list = new ArrayList<String>();
    
    /**
     * Create a player positioned in square (x,y)
     * @param x
     * @param y
     */
    public Player(Dungeon dungeon, int x, int y) {
        super(x, y);
        this.dungeon = dungeon;
    }

	//private List

	@Override
	public EntityType getType(){return EntityType.PLAYER;}

	@Override
	public boolean isBlocked(List<Entity> entitiesAtNew){
		for (Entity entity : entitiesAtNew) {
			if (entity.getType()==EntityType.WALL){
				return true;
			} else if(entity.getType() == EntityType.DOOR && !entity.isIs_open()) {
				return true;
			} else if(entity.getType() == EntityType.DOOR && entity.isIs_open()) {
				return false;
			}
			
		}
		return false;
	}

	@Override
	public void postMove(List<Entity> entitiesAtNew){
		for (Entity entity : entitiesAtNew) {
			if (entity.getType()==EntityType.EXIT){
				Mediator.getInstance().markGameOver();
			}
		}

	}

	@Override
	public boolean stepOver() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeEntity() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void generateEntity() {
		// TODO Auto-generated method stub
		
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
		return "Player image";
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
