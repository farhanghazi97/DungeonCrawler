package unsw.dungeon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Exit extends Entity{
	
	private String image_path = "/exit.png";
	private ArrayList<String> image_list = new ArrayList<String>();
	private ArrayList<String> goal_conditions = new ArrayList<String>(Arrays.asList("AND" , "OR"));
	private ArrayList<String> goal_requirements = new ArrayList<String>(Arrays.asList
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
    public void moveTo(int newX, int newY) {
        //Nothing here
    }

    @Override
    public void postMove(List<Entity> entitiesAtNew) {
    	//Nothing here
    }

    @Override
	public boolean stepOver() {
		
    	JSONObject goal = dungeon.getGoal();
		String goal_condition = goal.getString("goal");
		JSONArray player_goal_requirements = null;
		
		boolean goals_met = false;
		
		try {
			// More than one goal
			player_goal_requirements = goal.getJSONArray("subgoals");
		} catch (JSONException e) {
			// Single goal
			if(checkGoalMet(goal_condition)) {
				goals_met = true;
				return goals_met;
			} else {
				return goals_met;
			}
		}
		
		if(goal_condition.equals(this.goal_conditions.get(0))) {
			for(int i = 0; i < player_goal_requirements.length(); i++) {
				JSONObject goal_cond_obj = player_goal_requirements.getJSONObject(i);
				String goal_cond = goal_cond_obj.getString("goal");
				if(this.goal_requirements.contains(goal_cond)) {
					if(checkGoalMet(goal_cond)) {
						goals_met = true;
					} else {
						goals_met = false;
						return goals_met;
					}
				}
			}
		} 
		
		if (goal_condition.equals(this.goal_conditions.get(1))) {
			for(int j = 0; j < player_goal_requirements.length(); j++) {
				JSONObject goal_cond_obj = player_goal_requirements.getJSONObject(j);
				String goal_cond = goal_cond_obj.getString("goal");
				if(this.goal_requirements.contains(goal_cond)) {
					if(checkGoalMet(goal_cond)) {
						goals_met = true;
						return goals_met;
					} 
				}
			}
		}
		
		return goals_met;
	
    }

	@Override
	public String getImageID() {
		return "Exit Image";
	}
	
	@Override
	 public String getImagePath() {
		return this.image_path;
	}

	@Override
	public ArrayList<String> getImageList() {
		return image_list;
	}

    @Override
    public String toString() {
    	return "EXIT object";
    }

	private ArrayList<String> getGoal_requirements() {
		return goal_requirements;
	}
	
	private boolean checkGoalMet(String goal) {
		if(goal.equals(this.goal_requirements.get(0))) {
			if(dungeon.getEntityOfType(EntityType.TREASURE).size() == 0) {
				return true;
			}
		} else if (goal.equals(this.goal_requirements.get(1))) {
			if(dungeon.getEntityOfType(EntityType.ENEMY).size() == 0) {
				return true;
			}
		} else if (goal.equals(this.goal_requirements.get(2))) {
			List<Entity> switches = dungeon.getEntityOfType(EntityType.SWITCH);
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
