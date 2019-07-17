package unsw.dungeon;

import java.awt.Rectangle;
import java.util.List;

public class Sword extends Entity {
	
	private String type = "Sword";
	private String image_path = "greatsword_1_new.png";
	
	private int swingsRemaining = 5;
	private boolean collected = false;
	
	public Sword(int x, int y) {
        super(x, y);
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
    public void postMove(List<Entity> entitiesAtNew) {

    }
    
	@Override
	public boolean stepOver() {
		System.out.println("In Sword's stepOver");
		this.swingsRemaining = 5;
		//Add sword to collected entities
		Entity tempSword = Mediator.getInstance().getCollected(EntityType.SWORD);
		
		if(tempSword != null) {
			//Player already has a sword
			return false;
		}else {
			//Add new sword
			if(Mediator.getInstance().collectedEntities.add(this)) {
				this.collected = true;
				System.out.println(this.toString());
				Mediator.getInstance().removeEntity(this);
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
			Mediator.getInstance().collectedEntities.remove(this);
			System.out.println(this.toString());
			return false;
		}
	}
	
	@Override
	public void removeEntity() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void generateEntity() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String toString() {
		return "Sword [swingsRemaining=" + swingsRemaining + ", collected=" + collected + "]";
	}
	
	@Override
	public int getDoorID() {
		return -1;
	}
	
	@Override
	public int getKeyID() {
		return -1;
	}
	
	@Override
	public boolean isIs_open() {
		return false;
	}
	
	@Override
	public Entity getObjectByType(String s) {
		if(s.equals(type)) {
			return this;
		} else {
			return null;
		}
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
	
}
