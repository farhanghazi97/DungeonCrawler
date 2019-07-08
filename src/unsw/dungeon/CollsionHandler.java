package unsw.dungeon;

import java.util.List;

public class CollsionHandler {

	public static boolean WallCollision(String direction , Player e , List<Entity> entities) {
		boolean collision = false;
		for(int i = 0; i < entities.size(); i++) {
			Entity list_entity = entities.get(i).getObjectByType("Wall object");
			if(list_entity != null) {
				if(e.getBounds(direction).contains(list_entity.getBounds(direction))) {
					collision = true;
					break;
				}
			}
		}
		return collision;
	}
	
	public static boolean BoulderToWallCollision(String direction , Boulder b , List<Entity> entities) {
		boolean collision = false;
		for(int i = 0; i < entities.size(); i++) {
			Entity list_entity = entities.get(i).getObjectByType("Wall object");
			if(list_entity != null) {
				if(b.getBounds(direction).contains(list_entity.getBounds(direction))) {
					collision = true;
					break;
				}
			}
		}
		return collision;
	}
	
	public static boolean BoulderToBoulderCollision(String direction , Boulder b , List<Entity> entities) {
		boolean collision = false;
		for(int i = 0; i < entities.size(); i++) {
			Entity list_entity = entities.get(i).getObjectByType("Boulder object");
			if(list_entity != null) {
				if(b.getBounds(direction).contains(list_entity.getBounds(direction))) {
					System.out.println("Here");
					collision = true;
					break;
				}
			}
		}
		return collision;
	}
	
	public static boolean BoulderCollision(String direction , Player e , List<Entity> entities) {
		
		boolean collision = false;
		for(int i = 0; i < entities.size(); i++) {
			Entity list_entity = entities.get(i).getObjectByType("Boulder object");
			if(list_entity != null) {
				if(e.getBounds(direction).contains(list_entity.getBounds(direction)) && (!list_entity.checkBoulderToWallCollision(direction, entities))) {
						if(direction.equals("RIGHT")) {
							// Update boulder position
							list_entity.setX(list_entity.getX() + 1);
							// Update player position
							e.setX(e.getX() - 1);
						} else if (direction.equals("LEFT")) {
							// Update boulder position
							list_entity.setX(list_entity.getX() - 1);
							// Update player position
							e.setX(e.getX() + 1);
						} else if(direction.equals("UP")) {
							// Update boulder position
							list_entity.setY(list_entity.getY() - 1);
							// Update player position
							e.setY(e.getY() + 1);
						} else if (direction.equals("DOWN")) {
							// Update boulder position
							list_entity.setY(list_entity.getY() + 1);
							// Update player position
							e.setY(e.getY() - 1);
						}
						break;
				}
			}
		}
		return collision;
	}
	
}
