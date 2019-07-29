package unsw.dungeon;

import java.util.ArrayList;
import java.util.List;

import javafx.concurrent.Task;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Potion extends Entity {

	private boolean collected = false;
	private String image_path = "/brilliant_blue_new.png";
	private boolean in_effect = false;

	public Potion(Dungeon dungeon, int x, int y) {
        super(dungeon, x, y);
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
    
		Entity tempPotion = dungeon.getInventoryEntity(EntityType.POTION);
		Entity player = dungeon.getPlayer();
		
		if(tempPotion != null ) {
			//Player already has a potion
			return false;
		}else {
			//Add new potion
			if(dungeon.getInventoryEntities().add(this)) {
				this.in_effect = true;
				this.updatePlayerUI(player , in_effect);
				//Start potion timer function
				startSelfDestruct(6000 , player);
				dungeon.removeEntity(this);
				return true;
			}
		}
		return false;
	}
    
    //Method to limit potion usage to ~ 5 seconds
    private void startSelfDestruct(long time , Entity entity) {
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
        	dungeon.getInventoryEntities().remove(this);
        	System.out.println("After destroying: "+ dungeon.getInventoryEntities());
        	this.in_effect = false;
            this.updatePlayerUI(entity , in_effect);
        });

        new Thread(task).start();
    }

	@Override
	public String toString() {
		return "POTION object [collected=" + collected + "]";
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
	public ArrayList<String> getImageList() {
		return null;
	}
	
	private void updatePlayerUI(Entity entity , boolean in_effect) {
		ArrayList<String> images = entity.getImageList();
		
		Image potion_effect;
		if(in_effect) {
			potion_effect = new Image(images.get(1));
		} else {
			 potion_effect = new Image(images.get(0));
		}
		
		ImageView image = dungeon.getImageByEntity(dungeon.getImageEntities(), entity);
		image.setImage(potion_effect);
	}

}
