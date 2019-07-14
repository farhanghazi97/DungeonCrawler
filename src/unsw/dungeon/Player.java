package unsw.dungeon;

import java.awt.Rectangle;
import java.util.List;

/**
 * The player entity
 * @author Robert Clifton-Everest
 *
 */
public class Player extends Entity {

    private Dungeon dungeon;

    /**
     * Create a player positioned in square (x,y)
     * @param x
     * @param y
     */
    public Player(Dungeon dungeon, int x, int y) {
        super(x, y);
        this.dungeon = dungeon;
    }

	//private List
    
    @Override
    public String toString() {
		return String.format("Player object");
    }

	@Override
	public EntityType getType(){return EntityType.PLAYER;}

	@Override
	public boolean isBlocked(List<Entity> entitiesAtNew){
		for (Entity entity : entitiesAtNew) {
			if (entity.getType()==EntityType.WALL){
				return true;
			}
		}
		return false;
	}

	@Override
	public void postMove(List<Entity> entitiesAtNew){
		for (Entity entity : entitiesAtNew) {
			if (entity.getType()==EntityType.EXIT){
				Mediator.getInstance().markGameOver();
			}
		}

	}

	@Override
	public boolean stepOver() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeEntity() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void generateEntity() {
		// TODO Auto-generated method stub
		
	}
    
}
