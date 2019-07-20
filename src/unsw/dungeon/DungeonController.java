package unsw.dungeon;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * Added for testability
     * @return
     */
    public Dungeon getDungeon() {
        return dungeon;
    }
    public GridPane getSquares() {
        return squares;
    }
    public List<ImageView> getInitialEntities() {
        return initialEntities;
    }

    
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

      //Initializes Mediator class  
       Mediator.getInstance().setDungeon(dungeon, squares , initialEntities);

    }
    
    @FXML
    public void handleKeyPress(KeyEvent event) {

        switch (event.getCode()) {
        case UP:
            Mediator.getInstance().moveTo(player.getX(), player.getY(), player.getX(), player.getY() - 1);
            break;
        case DOWN:
            Mediator.getInstance().moveTo(player.getX(), player.getY(), player.getX(), player.getY() + 1);
            break;
        case LEFT:
            Mediator.getInstance().moveTo(player.getX(), player.getY(),player.getX()-1, player.getY());
            break;
        case RIGHT:
            Mediator.getInstance().moveTo(player.getX(), player.getY(),player.getX()+1, player.getY()); 
            break;
        case U:
        	Mediator.getInstance().unlockDoor(player.getX(), player.getY());
        	break;
        case S:
        	Mediator.getInstance().swingSword(player.getX(), player.getY());
        	break;
        case B:
        	Mediator.getInstance().igniteBomb(player.getX() , player.getY());
        	break;
        default:
            break;
        }
    }
    
    public GridPane getGridPane(){
    	return squares;
    }

}

