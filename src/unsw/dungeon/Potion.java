package unsw.dungeon;

import java.awt.Rectangle;

public class Potion extends Entity {

	private String type = "POTION";
	
	public Potion(int x, int y) {
        super(x, y);
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
