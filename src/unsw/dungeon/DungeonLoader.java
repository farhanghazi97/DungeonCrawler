package unsw.dungeon;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * Loads a dungeon from a .json file.
 *
 * By extending this class, a subclass can hook into entity creation. This is
 * useful for creating UI elements with corresponding entities.
 *
 * @author Robert Clifton-Everest
 *
 */
public abstract class DungeonLoader {

    private JSONObject json;

    public DungeonLoader(String filename) throws FileNotFoundException {
        json = new JSONObject(new JSONTokener(new FileReader("dungeons/" + filename)));
    }

    /**
     * Parses the JSON to create a dungeon.
     * @return
     */
    public Dungeon load() {
    	
        String musicFile = "Medieval Music - Dark Dungeon.mp3"; 
        Media sound = new Media(new File(musicFile).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setVolume(0.30);
        mediaPlayer.play();
    	
        int width = json.getInt("width");
        int height = json.getInt("height");
        
        JSONObject goalConditions = json.getJSONObject("goal-condition");

        Dungeon dungeon = new Dungeon(width, height , goalConditions , mediaPlayer);

        JSONArray jsonEntities = json.getJSONArray("entities");

        for (int i = 0; i < jsonEntities.length(); i++) {
            loadEntity(dungeon, jsonEntities.getJSONObject(i));
        }
        return dungeon;
    }

    private void loadEntity(Dungeon dungeon, JSONObject json) {
        String type = json.getString("type");
        
        int x = json.getInt("x");
        int y = json.getInt("y");
       
        int keyID = -1;
        int doorID = -1;
        
		if (type.equals("key")) {
			keyID = json.getInt("id");
		}
		
		if(type.equals("door")) {
			doorID = json.getInt("id");
		}
		
        Entity entity = null;
        
        switch (type) {
        case "player":
        	Player player = new Player(dungeon, x, y);
            dungeon.setPlayer(player);
            onLoad(player);
            entity = player;
            break;
        
        case "wall":
        	Wall wall = new Wall(dungeon, x, y);
            onLoad(wall);
            entity = wall;
            break;
        
        case "boulder":
        	Boulder boulder = new Boulder(dungeon,x, y);
        	onLoad(boulder);
        	entity = boulder;
        	break;
        
        case "switch":
        	Switch swi = new Switch(dungeon,x,y);
        	onLoad(swi);
        	entity = swi;
        	break;
        
        case "sword":
        	Sword swo = new Sword(dungeon,x,y);
        	onLoad(swo);
        	entity = swo;
        	break;
        
        case "treasure":
        	Treasure t = new Treasure(dungeon,x,y);
        	onLoad(t);
        	entity = t;
        	break;
        
        case "invincibility":
        	Potion p = new Potion(dungeon,x,y);
        	onLoad(p);
        	entity = p;
        	break;
        	
        case "bomb":
        	Bomb b = new Bomb(dungeon,x,y);
        	onLoad(b);
        	entity = b;
        	break;

        case "exit":
            Exit exit = new Exit(dungeon,x,y);
            onLoad(exit);
            entity = exit;
            break;
            
		case "key":
			Key key = new Key(dungeon,x, y, keyID);
			onLoad(key);
			entity = key;
			break;
			
		case "enemy":
			Enemy enemy = new Enemy(dungeon,x, y);
			onLoad(enemy);
			entity = enemy;
			break;
           
		case "door":
			Door door = new Door(dungeon,x , y , doorID);
			onLoad(door);
			entity = door;
			break;
			
		case "ice":
			IceBall iceBall = new IceBall(dungeon,x,y);
			onLoad(iceBall);
			entity = iceBall;
			break;
			
        }
        dungeon.addEntity(entity);
    }

    public abstract void onLoad (Entity player);
    public abstract void onLoad (Wall wall);
	public abstract void onLoad (Boulder boulder);
	public abstract void onLoad (Switch s) ;
	public abstract void onLoad (Sword s);
	public abstract void onLoad (IceBall i);
	public abstract void onLoad (Treasure s);
	public abstract void onLoad (Potion s);
	public abstract void onLoad (Bomb b);
    public abstract void onLoad (Exit exit);
    public abstract void onLoad (Key key);
    public abstract void onLoad (Enemy enemy);
    public abstract void onLoad (Door door);

}
