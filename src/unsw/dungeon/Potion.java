package unsw.dungeon;

import java.util.ArrayList;
import java.util.List;

import javafx.concurrent.Task;

public class Potion extends Entity {

	private boolean collected = false;
	private String image_path = "/brilliant_blue_new.png";
	private boolean isDestroyed = false;
	private int count = 0;
	
	public Potion(int x, int y) {
        super(x, y);
    }

    @Override
    public EntityType getType() {
        return EntityType.POTION;
    }

    @Override
    public boolean isBlocked(List<Entity> entitiesAtNew) {
        return true;
    }

    @Override
    public void postMove(List<Entity> entitiesAtNew) {
    	
    }

    @Override
	public void moveTo(int newX, int newY) {
		//Nothing here
	}

    @Override
	public boolean stepOver() {
    	System.out.println("Inside Potion's stepOver");
    
		Entity tempPotion = Mediator.getInstance().getCollected(EntityType.POTION);

		if(tempPotion != null && isDestroyed == false) {
			//Player already has a potion
			return false;
		}else {
			//Add new potion
			if(Mediator.getInstance().collectedEntities.add(this)) {
				
				//Start potion timer function
				startSelfDestruct(6000);
				MediatorHelper.removeEntity(this);
				return true;
			}
		}
		return false;
	}
    
    //Method to limit potion usage to ~ 5 seconds
    private void startSelfDestruct(long time) {
    	//System.out.println("Here");
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() {
                try {
                    Thread.sleep(time);
                } catch (InterruptedException e) {
                }
                return null;
            }
        };
       
        task.setOnSucceeded(e -> {
        	isDestroyed = true;
        	Mediator.getInstance().collectedEntities.remove(this);
        	System.out.println("After destroying: "+ Mediator.getInstance().collectedEntities);
            
        });

        new Thread(task).start();
    }

	@Override
	public String toString() {
		return "POTION object [count=" + count + ", collected=" + collected + "]";
	}
	

	@Override
	public String getImageID() {
		return "Potion image";
	}
	
	@Override
	 public String getImagePath() {
		return this.image_path;
	}

	@Override
	public ArrayList<String> getImage_list() {
		// TODO Auto-generated method stub
		return null;
	}

}
