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
 * 
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
			dungeon.moveTo(player.getX(), player.getY(), player.getX(), player.getY() - 1);
			break;
		case DOWN:
			dungeon.moveTo(player.getX(), player.getY(), player.getX(), player.getY() + 1);
			break;
		case LEFT:
			dungeon.moveTo(player.getX(), player.getY(), player.getX() - 1, player.getY());
			break;
		case RIGHT:
			dungeon.moveTo(player.getX(), player.getY(), player.getX() + 1, player.getY());
			break;
		case U:
			dungeon.handleKeyPressU(player.getX(), player.getY());
			break;
		case S:
			dungeon.handleKeyPressS(player.getX(), player.getY());
			break;
		case B:
			dungeon.handleKeyPressB(player.getX(), player.getY());
			break;
		default:
			break;
		}
	}

	/**
	 * Method to remove an entity ImageView from maze
	 * @param entity to be removed
	 */
	public void removeEntity(Entity entity){
		ImageView image = getImageByEntity(entity);
		if (image.getId().equals(entity.getImageID())) {
			// Removing from screen
			squares.getChildren().remove(image);
		}
	}

	/**
	 * Method to get a new (x,y) location in the maze. The location returned is randomised
	 * and can be any location on the maze regardless of whether that location is occupied by
	 * an other entity
	 * @return Pair (x,y)
	 */
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

	/**
	 * Method to create an entity image on the board
	 * @param entity whose image is to be generated
	 */
	public void generateImage(Entity entity) {
		Image new_image = new Image(entity.getImagePath());
		ImageView new_view = new ImageView(new_image);
		new_view.setId(entity.getImageID());
		GridPane.setColumnIndex(new_view, entity.getX());
		GridPane.setRowIndex(new_view, entity.getY());

		initialEntities.add(new_view);
		squares.getChildren().add(new_view);
	}

	/**
	 * Method to get an entity's corresponding ImageView object
	 * @param e
	 * @return the ImageView of entity e
	 */
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


	/**
	 * Method to launch the startup message asking the user to choose their desired level of difficulty
	 */
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


	/**
	 * Method to handle onClick for various buttons in the popup box
	 * @param easy difficulty level
	 * @param medium difficulty level
	 * @param hard difficulty level
	 * @param clickButton is an array of type ButtonType
	 */
	private void handleDifficultyBtn(ButtonType easy, ButtonType medium, ButtonType hard,
			Optional<ButtonType> clickButton) {
		if (clickButton.get() == easy) {
			dungeon.setDifficulty(1);
		} else if (clickButton.get() == medium) {
			dungeon.setDifficulty(2);
		} else if (clickButton.get() == hard) {
			dungeon.setDifficulty(3);
		}
	}

	/**
	 * Method to display the a popup with provided message
	 * @param infoMessage
	 * @param titleBar
	 * @param headerMessage
	 */
	public void showMessageBox(String infoMessage, String titleBar, String headerMessage) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(titleBar);
		alert.setHeaderText(headerMessage);
		alert.setContentText(infoMessage);
		alert.showAndWait();
		
	}


	public List<ImageView> getInitialEntities() {
		return initialEntities;
	}

}
