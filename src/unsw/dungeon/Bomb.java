package unsw.dungeon;

public class Bomb extends Entity {

	private String type = "BOMB";
	
	public Bomb(int x , int y) {
		super(x , y);
	}

	@Override
	public String getType() {
		return type;
	}
	
	@Override
	public void setType(String type) {
		this.type = type;
	}
	
}
