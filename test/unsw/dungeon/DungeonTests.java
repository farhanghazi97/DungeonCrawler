package unsw.dungeon;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class DungeonTests {

	  /**
     * WWW
     * WPW
     * WWW
     */
    public static final String ALL_WALLS_JSON = "test_all_walls.json";


    /**
     * WWW
     * WP_
     * WWW
     */
    public static final String EMPTY_AND_WALLS_JSON = "test_empty_and_walls.json";


    /**
     * ___
     * B__
     * PBB
     *
     */
    public static final String BOULDERS_JSON = "test_boulders.json";
    
    @BeforeEach
    void setUp() throws FileNotFoundException, InterruptedException {

    }

    @AfterEach
    void tearDown() {

    }
    
    private void initializeDungeon(String filename) {
        try {
            MockDungeonControllerLoader mdcl = new MockDungeonControllerLoader(filename);

            DungeonController controller = mdcl.loadController();
            Dungeon dungeon = controller.getDungeon();
            Mediator.getInstance().setDungeon(controller.getDungeon(), controller.getSquares(), controller.getInitialEntities());
        } catch (FileNotFoundException e) {
            fail("Test Failed - File not found");
        }
    }
    

    @Test
    //Moving to a new coordinate
    void moveTo_empty() {
        initializeDungeon(EMPTY_AND_WALLS_JSON);
        Mediator.getInstance().moveTo(1, 1, 1, 2);
        Dungeon dungeon = Mediator.getInstance().getDungeon();
        Player player = dungeon.getPlayer();
        //Player moves.
        assertEquals(1, player.getX());
        assertEquals(2, player.getY());
    }
    
    @Test
    //Movemenet blocked by walls
    void moveTo_all_walls() {
        initializeDungeon(ALL_WALLS_JSON);
        Mediator.getInstance().moveTo(1, 1, 1, 2);
        Dungeon dungeon = Mediator.getInstance().getDungeon();
        Player player = dungeon.getPlayer();
        //Player unable to move.
        assertEquals(1, player.getX());
        assertEquals(1, player.getY());
    }
    


    @Test
    void move_A_Boulder() {
        initializeDungeon(BOULDERS_JSON);
        Mediator.getInstance().moveTo(2, 0, 1, 0);
        Dungeon dungeon = Mediator.getInstance().getDungeon();
        Player player = dungeon.getPlayer();
        Entity boulder = getEntity(1,0, dungeon.getEntities(), Boulder.class);

        //Player moves.
        assertEquals(1, player.getX());
        assertEquals(0, player.getY());
        //Boulder is also at new position.
        assertEquals(1, boulder.getX());
        assertEquals(0, boulder.getY());

        //Move Boulder this time, not the player
        Mediator.getInstance().moveTo(1, 0, 0, 0);
        assertEquals(1, player.getX());
        assertEquals(0, player.getY());
        assertEquals(0, boulder.getX());
        assertEquals(0, boulder.getY());

    }

    @Test
    void move_Two_Boulder_Do_NotMove() {
        initializeDungeon(BOULDERS_JSON);

        Dungeon dungeon = Mediator.getInstance().getDungeon();
        Player player = dungeon.getPlayer();
        Entity boulder1 = getEntity(2,1, dungeon.getEntities(), Boulder.class);
        Entity boulder2 = getEntity(2,2, dungeon.getEntities(), Boulder.class);

        //Try moving up , where 2 boulders are cascaded.
        Mediator.getInstance().moveTo(2, 2, 2, 1);

        //Neither player moves, nor does either of the boulders.
        assertEquals(2, player.getX());
        assertEquals(0, player.getY());

        assertEquals(2, boulder1.getX());
        assertEquals(1, boulder1.getY());

        assertEquals(2, boulder2.getX());
        assertEquals(2, boulder2.getY());

    }

    public static Entity getEntity(int x, int y, List<Entity> entities, Class clazz){
        List<Entity> list = new LinkedList<>();
        for (Entity entity : entities) {
            if (entity.getX() == x && entity.getY() == y && clazz.isInstance(entity)) {
                list.add(entity);
            }
        }
        /*
        We will keep only one entity of a type at a square in tests.
         */
        return list.get(0);
    }


    
    
    
    
    
	

}
