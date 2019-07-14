package unsw.dungeon;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

/**
 * A DungeonLoader that also creates the necessary ImageViews for the UI,
 * connects them via listeners to the model, and creates a controller.
 * @author Robert Clifton-Everest
 *
 */
public class DungeonControllerLoader extends DungeonLoader {

    private List<ImageView> entities;

    //Images
    private Image playerImage;
    private Image wallImage;
    private Image boulderimage;
    private Image switchimage;
    private Image swordimage;
    private Image treasureimage;
    private Image potionimage;
    private Image bombimage_unlit;

    private Image exitimage;

    public DungeonControllerLoader(String filename)
            throws FileNotFoundException {
        super(filename);
        
        entities = new ArrayList<>();
        playerImage = new Image("/human_new.png");
        wallImage = new Image("/brick_brown_0.png");
        boulderimage = new Image("/boulder.png");
        switchimage = new Image("/pressure_plate.png");
        swordimage = new Image("/greatsword_1_new.png");
        treasureimage = new Image("/gold_pile.png");
        potionimage = new Image("/brilliant_blue_new.png");
        bombimage_unlit = new Image("/bomb_unlit.png");
        exitimage = new Image("/exit.png");

    }

    @Override
    public void onLoad(Entity player) {
        ImageView view = new ImageView(playerImage);
        addEntity(player, view);
    }

    @Override
    public void onLoad(Wall wall) {
        ImageView view = new ImageView(wallImage);
        addEntity(wall, view);
    }
    
    @Override
    public void onLoad(Boulder boulder) {
    	ImageView view = new ImageView(boulderimage);
    	addEntity(boulder, view);
    }

    @Override
    public void onLoad(Switch s) {
    	ImageView view = new ImageView(switchimage);
    	addEntity(s , view);
    }
    
    @Override
    public void onLoad(Sword s) {
    	ImageView view = new ImageView(swordimage);
    	addEntity(s , view);
    }
    
    @Override
    public void onLoad(Treasure t) {
    	ImageView view = new ImageView(treasureimage);
    	addEntity(t , view);
    }
    
    @Override
    public void onLoad(Potion p) {
    	ImageView view = new ImageView(potionimage);
    	addEntity(p , view);
    }
    
    @Override
    public void onLoad(Bomb b) {
    	ImageView view = new ImageView(bombimage_unlit);
    	addEntity(b , view);
    }

    @Override
    public void onLoad(Exit exit) {
        ImageView view = new ImageView(exitimage);
        addEntity(exit , view);
    }

    private void addEntity(Entity entity, ImageView view) {
        trackPosition(entity, view);
        entities.add(view);
    }

    /**
     * Set a node in a GridPane to have its position track the position of an
     * entity in the dungeon.
     *
     * By connecting the model with the view in this way, the model requires no
     * knowledge of the view and changes to the position of entities in the
     * model will automatically be reflected in the view.
     * @param entity
     * @param node
     */
    private void trackPosition(Entity entity, Node node) {
        GridPane.setColumnIndex(node, entity.getX());
        GridPane.setRowIndex(node, entity.getY());
        entity.x().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) {
                GridPane.setColumnIndex(node, newValue.intValue());
            }
        });
        entity.y().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) {
                GridPane.setRowIndex(node, newValue.intValue());
            }
        });
    }

    /**
     * Create a controller that can be attached to the DungeonView with all the
     * loaded entities.
     * @return
     * @throws FileNotFoundException
     */
    public DungeonController loadController() throws FileNotFoundException {
        return new DungeonController(load(), entities);
    }


}
