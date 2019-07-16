package unsw.dungeon;

import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.List;

import javafx.scene.image.ImageView;

public class MockDungeonControllerLoader extends DungeonLoader {

	public MockDungeonControllerLoader(String filename) throws FileNotFoundException {
        super(filename);
    }

    @Override
    public void onLoad(Entity player) {

    }

    @Override
    public void onLoad(Wall wall) {

    }

    @Override
    public void onLoad(Boulder boulder) {

    }

    @Override
    public void onLoad(Switch s) {

    }

    @Override
    public void onLoad(Sword s) {

    }

    @Override
    public void onLoad(Treasure s) {

    }

    @Override
    public void onLoad(Potion s) {

    }

    @Override
    public void onLoad(Bomb b) {

    }

    @Override
    public void onLoad(Exit exit) {

    }

    @Override
    public void onLoad(Key key) {

    }

    @Override
    public void onLoad(Enemy enemy) {

    }

    @Override
    public void onLoad(Door door) {

    }

    private List<ImageView> entities;

    public DungeonController loadController() throws FileNotFoundException {
        return new DungeonController(load(), Collections.emptyList());
    }
	
}
