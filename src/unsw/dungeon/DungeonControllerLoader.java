package unsw.dungeon;

import java.io.FileNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    
    private Image swordimage0;
    
    private Image ice_weapon;
    
    private Image treasureimage;
    
    private Image potionimage0;

    
    private Image bombimage_unlit;
    private Image keyimage;
    
    private Image enemyimage0;
    
    private Image doorimage;
    private Image exitimage;
    
    public DungeonControllerLoader(String filename)
            throws FileNotFoundException {
        super(filename);
        
        entities = new ArrayList<>();
        playerImage = new Image("/human_new.png");
        wallImage = new Image("/brick_brown_0.png");
        boulderimage = new Image("/boulder.png");
        switchimage = new Image("/pressure_plate.png");
        
        swordimage0 = new Image("/greatsword_1_new.png");
        
        ice_weapon = new Image("/misc_crystal.png");


        treasureimage = new Image("/gold_pile.png");
        
        potionimage0 = new Image("/brilliant_blue_new.png");
        
        bombimage_unlit = new Image("/bomb_unlit.png");
        exitimage = new Image("/exit.png");
        keyimage = new Image("/key.png");
        
        enemyimage0 = new Image("/deep_elf_master_archer.png");
        
        doorimage = new Image("/closed_door.png");
        
        Random rand = new Random();
        
    }

    @Override
    public void onLoad(Entity player) {
        ImageView view = new ImageView(playerImage);
        view.setId("Player image");
        addEntity(player, view);
    }

    @Override
    public void onLoad(Wall wall) {
        ImageView view = new ImageView(wallImage);
        view.setId("Wall image");
        addEntity(wall, view);
    }
    
    @Override
    public void onLoad(Boulder boulder) {
    	ImageView view = new ImageView(boulderimage);
    	view.setId("Boulder image");
    	addEntity(boulder, view);
    }

    @Override
    public void onLoad(Switch s) {
    	ImageView view = new ImageView(switchimage);
    	view.setId("Switch image");
    	addEntity(s , view);
    }
    
    @Override
    public void onLoad(Sword s) {
    	ImageView view = new ImageView(swordimage0);
    	view.setId("Sword image");
    	addEntity(s , view);
    }
    
    @Override
    public void onLoad(IceBall i) {
    	ImageView view = new ImageView(ice_weapon);
    	view.setId("IceBall Image");
    	addEntity(i , view);
    }
    
    @Override
    public void onLoad(Treasure t) {
    	ImageView view = new ImageView(treasureimage);
    	view.setId("Treasure image");
    	addEntity(t , view);
    }
    
    @Override
    public void onLoad(Potion p) {
    	ImageView view = new ImageView(potionimage0);  	
    	view.setId("Potion image");
    	addEntity(p , view);
    }
    
    @Override
    public void onLoad(Bomb b) {
    	ImageView view = new ImageView(bombimage_unlit);
    	view.setId("Bomb image");
    	addEntity(b , view);
    }

    @Override
    public void onLoad(Exit exit) {
        ImageView view = new ImageView(exitimage);
        view.setId("Exit image");
        addEntity(exit , view);
    }

    @Override
    public void onLoad(Key key) {
    	ImageView view = new ImageView(keyimage);
    	view.setId("Key image");
    	addEntity(key , view);
    }
    
    @Override
    public void onLoad(Enemy enemy) {
    	ImageView view= new ImageView(enemyimage0);;
    	view.setId("Enemy image");
    	addEntity(enemy,view);
    }

    @Override
    public void onLoad(Door door) {
        ImageView view = new ImageView(doorimage);
        view.setId("Door image");
        addEntity(door, view);
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
    public void trackPosition(Entity entity, Node node) {
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
     * loaded entities
     * Provides the dungeon with a reference to the the CungeonController
     * @return
     * @throws FileNotFoundException
     */
    public DungeonController loadController() throws FileNotFoundException {
 
        Dungeon dungeon = load();
		DungeonController dc = new DungeonController(dungeon, entities);
		dungeon.setDungeonController(dc);
        return dc;
        
    }
    
    


}
