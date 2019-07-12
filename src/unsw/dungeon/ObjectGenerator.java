package unsw.dungeon;

import java.io.FileNotFoundException;
import java.util.Random;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class ObjectGenerator  {

    private Image swordimage;
    private Image treasureimage;
    private Image potionimage;
    private Image bombimage_unlit;
	
	public ObjectGenerator()  {
	    this.swordimage = new Image("greatsword_1_new.png");
	    this.treasureimage = new Image("gold_pile.png");
	    this.potionimage = new Image("brilliant_blue_new.png");
	    this.bombimage_unlit = new Image("bomb_unlit.png");
	}
	
	public void GenerateTreasure(GridPane g , int x , int y) {
		Random rand = new Random();
		g.add(new ImageView(treasureimage), rand.nextInt(x),rand.nextInt(y));
	}
	
	public void GenerateSword(GridPane g , int x , int y) {
		Random rand = new Random();
		g.add(new ImageView(swordimage), rand.nextInt(x),rand.nextInt(y));
	}
	
	public void GeneratePotion(GridPane g , int x , int y) {
		Random rand = new Random();
		g.add(new ImageView(potionimage), rand.nextInt(x),rand.nextInt(y));
	}
	
	public void GenerateUnlitBomb(GridPane g , int x , int y) {
		Random rand = new Random();
		g.add(new ImageView(bombimage_unlit), rand.nextInt(x),rand.nextInt(y));
	}
	

}
