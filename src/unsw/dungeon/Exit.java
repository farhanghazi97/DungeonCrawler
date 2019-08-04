package unsw.dungeon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Exit extends Entity{
	
	private String imagePath = "/exit.png";
	private ArrayList<String> goalRequirements = new ArrayList<String>(Arrays.asList
			(
					"treasure" ,
					"enemies",
					"boulders",
					"exit"
			)
	); 
	
  
	public Exit(Dungeon dungeon, int x, int y) {
        super(dungeon, x, y);
    }

    @Override
    public EntityType getType() {
        return EntityType.EXIT;
    }

    @Override
    public boolean isBlocked(List<Entity> entitiesAtNew) {
        return false;
    }

    @Override
    public void moveTo(int newX, int newY , boolean flag) {
        //Nothing here
    }

    @Override
    public void postMove(List<Entity> entitiesAtNew) {}

	/**
	 * Method to check if exit goal has been met and if so, trigger the required logic
	 * @return
	 */
	@Override
	public boolean stepOver() {
		
    	JSONObject goal = dungeon.getGoal();
		String operator = goal.getString("goal"); //AND/OR
		JSONArray playerGoalReqs = null;
		
		boolean isGoalMet = false;
		
		try {
			// More than one goal
			playerGoalReqs = goal.getJSONArray("subgoals");
		} catch (JSONException e) {
			// Single goal
			if(checkGoalMet(operator)) {
				isGoalMet = true;
				return isGoalMet;
			} else {
				return isGoalMet;
			}
		}
		
		if(operator.equals("AND")) {
			for(int i = 0; i < playerGoalReqs.length(); i++) {
				JSONObject goal_cond_obj = playerGoalReqs.getJSONObject(i);
				String goal_cond = goal_cond_obj.getString("goal");
				if(this.goalRequirements.contains(goal_cond)) {
					if(checkGoalMet(goal_cond)) {
						isGoalMet = true;
					} else {
						isGoalMet = false;
						return isGoalMet;
					}
				}
			}
		} 
		
		if (operator.equals("OR")) {
			for(int j = 0; j < playerGoalReqs.length(); j++) {
				JSONObject goal_cond_obj = playerGoalReqs.getJSONObject(j);
				String goal_cond = goal_cond_obj.getString("goal");
				if(this.goalRequirements.contains(goal_cond)) {
					if(checkGoalMet(goal_cond)) {
						isGoalMet = true;
						return isGoalMet;
					} 
				}
			}
		}
		
		return isGoalMet;
	
    }

	@Override
	public String getImageID() {
		return "Exit Image";
	}
	
	@Override
	 public String getImagePath() {
		return this.imagePath;
	}

	@Override
	public ArrayList<String> getImageList() {
		return null;
	}

    @Override
    public String toString() {
    	return "EXIT object";
    }
	
	private boolean checkGoalMet(String goal) {
		if(goal.equals(goalRequirements.get(0))) {
			if(dungeon.getEntities(EntityType.TREASURE).size() == 0) {
				return true;
			}
		} else if (goal.equals(this.goalRequirements.get(1))) {
			if(dungeon.getEntities(EntityType.ENEMY).size() == 0) {
				return true;
			}
		} else if (goal.equals(this.goalRequirements.get(2))) {
			List<Entity> switches = dungeon.getEntities(EntityType.SWITCH);
			boolean all_triggered = true;
			for(Entity s : switches) {
				if(!((Switch) s).isTriggered()) {
					all_triggered = false;
					break;
				}
			}
			return all_triggered;
		}
		return false;
	}

}
