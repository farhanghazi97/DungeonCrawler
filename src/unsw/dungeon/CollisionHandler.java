package unsw.dungeon;

import java.awt.Rectangle;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Random;

import javafx.beans.property.IntegerProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class CollisionHandler {

	public static boolean PlayerToWallCollision(String direction , Player p , List<Entity> entities) {
		
		boolean collision = false;
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i).getObjectByType("Wall object");
			if(e != null) {
				if(p.getBounds(direction).contains(e.getBounds(direction))) {
					collision = true;
					break;
				}
			}
		}
		return collision;
	}
	
	public static boolean PlayerToTreasureCollision(String direction , Player p , List<Entity> entities) {
		boolean collision = false;
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i).getObjectByType("Treasure object");
			if(e != null) {
				if(p.getBounds(direction).contains(e.getBounds(direction))) {
					collision = true;
					break;
				}
			}
		}
		return collision;
	}
	
	public static boolean BoulderOnPressurePlate(String direction , Boulder b , List<Entity> entities) {
		boolean collision = false;
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i).getObjectByType("Switch object");
			if(e != null) {
				if(e.getBounds(direction).contains(b.getBounds(direction))) {
					collision = true;
					break;
				}
			}
		}
		return collision;
	}
	
	public static boolean BoulderToBoulderCollision(String direction , Boulder b , List<Entity> entities) {
		
		Rectangle r = new Rectangle();
		boolean collision = false;
		
		if(direction.equals("RIGHT")) {
			r = b.getBounds("RIGHT");
			r.setBounds((int) r.getX() + 1, (int)r.getY(), 32, 32);
		} else if(direction.equals("LEFT")) {
			r = b.getBounds("LEFT");
			r.setBounds((int) r.getX() - 1, (int)r.getY(), 32, 32);
		}  else if(direction.equals("UP")) {
			r = b.getBounds("UP");
			r.setBounds((int) r.getX(), (int)r.getY() - 1, 32, 32);
		}  else if(direction.equals("DOWN")) {
			r = b.getBounds("DOWN");
			r.setBounds((int) r.getX(), (int)r.getY() + 1	, 32, 32);
		}
		
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i).getObjectByType("Boulder object");
			if(e != null) {
				if(r.contains(e.getBounds(direction))) {
					collision = true;
					break;
				}
			}
		}
		
		if(!collision) {
			if(direction.equals("RIGHT")) {
				b.setX(b.getX() + 1);
			} else if(direction.equals("LEFT")) {
				b.setX(b.getX() - 1);
			} else if(direction.equals("UP")) {
				b.setY(b.getY() - 1);
			} else if(direction.equals("DOWN")) {
				b.setY(b.getY() + 1);
			}
		}
		
		return collision;
		
	}
	
	public static boolean PlayerToBoulderCollision(GridPane squares , String direction , Player p , List<Entity> entities) throws FileNotFoundException {
		
		// If the player is in "front" of a boulder, the player can move into the position of the boulder
		// (overlaps into boulder at the moment - should not overlap) in order to change its position.
		// Based on the direction the player is facing, the position of the boulder is updated. This 
		// function also calls "BoulderToBoulderCollision" to check if the "current" boulder overlaps
		// into another boulder. If overlap can occur, we restrict the player from moving the boulder.
		// Otherwise, the boulder's position is updated.
		
		// If a boulder is moved into a floor switch, an event is triggered which has a randomized
		// probability of spawning treasure
		
		boolean collision = false; 
		ObjectGenerator o = new ObjectGenerator("boulders.json");
		Dungeon d = p.getDungeon();
		
		int width  = p.getDungeon().getWidth();
		int height = p.getDungeon().getHeight();
		
		Random rand = new Random();
		int r_width      = rand.nextInt(width);
		int r_height     = rand.nextInt(height);
		
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i).getObjectByType("Boulder object");
			if(e != null) {
				if(p.getBounds(direction).contains(e.getBounds(direction))) {
					collision = true;
					if(!e.checkBoulderCollision(squares , direction, entities)) {
						if(e.checkBoulderOnPressurePlate(direction, entities)) {
							if(rand.nextInt(3) == 1) {
								o.GenerateTreasure(entities , squares , r_width , r_height);
							}
						}
					}
					break;
				}
			}
		}
		return collision;
	}
}
