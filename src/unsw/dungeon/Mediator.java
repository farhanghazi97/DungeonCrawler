package unsw.dungeon;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import javafx.scene.layout.GridPane;

//Singleton class
public class Mediator {

	// List of entities collected by player
	private List<Entity> collectedEntities = new LinkedList<>();

	private static Mediator mediator = new Mediator();

	private Mediator() {
	}

	public static Mediator getInstance() {
		return mediator;
	}

	private Dungeon dungeon;
	private GridPane squares;

	public void setDungeon(Dungeon dungeon, GridPane squares) {
		this.dungeon = dungeon;
		this.squares = squares;
	}

	// To move from old coordinates to new coordinates
	public boolean moveTo(int currentX, int currentY, int newX, int newY) {

		if (gameOver) {
			return false;
		}

		if (outsideDungeon(newX, newY)) {
			// Whether moving boulder or player, outside dungeon boundaries is prohibited.
			return false;
		}

		// At start, entity to move is the player
		Entity entityToMove = dungeon.getPlayer();
		List<Entity> entitiesAtCurrent = getEntities(currentX, currentY);
		List<Entity> entitiesAtNew = getEntities(newX, newY);

		List<Entity> bouldersAtCurrent = getEntities(currentX, currentY, Boulder.class);
		List<Entity> swordAtCurrent = getEntities(currentX, currentY, Sword.class);
		List<Entity> potionAtCurrent = getEntities(currentX, currentY, Potion.class);

		if (!bouldersAtCurrent.isEmpty()) {
			// there is a boulder at currentX and currentY
			// We will move boulder instead of player
			entityToMove = bouldersAtCurrent.get(0);
		}

		if (!swordAtCurrent.isEmpty()) {
			// there is a sword at currentX and currentY
			Entity sword = swordAtCurrent.get(0);
			if (!isCollected(sword)) {
				System.out.println("Sword collected");
				collectedEntities.addAll(swordAtCurrent);

				if (sword.stepOver())
					;

				// Remove sword from board.. how?
				removeEntity(sword);
			}
		}

		if (!potionAtCurrent.isEmpty()) {
			// there is a sword at currentX and currentY
			Entity potion = potionAtCurrent.get(0);

			System.out.println("Potion collected");
			potion.stepOver();
			// Remove potion from board.. how?
			removeEntity(potion);
		}

		if (entityToMove.isBlocked(entitiesAtNew)) {
			return false;
		}

		entityToMove.moveTo(newX, newY);
		// entityToMove.collect(entitiesAtNew);
		entityToMove.postMove(entitiesAtNew);

		return true;
	}

	private boolean gameOver = false;

	public void markGameOver() {
		gameOver = true;
	}

	// HELPER OPERATIONS BELOW

	// Returns true if the new coordinates given are outside the boundaries of the
	// dungeon
	private boolean outsideDungeon(int newX, int newY) {
		int width = dungeon.getWidth();
		int height = dungeon.getHeight();
		if (newX + 1 > width || newY + 1 > height) {
			return true;
		}
		return false;
	}

	// Returns all entities on (x,y) coordinates
	private List<Entity> getEntities(int x, int y) {
		List<Entity> list = new LinkedList<>();
		for (Entity entity : dungeon.getEntities()) {
			if (entity.getX() == x && entity.getY() == y) {
				list.add(entity);
			}
		}
		return list;
	}

	// Returns a specific type of entity class if it at a given (x,y) coordinate
	private List<Entity> getEntities(int x, int y, Class clazz) {
		List<Entity> list = new LinkedList<>();
		for (Entity entity : dungeon.getEntities()) {
			if (entity.getX() == x && entity.getY() == y && clazz.isInstance(entity)) {
				list.add(entity);
			}
		}
		return list;
	}

	// Returns true if the player already has newEntity in collected
	private boolean isCollected(Entity newEntity) {
		for (Entity collected : collectedEntities) {
			if (collected.getType() == newEntity.getType()) {
				return true;
			}
		}
		return false;
	}

	// Returns entity if the player already has an entity of given type
	private Entity getCollected(EntityType entityType) {
		for (Entity collected : collectedEntities) {
			if (collected.getType() == entityType) {
				return collected;
			}
		}
		return null;
	}

	private void removeEntity(Entity entity) {
		System.out.println("In remove entity");
		squares.getChildren().remove(entity);

	}
}
