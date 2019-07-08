package unsw.dungeon;

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
	
	public static boolean PlayerToBoulderCollision(String direction , Player p , List<Entity> entities) {
		boolean collision = false;
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i).getObjectByType("Boulder object");
			if(e != null) {
				if(p.getBounds(direction).contains(e.getBounds(direction))) {
					collision = true;
					break;
				}
			}
		}
		return collision;
	}
}
