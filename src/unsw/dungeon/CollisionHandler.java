package unsw.dungeon;

import java.awt.Rectangle;
import java.util.List;

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
		
		return collision;
		
	}
	
	public static boolean PlayerToBoulderCollision(GridPane squares , String direction , Player p , List<Entity> entities) {
		
		// If the player is in "front" of a boulder, the player can move into the position of the boulder
		// (overlaps into boulder at the moment - should not overlap) in order to change its position.
		// Based on the direction the player is facing, the position of the boulder is updated. This 
		// function also calls "BoulderToBoulderCollision" to check if the "current" boulder overlaps
		// into another boulder. If overlap can occur, we restrict the player from moving the boulder.
		// Otherwise, the boulder's position is updated.
		
		// If a boulder is moved into a floor switch, an event is triggered (new tresure is spawned)
		
		boolean collision = false; 
		ObjectGenerator o = new ObjectGenerator();
		
		int width = p.getDungeon().getWidth();
		int height = p.getDungeon().getHeight();
		
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i).getObjectByType("Boulder object");
			if(e != null) {
				if(p.getBounds(direction).contains(e.getBounds(direction))) {
					collision = true;
					if(direction.equals("RIGHT")) {
						if(!e.checkBoulderCollision(squares , direction, entities)) {
							e.setX(e.getX() + 1);
							if(e.checkBoulderOnPressurePlate(direction, entities)) {
								o.GenerateTreasure(squares , width , height);
							}
						}
					} else if (direction.equals("LEFT")) {
						if(!e.checkBoulderCollision(squares , direction, entities)) {
							e.setX(e.getX() - 1);
							if(e.checkBoulderOnPressurePlate(direction, entities)) {
								o.GenerateTreasure(squares , width , height);
							}
						}
					} else if (direction.equals("UP")) {
						if(!e.checkBoulderCollision(squares , direction, entities)) {
							e.setY(e.getY() - 1);
							if(e.checkBoulderOnPressurePlate(direction, entities)) {
								o.GenerateTreasure(squares , width , height);
							}
						}
					} else if (direction.equals("DOWN")) {
						if(!e.checkBoulderCollision(squares , direction, entities)) {
							e.setY(e.getY() + 1);
							if(e.checkBoulderOnPressurePlate(direction, entities)) {
								o.GenerateTreasure(squares , width , height);
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
