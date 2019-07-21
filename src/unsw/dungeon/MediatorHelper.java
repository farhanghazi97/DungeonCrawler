package unsw.dungeon;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class MediatorHelper {

    // Returns all entities on (x,y) coordinates
    public static List<Entity> getEntities(int x, int y) {
        Dungeon dungeon = Mediator.getInstance().getDungeon();
        List<Entity> list = new LinkedList<>();
        for (Entity entity : dungeon.getEntities()) {
            if (entity.getX() == x && entity.getY() == y) {
                list.add(entity);
            }
        }
        return list;
    }

    // Returns a specific type of entity class if it at a given (x,y) coordinate
    public static List<Entity> getEntities(int x, int y, Class clazz) {
        Dungeon dungeon = Mediator.getInstance().getDungeon();
        List<Entity> list = new LinkedList<>();
        for (Entity entity : dungeon.getEntities()) {
            if (entity.getX() == x && entity.getY() == y && clazz.isInstance(entity)) {
                list.add(entity);
            }
        }
        return list;
    }

    // UI FUNCTIONS
    // Removes UI element and object corresponding to given entity
    public static void removeEntity(Entity entity) {

        List<ImageView> imageEntities = Mediator.getInstance().getImageEntities();
        GridPane squares = Mediator.getInstance().getSquares();
        Dungeon dungeon = Mediator.getInstance().getDungeon();

        System.out.println("In remove entity function " + entity);
        for (int i = 0; i < imageEntities.size(); i++) {
            ImageView image = imageEntities.get(i);
            // Map GridPane co-ords to entity co-ords
            if (GridPane.getColumnIndex(image) == entity.getX() && GridPane.getRowIndex(image) == entity.getY()) {
                if (image.getId().equals(entity.getImageID())) {
                    //Removing from screen
                    squares.getChildren().remove(image);
                }
            }
        }
        // To remove the object
        if (dungeon.getEntities().contains(entity)) {
            dungeon.getEntities().remove(entity);
        }
    }

    // Returns a list of entities of type "type" if they are in adjacent squares
    //Modified to accepts a vararg of EntityTypes.

    public static List<Entity> entitiesInVicinity(int x, int y, EntityType... type) {
        Dungeon dungeon = Mediator.getInstance().getDungeon();
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
    public static boolean outsideDungeon(int newX, int newY) {
        Dungeon dungeon = Mediator.getInstance().getDungeon();
        int width = dungeon.getWidth();
        int height = dungeon.getHeight();
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
    public static List<Entity> doorInFront(int x, int y) {
        Dungeon dungeon = Mediator.getInstance().getDungeon();
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

    public static Pair getUniqueSpawnLocation(int x, int y) {
        Dungeon dungeon = Mediator.getInstance().getDungeon();
        Random rand = new Random();
        int rand_x = rand.nextInt(x);
        int rand_y = rand.nextInt(y);
        List<Entity> entitiesAtXY = MediatorHelper.getEntities(rand_x, rand_y);
        if (entitiesAtXY.size() == 0) {
            return new Pair(rand_x, rand_y);
        }
        return null;
    }

    // Returns entity if the player already has an entity of given type
    public static List<Entity> getEntityOfType(EntityType entityType) {
        Dungeon dungeon = Mediator.getInstance().getDungeon();
        List<Entity> list = new LinkedList<>();
        for (Entity entity : dungeon.getEntities()) {
            if (entity.getType() == entityType) {
                list.add(entity);
            }
        }
        return list;
    }


    // UI
    public static ImageView getImageByEntity(List<ImageView> entities, Entity e) {
        ImageView image = new ImageView();
        for (int i = 0; i < entities.size(); i++) {
            image = entities.get(i);
            if (GridPane.getColumnIndex(image) == e.getX() && GridPane.getRowIndex(image) == e.getY()) {
                if (image.getId().equals(e.getImageID())) {
                    break;
                }
            }
        }
        return image;
    }


    // Method to generate a new entity in the maze
    public static void generateObject(EntityType type) {
        Dungeon dungeon = Mediator.getInstance().getDungeon();

        int width = dungeon.getWidth();
        int height = dungeon.getHeight();

        Pair location = null;
        while (location == null) {
            location = MediatorHelper.getUniqueSpawnLocation(width, height);
        }

        Entity new_object = null;
        if (type == EntityType.TREASURE) {
            new_object = new Treasure(location.getX(), location.getY());
        } else if (type == EntityType.POTION) {
            new_object = new Potion(location.getX(), location.getY());
        }

        dungeon.getEntities().add(new_object);

        setupImage(new_object);

    }

    public static void setupImage(Entity entity) {
        Image new_image = new Image(entity.getImagePath());
        ImageView new_view = new ImageView(new_image);
        new_view.setId(entity.getImageID());
        GridPane.setColumnIndex(new_view, entity.getX());
        GridPane.setRowIndex(new_view, entity.getY());

        Mediator.getInstance().getImageEntities().add(new_view);
        Mediator.getInstance().getSquares().getChildren().add(new_view);
    }
    
}
