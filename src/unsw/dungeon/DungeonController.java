package unsw.dungeon;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;


/**
 * A JavaFX controller for the dungeon.
 * @author Robert Clifton-Everest
 *
 */


public class DungeonController {

    @FXML
    private GridPane squares;

    private List<ImageView> initialEntities;

    private Player player;

    private Dungeon dungeon;

    
    public DungeonController(Dungeon dungeon, List<ImageView> initialEntities) {
        this.dungeon = dungeon;
        this.player = dungeon.getPlayer();
        this.initialEntities = new ArrayList<>(initialEntities);
    }

    @FXML
    public void initialize() {
        Image ground = new Image("/dirt_0_new.png");

        // Add the ground first so it is below all other entities
        for (int x = 0; x < dungeon.getWidth(); x++) {
            for (int y = 0; y < dungeon.getHeight(); y++) {
                squares.add(new ImageView(ground), x, y);
            }
        }
        for (ImageView entity : initialEntities) {
            squares.getChildren().add(entity);
        }
 
    }
    
    @FXML
    public void handleKeyPress(KeyEvent event) {

        switch (event.getCode()) {
        case UP:
            //Mediator.getInstance().moveTo(player.getX(), player.getY(), player.getX(), player.getY() - 1);
        	dungeon.moveTo(player.getX(), player.getY(), player.getX(), player.getY() - 1);
            break;
        case DOWN:
            //Mediator.getInstance().moveTo(player.getX(), player.getY(), player.getX(), player.getY() + 1);
        	dungeon.moveTo(player.getX(), player.getY(), player.getX(), player.getY() + 1);
            break;
        case LEFT:
            //Mediator.getInstance().moveTo(player.getX(), player.getY(),player.getX()-1, player.getY());
        	dungeon.moveTo(player.getX(), player.getY(),player.getX()-1, player.getY());
            break;
        case RIGHT:
            //Mediator.getInstance().moveTo(player.getX(), player.getY(),player.getX()+1, player.getY()); 
            dungeon.moveTo(player.getX(), player.getY(),player.getX()+1, player.getY());
            break;
        case U:
        	dungeon.handleKeyPressU(player.getX(), player.getY());
        	break;
        case S:
        	dungeon.handleKeyPressS(player.getX(), player.getY());
        	break;
        case B:
        	dungeon.handleKeyPressB(player.getX() , player.getY());
        	break;
        default:
            break;
        }
    }
     
    public void removeEntity(Entity entity) {
    	System.out.println("Controller : Removing "+ entity);
        ImageView image = dungeon.getImageByEntity(initialEntities,  entity);
        if (image.getId().equals(entity.getImageID())) {
            //Removing from screen
            squares.getChildren().remove(image);
        }
    }
    
    public void generateImage(Entity entity) {
        Image new_image = new Image(entity.getImagePath());
        ImageView new_view = new ImageView(new_image);
        new_view.setId(entity.getImageID());
        GridPane.setColumnIndex(new_view, entity.getX());
        GridPane.setRowIndex(new_view, entity.getY());

        initialEntities.add(new_view);
        squares.getChildren().add(new_view);
    }
    
    public Pair getUniqueMazeCoordinates() {
        Random rand = new Random();
        int randX = rand.nextInt(dungeon.getWidth());
        int randY = rand.nextInt(dungeon.getHeight());
        List<Entity> entitiesAtXY = dungeon.getEntities(randX, randY);
        if (entitiesAtXY.size() == 0) {
            return new Pair(randX, randY);
        }
        return null;
    }

	public List<ImageView> getInitialEntities() {
		return initialEntities;
	}


}

