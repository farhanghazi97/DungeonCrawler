package unsw.dungeon;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Random;

import javafx.beans.property.IntegerProperty;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class ObjectGenerator extends DungeonControllerLoader {

    private Image swordimage;
    private Image treasureimage;
    private Image potionimage;
    private Image bombimage_unlit;
    
	public ObjectGenerator(String filename) throws FileNotFoundException  {
		super(filename);
	    this.swordimage = new Image("greatsword_1_new.png");
	    this.treasureimage = new Image("gold_pile.png");
	    this.potionimage = new Image("brilliant_blue_new.png");
	    this.bombimage_unlit = new Image("bomb_unlit.png");
	}
	
	public void GenerateTreasure(List<Entity> entities , GridPane g , int x , int y) {
		System.out.println("GENERATED TREASURE");
		g.add(new ImageView(treasureimage), x, y);
		Treasure t = new Treasure(x , y);
		entities.add(t);
	}
	
	public void GenerateSword(List<Entity> entities , GridPane g , int x , int y) {
		System.out.println("GENERATED SWORD");
		g.add(new ImageView(swordimage), x ,y);
		Sword t = new Sword(x , y);
		entities.add(t);
	}
	
	public void GeneratePotion(List<Entity> entities , GridPane g , int x , int y) {
		System.out.println("GENERATED POTION");
		g.add(new ImageView(potionimage), x , y);
		Potion t = new Potion(x , y);
		entities.add(t);
	}
	
	public void GenerateUnlitBomb(List<Entity> entities , GridPane g , int x , int y) {
		System.out.println("GENERATED UNLIT BOMB");
		g.add(new ImageView(bombimage_unlit), x , y);
		Bomb t = new Bomb(x , y);
		entities.add(t);
	}
	

}
