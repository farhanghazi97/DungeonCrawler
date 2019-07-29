package unsw.dungeon;

import java.util.ArrayList;
import java.util.List;

public class Sword extends Entity {
	
	private String image_path = "greatsword_1_new.png";
	private ArrayList<String> image_list = new ArrayList<String>();
	
	private int swingsRemaining = 5;
	private boolean collected = false;
	
//	public Sword(int x, int y) {
//        super(x, y);
//    }

	public Sword(Dungeon dungeon, int x, int y) {
        super(dungeon, x, y);
    }

    @Override
    public EntityType getType() {
        return EntityType.SWORD;
    }

    @Override
    public boolean isBlocked(List<Entity> entitiesAtNew) {
        return true;
    }

	@Override
	public void moveTo(int newX, int newY) {
		//Nothing here
	}

    @Override
    public void postMove(List<Entity> entitiesAtNew) {

    }
    
	@Override
	public boolean stepOver() {
		System.out.println("In Sword's stepOver");
		this.swingsRemaining = 5;
		//Add sword to collected entities
		Entity tempSword =dungeon.getCollected(EntityType.SWORD);
		
		if(tempSword != null) {
			//Player already has a sword
			return false;
		}else {
			//Add new sword
			if(dungeon.getCollectedEntities().add(this)) {
				this.collected = true;
				System.out.println(this.toString());
				dungeon.removeEntity(this);
				return true;
			}
		}
		return false;
	}
	
	//Swings sword once and reduces swingsRemaining
	public boolean swing() {
		swingsRemaining--;
		if(swingsRemaining > 0) {
			System.out.println(this.toString());
			return true;
		}else {
			//Remove sword from player's collected entities
			this.collected = false;
			dungeon.getCollectedEntities().remove(this);
			System.out.println(this.toString());
			return false;
		}
	}

	@Override
	public String toString() {
		return "SWORD object [swingsRemaining=" + swingsRemaining + ", collected=" + collected + "]";
	}
	
	@Override
	public String getImageID() {
		return "Sword image";
	}
	
	@Override
	 public String getImagePath() {
		return this.image_path;
	}

	public int getSwingsRemaining() {
		return swingsRemaining;
	}

	@Override
	public ArrayList<String> getImageList() {
		return image_list;
	}
	

}
