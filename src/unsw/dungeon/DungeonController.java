package unsw.dungeon;

import java.io.FileNotFoundException;
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
    public void handleKeyPress(KeyEvent event) throws FileNotFoundException {
        switch (event.getCode()) {
        case UP:    	
        	if(!player.checkCollision(squares , "UP" , dungeon.getEntities())) {
        		player.moveUp();
        	} 
        	if(player.checkTreasureCollision("UP", dungeon.getEntities())) {
        		
        	}
            break;
        case DOWN:
            if(!player.checkCollision(squares ,"DOWN" , dungeon.getEntities())) {
                player.moveDown();
            }
            if(player.checkTreasureCollision("UP", dungeon.getEntities())) {
        		
        	}
            break;
        case LEFT:
            if(!player.checkCollision(squares , "LEFT" , dungeon.getEntities())) {
            	 player.moveLeft();
            } 
            if(player.checkTreasureCollision("UP", dungeon.getEntities())) {
        		
        	}
            break;
        case RIGHT:
        	if(!player.checkCollision(squares , "RIGHT" , dungeon.getEntities())) {
        		player.moveRight();
        	} 
        	if(player.checkTreasureCollision("UP", dungeon.getEntities())) {
        		
        	}
            break;
        default:
            break;
        }
    }

}

