package unsw.dungeon;

import java.util.List;

public class CollisionDetector {

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
}
