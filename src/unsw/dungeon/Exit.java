package unsw.dungeon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class Exit extends Entity{
	
	private String image_path = "/exit.png";
	private ArrayList<String> image_list = new ArrayList<String>();
	private ArrayList<String> goal_requirements = new ArrayList<String>(Arrays.asList
			(
					"enemies" ,
					"treasure",
					"floor_switch",
					"exit"
			)
	); 
	
    public Exit(int x, int y) {
        super(x, y);
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
		
    	JSONObject goal = Mediator.getInstance().getGoal();
		String goal_condition = goal.getString("goal");
		JSONArray player_goal_requirements = goal.getJSONArray("subgoals");
		
		boolean goals_met = true;
		if(goal_condition.equals("AND")) {
			for(int i = 0; i < player_goal_requirements.length(); i++) {
				JSONObject goal_cond_obj = player_goal_requirements.getJSONObject(i);
				String goal_cond = goal_cond_obj.getString("goal");
				if(this.goal_requirements.contains(goal_cond)) {
					if(!checkGoalMet(goal_cond)) {
						goals_met = false;
						break;
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
	public ArrayList<String> getImage_list() {
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
		if(goal.equals("treasure")) {
			if(MediatorHelper.getEntityOfType(EntityType.TREASURE).size() == 0) {
				return true;
			}
		} else if (goal.equals("enemies")) {
			if(MediatorHelper.getEntityOfType(EntityType.ENEMY).size() == 0) {
				return true;
			}
		}
		return false;
	}

}
