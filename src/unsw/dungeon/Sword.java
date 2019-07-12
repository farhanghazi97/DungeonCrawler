package unsw.dungeon;

import java.awt.Rectangle;

public class Sword extends Entity {

	private String type = "SWORD";
	
	public Sword(int x, int y) {
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
