package unsw.dungeon;

import java.awt.Rectangle;
import java.util.List;

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
	
	public static boolean PlayerToBoulderCollision(String direction , Player p , List<Entity> entities) {
		
		// This function also handles BoulderToBoulderCollision
		
		boolean collision = false;
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i).getObjectByType("Boulder object");
			if(e != null) {
				if(p.getBounds(direction).contains(e.getBounds(direction))) {
					collision = true;
					if(direction.equals("RIGHT")) {
						if(!e.checkBoulderCollision(direction, entities)) {
							e.setX(e.getX() + 1);
						}
					} else if (direction.equals("LEFT")) {
						if(!e.checkBoulderCollision(direction, entities)) {
							e.setX(e.getX() - 1);
						}
					} else if (direction.equals("UP")) {
						if(!e.checkBoulderCollision(direction, entities)) {
							e.setY(e.getY() - 1);
						}
					} else if (direction.equals("DOWN")) {
						if(!e.checkBoulderCollision(direction, entities)) {
							e.setY(e.getY() + 1);
						}
					}
					break;
				}
			}
		}
		return collision;
	}
}
