package unsw.dungeon;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;


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
    
    /*
     * When this method is called, it will change teh scene to winnerView
     */
    public void showWinnerBox(String infoMessage, String titleBar, String headerMessage)  {

    	 Alert alert = new Alert(AlertType.INFORMATION);
         alert.setTitle(titleBar);
         alert.setHeaderText(headerMessage);
         alert.setContentText(infoMessage);
         alert.showAndWait();
    	    
    }
     
    public void removeEntity(Entity entity) {
    	System.out.println("Controller : Removing "+ entity);
        ImageView image = getImageByEntity(entity);
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
	
	public ImageView getImageByEntity(Entity e) {
		ImageView image = new ImageView();
		for (int i = 0; i < initialEntities.size(); i++) {
			image = initialEntities.get(i);
			if (GridPane.getColumnIndex(image) == e.getX() && GridPane.getRowIndex(image) == e.getY()) {
				if (image.getId().equals(e.getImageID())) {
					break;
				}
			}
		}
		return image;
	}

	public List<ImageView> getInitialEntities() {
		return initialEntities;
	}

	public void launchStarterDialog() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Welcome to the Dungeon");
		alert.setHeaderText("Before you begin, please choose an enemy difficulty level");
		alert.setContentText(null);

		ButtonType easy = new ButtonType("Easy");
		ButtonType medium = new ButtonType("Medium");
		ButtonType hard = new ButtonType("Hard");

		alert.getButtonTypes().setAll(easy, medium, hard);
		
		Optional<ButtonType> clickButton = alert.showAndWait();
		
		
		handleDifficultyBtn(easy, medium, hard, clickButton);
	}

	private void handleDifficultyBtn(ButtonType easy, ButtonType medium, ButtonType hard, Optional<ButtonType> clickButton) {
		if (clickButton.get() == easy) {
			dungeon.setEnemyDifficulty(1);
		}else if(clickButton.get() == medium) {
			dungeon.setEnemyDifficulty(2);
		}else if(clickButton.get() == hard) {
			dungeon.setEnemyDifficulty(3);
		}
	}

	public void showLoserBox(String infoMessage, String titleBar, String headerMessage) {
		// TODO Auto-generated method stub

   	 Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(titleBar);
        alert.setHeaderText(headerMessage);
        alert.setContentText(infoMessage);
        alert.showAndWait();
	}
	
	
	

//	Parent winnerViewParent = FXMLLoader.load(getClass().getResource("WinnerView.fxml"));
//	Scene winnerViewScene = new Scene(winnerViewParent);
//	
//	//Now get the stage information
//	Stage window = (Stage)(((Node) event.getSource()).getScene().getWindow());
//	
//	window.setScene(winnerViewScene);
//	window.show();

}

