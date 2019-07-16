package unsw.dungeon;

import java.io.FileNotFoundException;
import java.io.FileReader;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

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
        int width = json.getInt("width");
        int height = json.getInt("height");

        Dungeon dungeon = new Dungeon(width, height);

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
        	Wall wall = new Wall(x, y);
            onLoad(wall);
            entity = wall;
            break;
        
        case "boulder":
        	Boulder boulder = new Boulder(x, y);
        	onLoad(boulder);
        	entity = boulder;
        	break;
        
        case "switch":
        	Switch swi = new Switch(x,y);
        	onLoad(swi);
        	entity = swi;
        	break;
        
        case "sword":
        	Sword swo = new Sword(x,y);
        	onLoad(swo);
        	entity = swo;
        	break;
        
        case "treasure":
        	Treasure t = new Treasure(x,y);
        	onLoad(t);
        	entity = t;
        	break;
        
        case "invincibility":
        	Potion p = new Potion(x,y);
        	onLoad(p);
        	entity = p;
        	break;
        	
        case "bomb":
        	Bomb b = new Bomb(x,y);
        	onLoad(b);
        	entity = b;
        	break;

        case "exit":
            Exit exit = new Exit(x,y);
            onLoad(exit);
            entity = exit;
            break;
            
		case "key":
			Key key = new Key(x, y, keyID);
			onLoad(key);
			entity = key;
			break;
			
		case "enemy":
			Enemy enemy = new Enemy(x, y);
			onLoad(enemy);
			entity = enemy;
			break;
           
		case "door":
			Door door = new Door(x , y , doorID);
			onLoad(door);
			entity = door;
			break;
		
        }
        dungeon.addEntity(entity);
    }

    public abstract void onLoad (Entity player);
    public abstract void onLoad (Wall wall);
	public abstract void onLoad (Boulder boulder);
	public abstract void onLoad (Switch s) ;
	public abstract void onLoad (Sword s);
	public abstract void onLoad (Treasure s);
	public abstract void onLoad (Potion s);
	public abstract void onLoad (Bomb b);
    public abstract void onLoad (Exit exit);
    public abstract void onLoad (Key key);
    public abstract void onLoad (Enemy enemy);
    public abstract void onLoad (Door door);
    // TODO Create additional abstract methods for the other entities

}
