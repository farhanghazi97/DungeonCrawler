package unsw.dungeon;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class MediatorHelper {

    // Returns all entities on (x,y) coordinates
    public static List<Entity> getEntities(Dungeon dungeon, int x, int y) {
        List<Entity> list = new LinkedList<>();
        for (Entity entity : dungeon.getEntities()) {
            if (entity.getX() == x && entity.getY() == y) {
                list.add(entity);
            }
        }
        return list;
    }

    // Returns a specific type of entity class if it at a given (x,y) coordinate
    public static List<Entity> getEntities(Dungeon dungeon, int x, int y, Class clazz) {
        List<Entity> list = new LinkedList<>();
        for (Entity entity : dungeon.getEntities()) {
            if (entity.getX() == x && entity.getY() == y && clazz.isInstance(entity)) {
                list.add(entity);
            }
        }
        return list;
    }

    // Returns a list of entities of type "type" if they are in adjacent squares
    //Modified to accepts a vararg of EntityTypes.

    public static List<Entity> entitiesInVicinity(Dungeon dungeon, int x, int y, EntityType... type) {
        List<Entity> list = new LinkedList<>();

        for (EntityType aType:
             type) {
            for (Entity entity : dungeon.getEntities()) {
                if (entity.getType() == aType && ((entity.getX() == x + 1 && entity.getY() == y)
                        || (entity.getX() == x + 1 && entity.getY() == y - 1)
                        || (entity.getX() == x + 1 && entity.getY() == y + 1)
                        || (entity.getX() == x && entity.getY() == y + 1) || (entity.getX() == x && entity.getY() == y - 1)
                        || (entity.getX() == x - 1 && entity.getY() == y)
                        || (entity.getX() == x - 1 && entity.getY() == y - 1)
                        || (entity.getX() == x + 1 && entity.getY() == y + 1)
                        || (entity.getX() == x && entity.getY() == y))) {
                    list.add(entity);
                }
            }
        }
        return list;
    }

    // Returns true if the new coordinates given are outside
    // the boundaries of the dungeon
    public static boolean outsideDungeon(Dungeon dungeon, int newX, int newY) {
        int width = dungeon.getWidth();
        int height = dungeon.getHeight();
        Player p = dungeon.getPlayer();
        System.out.println("NEW: "+newX+" "+newY);
        System.out.println("CURRENT "+p.getX() + " " + p.getY());
        if(newX == -1|| newY == -1) {
        	//If there was an entity , and it was destroyed, then coordinates change to -1
        	return true;
        }
        if (newX + 1 > width || newY + 1 > height) {
            return true;
        }
        return false;
    }

    // Checks if there is a door in front of player
    public static List<Entity> doorInFront(Dungeon dungeon, int x, int y) {
        List<Entity> list = new LinkedList<>();
        for (Entity entity : dungeon.getEntities()) {
            if (entity.getType() == EntityType.DOOR) {
                if (entity.getY() == y - 1 && entity.getX() == x) {
                    list.add(entity);
                }
            }
        }
        return list;
    }

    public static Pair getUniqueSpawnLocation(Dungeon dungeon, int x, int y) {
        Random rand = new Random();
        int rand_x = rand.nextInt(x);
        int rand_y = rand.nextInt(y);
        List<Entity> entitiesAtXY = MediatorHelper.getEntities(dungeon, rand_x, rand_y);
        if (entitiesAtXY.size() == 0) {
            return new Pair(rand_x, rand_y);
        }
        return null;
    }
}
