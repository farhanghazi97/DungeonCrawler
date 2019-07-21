package unsw.dungeon;

import java.util.ArrayList;
import java.util.List;

public class Key extends Entity {

	private String image_path = "/key.png";
	private ArrayList<String> image_list = new ArrayList<String>();
	
	private int keyID;
	private boolean collected = false;
	
	public Key(int x, int y, int keyId) {
		super(x, y);
		this.keyID = keyId;
	}

	@Override
	public EntityType getType() {
		return EntityType.KEY;
	}

	@Override
	public boolean isBlocked(List<Entity> entitiesAtNew) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void moveTo(int newX, int newY) {
		//Nothing here
	}

	@Override
	public void postMove(List<Entity> entitiesAtNew) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean stepOver() {
		System.out.println("In Key's stepOver");
	
		//Add sword to collected entities
		Entity tempKey = Mediator.getInstance().getCollected(EntityType.KEY);
		
		if(tempKey != null) {
			//Player already has a key
			return false;
		}else {
			//Pick up key
			if(Mediator.getInstance().collectedEntities.add(this)) {
				this.collected = true;
				System.out.println(this.toString());
				MediatorHelper.removeEntity(this);
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "KEY object [Key ID=" + keyID + ", collected=" + collected + "]";
	}

	public int getKeyID() {
		return keyID;
	}
	
	@Override
	public String getImageID() {
		return "Key image";
	}
	
	@Override
	 public String getImagePath() {
		return this.image_path;
	}

	@Override
	public ArrayList<String> getImage_list() {
		return image_list;
	}

}
