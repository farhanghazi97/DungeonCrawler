package unsw.dungeon;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

//TODO
//Test if player dies when it touches bomb
//Test if bomb destroys boulders in vicinity
//Test if bomb destroys enemies in vicinity

//Test if player dies when it touches sword 

//Test if player under invincibility potion lives if it touches bomb - DONE
//Test if player under invincibility potion lives if it touches enemy - DONE

//test if a given goal is complete AND player reaches exit, game is marked over
//Test if game is not marked over if player reaches exit without goal requirement completed

//test if enemy is destroyed when it touches player with potion - DONE
//test one potion carriable - DONE
//Test if enemy moves cloder to player without potion
//test if enemy moves further away from a player with potion


class DungeonTests {

	  /**
     * WWW
     * WPW
     * WWW
     */
    public static final String ALL_WALLS_JSON = "test_all_wall.json";

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
    
    public static final String WALLS_AND_DOOR_JSON  = "test_boulders.json";
    public static final String EMPTY_AND_DOOR_JSON  = "test_empty_and_door.json";
    
    public static final String KEY_JSON_PASS = "test_key_door_pass.json";
    public static final String KEY_JSON_FAIL  = "test_key_door_fail.json";
    
    public static final String POTION_JSON 	  = "test_potion_collected.json";
    public static final String TREASURE_JSON = "test_treasure_collected.json";
    public static final String SWORD_JSON 	  = "test_sword_collected.json";
    public static final String BOMB_JSON 	      = "test_bomb_collected.json";
    public static final String ALL_ENTITIES_JSON 	      = "all_entities.json";
    
    @BeforeEach
    void setUp() throws FileNotFoundException, InterruptedException {
    	RefreshInventory();
    }

    @AfterEach
    void tearDown() {

    }
    
    private void initializeDungeon(String filename) {
        try {
            MockDungeonControllerLoader mdcl = new MockDungeonControllerLoader(filename);
            DungeonController controller = mdcl.loadController();
            JSONObject goal = new JSONObject();
            Mediator.getInstance().setDungeon(controller.getDungeon(), controller.getSquares(), controller.getInitialEntities() , goal);
        } catch (FileNotFoundException e) {
            fail("Test Failed - File not found");
        }
    }
    
    //NAVIGATING PLAYER TESTS
    
    //Testing if player moves based on coordinates given
    @Test
    void moveTo_empty() {
        initializeDungeon(EMPTY_AND_WALLS_JSON);
        Mediator.getInstance().moveTo(1, 1, 1, 2);
        Dungeon dungeon = Mediator.getInstance().getDungeon();
        Player player = dungeon.getPlayer();
        //Player moves.
        assertEquals(1, player.getX());
        assertEquals(2, player.getY());
    }
    
    //Testing if player is succesfully blocked by walls
    @Test
    void moveTo_all_walls() {
        initializeDungeon(ALL_WALLS_JSON);
        Mediator.getInstance().moveTo(1, 1, 1, 2);
        Dungeon dungeon = Mediator.getInstance().getDungeon();
        Player player = dungeon.getPlayer();
        //Player unable to move.
        assertEquals(1, player.getX());
        assertEquals(1, player.getY());
    }
    
    //Testing if player is succesfully blocked by doors
    @Test
    void moveTo_empty_and_doors() {
        initializeDungeon(EMPTY_AND_DOOR_JSON);
        Mediator.getInstance().moveTo(1, 2, 1, 1);
        Dungeon dungeon = Mediator.getInstance().getDungeon();
        Player player = dungeon.getPlayer();
        //Player unable to move.
        assertEquals(1, player.getX());
        assertEquals(2, player.getY());
    }
    
    //BOULDER TESTS
    
    //Test to check if a boulder can be moved by player
    @Test
    void move_A_Boulder() {
        
    	initializeDungeon(BOULDERS_JSON);
        Mediator.getInstance().moveTo(2, 0, 1, 0);
        Dungeon dungeon = Mediator.getInstance().getDungeon();
        Player player = dungeon.getPlayer();
        Entity boulder = getEntity(1,0, dungeon.getEntities(), Boulder.class);

        // Player moves.
        assertEquals(1, player.getX());
        assertEquals(0, player.getY());
        
        // Boulder is also at new position.
        assertEquals(1, boulder.getX());
        assertEquals(0, boulder.getY());

        // Move Boulder this time, not the player
        Mediator.getInstance().moveTo(1, 0, 0, 0);
        assertEquals(1, player.getX());
        assertEquals(0, player.getY());
        assertEquals(0, boulder.getX());
        assertEquals(0, boulder.getY());

    }

    //Test to check if a boulder is blocked by another boulder 
    @Test
    void move_Two_Boulders_Do_NotMove() {
        initializeDungeon(BOULDERS_JSON);

        Dungeon dungeon = Mediator.getInstance().getDungeon();
        Player player = dungeon.getPlayer();
        Entity boulder1 = getEntity(2,1, dungeon.getEntities(), Boulder.class);
        Entity boulder2 = getEntity(2,2, dungeon.getEntities(), Boulder.class);

        // Try moving up , where 2 boulders are cascaded.
        Mediator.getInstance().moveTo(2, 2, 2, 1);

        // Neither player moves, nor does either of the boulders.
        assertEquals(2, player.getX());
        assertEquals(0, player.getY());

        assertEquals(2, boulder1.getX());
        assertEquals(1, boulder1.getY());

        assertEquals(2, boulder2.getX());
        assertEquals(2, boulder2.getY());

    }
    
    //KEY TESTS
    @Test
    void test_key_collected() {
    	initializeDungeon(KEY_JSON_PASS);
    	Dungeon dungeon = Mediator.getInstance().getDungeon();
    	
    	Entity key = getEntity(1 , 3 , dungeon.getEntities() , Key.class);
    	
    	//Player moves on key
    	Mediator.getInstance().moveTo(1 , 3 , 1, 2);
    	assertTrue(Mediator.getInstance().getIsCollected(key));
    }
    
    
    //KEY-DOOR TESTS
    
    @Test
    void TestNoKeyDoorUnlock() {
    	initializeDungeon(KEY_JSON_PASS);
    	Dungeon dungeon = Mediator.getInstance().getDungeon();
    	Entity entity = getEntity(3 , 0 , dungeon.getEntities() , Door.class);
    	Door door = (Door) entity;
    	assertFalse(door.stepOver());
    	assertFalse(door.isOpen());
    }
    

    

    
    @Test
    void TestKeyDoorFail() {
    	initializeDungeon(KEY_JSON_FAIL);
    	Dungeon dungeon = Mediator.getInstance().getDungeon();
    	Entity key = getEntity(1 , 3 , dungeon.getEntities() , Key.class);
    	Entity entity = getEntity(3 , 0 , dungeon.getEntities() , Door.class);
    	Door door = (Door) entity;
    	Mediator.getInstance().getCollectedEntities().add(key);
    	assertFalse(door.stepOver());
    	assertFalse(door.isOpen());
    }
    
    @Test
    void TestMaxOneKeyCarriable() {
    	test_key_collected();
    	Dungeon dungeon = Mediator.getInstance().getDungeon();
    	Player player = dungeon.getPlayer();
    	Entity key = getEntity(3 , 3 , dungeon.getEntities() , Key.class);
    	Mediator.getInstance().moveTo(player.getX(), player.getY(), 3, 3);
    	Mediator.getInstance().moveTo(player.getX(), player.getY(), 3, 4);
    	assertEquals(1, Mediator.getInstance().getCollectedEntities().size());
    	assertFalse(key.stepOver());
    }
    
    //TREASURE TESTS
    
    @Test
    void test_treasure_collected() {
    	initializeDungeon(TREASURE_JSON);
    	Dungeon dungeon = Mediator.getInstance().getDungeon();
    	
    	Entity treasure = getEntity(3 , 3 , dungeon.getEntities() , Treasure.class);
    	
    	//Player moves on treasure
    	Mediator.getInstance().moveTo(3, 3, 3, 4);
    	assertTrue(Mediator.getInstance().getIsCollected(treasure));
    }
    
    //INVINCIBILITY POTION TESTS
    @Test
    void test_potion_collected() {
    	initializeDungeon(ALL_ENTITIES_JSON);
    	Dungeon dungeon = Mediator.getInstance().getDungeon();
    	Entity potion = getEntity(11 , 7 , dungeon.getEntities() , Potion.class);

    	//Player moves on to square containing potion, and then away from it.
    	Mediator.getInstance().moveTo(3 , 7 , 11, 7);
    	Mediator.getInstance().moveTo(11 , 7 , 3, 7);
    	
    	assertTrue(Mediator.getInstance().getIsCollected(potion));
    }
    
    
//    //Test to check if the enemy dies if a player under potion's effects comes in contact with it
//    @Test
//    void test_enemy_dies_by_potion() {
//    	test_potion_collected();              
//    	Dungeon dungeon = Mediator.getInstance().getDungeon();
//    	Player player = dungeon.getPlayer();
//    	
//    	assert(Mediator.getInstance().getCollected(EntityType.POTION) != null);
//    	
//    	//Enemy exists at (15,8)
//    	assertEquals(1, MediatorHelper.getEntities(2,7,Enemy.class).size());
//    	
//    	//Player moves on enemy
//    	Mediator.getInstance().moveTo(player.getX() , player.getY(), 15, 8);
//    	Mediator.getInstance().moveTo(player.getX() , player.getY(), 15, 7);
//    	
//    	//Enemy at (15,8) destroyed
//    	assertEquals(0, MediatorHelper.getEntities(15,8,Enemy.class).size());
//    }
    
    //Test to check if the player can carry only potion at a time
    @Test
    void test_one_potion_carriable() {
    	test_potion_collected();              
    	Dungeon dungeon = Mediator.getInstance().getDungeon();
    	Player player = dungeon.getPlayer();
    	
    	//Player has  potion in collected entites bag
    	assert(Mediator.getInstance().getCollected(EntityType.POTION) != null);
    	
    	//Second potion exists at (11,9)
    	assertEquals(1, MediatorHelper.getEntities(11,9,Potion.class).size());
    			;
    	//Player moves on potion
    	Mediator.getInstance().moveTo(player.getX() , player.getY(), 11, 9);
    	Mediator.getInstance().moveTo(player.getX() , player.getY(), 11, 10);
    	
    	//Second potion still exists
    	assertEquals(1, MediatorHelper.getEntities(11,9,Potion.class).size());
    }
    
    //Test to check if an exploding bomb has no effect on a player with a potion
    @Test
    void test_invincibility_from_bomb() {
    	test_potion_collected();   
    	
    	Dungeon dungeon = Mediator.getInstance().getDungeon();
    	Player player = dungeon.getPlayer();
    	
    	//Player has potion
    	assert(Mediator.getInstance().getCollected(EntityType.POTION) != null);
    	
    	//Player moves on to square containing bomb
    	Mediator.getInstance().moveTo(player.getX() , player.getY() , 13, 4);
    	
    	//Player drops bomb
    	Mediator.getInstance().igniteBomb(13,4);
    	//Player moves in vicinity
    	//NOTE: The test does not work if I comment the next line out, however in the actual game it does.
    	//So player has to move in vicinity. Player cannot stay standing on the same space for the test to work.
    	Mediator.getInstance().moveTo(13, 4, 12, 4);
    	
    	//Player still lives
    	assertFalse(Mediator.getInstance().getGameOver());
    	//Bomb at (13,4) destroyed
    	assertEquals(0, MediatorHelper.getEntities( 13,4,Bomb.class).size());
    	
    }
    
    
   

   
    //SWORD TESTS
    
    //Tests if the sword entity is collected
    @Test
    void test_sword_collected() {
    	initializeDungeon(SWORD_JSON);
    	Dungeon dungeon = Mediator.getInstance().getDungeon();
    	Entity sword = getEntity(0 , 0 , dungeon.getEntities() , Sword.class);
    	
    	//Player collects sword
    	Mediator.getInstance().moveTo(0 ,0 , 0, 1);
    	assertTrue(Mediator.getInstance().getIsCollected(sword));
    }
    
    //Tests if only one sword is carried by the player at any given time
    @Test
    void test_one_sword_carriable() {
    	//first sword collected
    	test_sword_collected();
    	Dungeon dungeon = Mediator.getInstance().getDungeon();
    	Entity secondSword = getEntity(2 , 2 , dungeon.getEntities() , Sword.class);
    	//Player collects second sword
    	Mediator.getInstance().moveTo(2, 2, 2, 1);
    	
    	assertEquals(1, Mediator.getInstance().getCollectedEntities().size());
    	assertFalse(secondSword.stepOver());
    }
    


   
    
    //Tests if sword expires after 5 swings
    @Test
    void test_sword_swings_count() {
    	test_sword_collected();
    	Entity sword = Mediator.getInstance().getCollected(EntityType.SWORD);
    	
    	int numberOfSwings = 5;
    	while(((Sword) sword).swing()) {
    		numberOfSwings--;
    	}
    	
    	assertEquals(1, numberOfSwings);
    	assertNull(Mediator.getInstance().getCollected(EntityType.SWORD));
    }
    
    
    //BOMB TESTS
    @Test
    void test_bomb_collected() {
    	initializeDungeon(BOMB_JSON);
    	Dungeon dungeon = Mediator.getInstance().getDungeon();
    	
    	Entity bomb = getEntity(0 ,0 , dungeon.getEntities() , Bomb.class);
    	
    	Mediator.getInstance().moveTo(0, 0, 0, 1);
    	assertTrue(Mediator.getInstance().getIsCollected(bomb));
    }
    
    
    @Test
    void test_one_bomb_carriable() {
    	test_bomb_collected();
    	Dungeon dungeon = Mediator.getInstance().getDungeon();
    	Entity secondBomb = getEntity(2 , 2 , dungeon.getEntities() , Bomb.class);
    	Mediator.getInstance().moveTo(2, 2, 2, 1);
    	assertEquals(1, Mediator.getInstance().getCollectedEntities().size());
    	assertFalse(secondBomb.stepOver());
    }
    
    //Tests if player without potion steps on a bomb
//    @Test
//    void test_game_over_player_on_bomb() {
//    	initializeDungeon(ALL_ENTITIES_JSON);
//    	Dungeon dungeon = Mediator.getInstance().getDungeon();
//    	Player player = dungeon.getPlayer();
//    	
//    	Entity bomb = getEntity(6 ,13 , dungeon.getEntities() , Bomb.class);
//    	
//    	Mediator.getInstance().igniteBomb(6,13);
//    	//Player moves on bomb
//    	Mediator.getInstance().moveTo(player.getX(), player.getY(), 6, 13);
//    	assertTrue(Mediator.getInstance().getGameOver());
//    }
    
  //Tests if player without potion comes in vicinity of a bomb
//    @Test
//    void test_game_over_player_in_vicinity_of_bomb() {
//    	initializeDungeon(ALL_ENTITIES_JSON);
//    	Dungeon dungeon = Mediator.getInstance().getDungeon();
//    	Player player = dungeon.getPlayer();
//    	Entity bomb = getEntity(6 ,13 , dungeon.getEntities() , Bomb.class);
//    
//    	Mediator.getInstance().igniteBomb(6,13);
//    	//Player moves in vicinity of a bomb
//    	Mediator.getInstance().moveTo(player.getX(), player.getY(), 6, 14);
//    	
//    	assertTrue(Mediator.getInstance().getGameOver());
//    }
//    
    
    
    //Tests if bomb destroys enemy in vicinity
    @Test
//    void test_bomb_kills_enemy() throws InterruptedException {
//    	test_bomb_collected();
//    	
//    	Dungeon dungeon = Mediator.getInstance().getDungeon();
//    	Player player = dungeon.getPlayer();
//    	Entity bomb = Mediator.getInstance().getCollected(EntityType.BOMB);
//    	
//    	assert(bomb != null);
//    	
//    	//Checking if one enemy exists
//    	assertEquals(1, Mediator.getInstance().getEntities(3,2,Enemy.class).size());
//    	
//    	//Player moves to 1 unit below the enemy  
//    	Mediator.getInstance().moveTo(player.getX(), player.getY(), 3, 3);
//    	
//    	//Player ignites bomb
//    	Mediator.getInstance().igniteBomb(3, 3);
//    	Thread.sleep(10000);
//    	//Checking if enemy is removed after explosion
//    	assertEquals(0 , Mediator.getInstance().getEntities(3, 2, Enemy.class).size());
//    	
//    }
  
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

    public static void RefreshInventory() {
    	if(!Mediator.getInstance().getCollectedEntities().isEmpty()) {
    		for(int i = 0; i < Mediator.getInstance().getCollectedEntities().size(); i++) {
    			Mediator.getInstance().getCollectedEntities().remove(i);
    		}
    	}
    }
    
    
    
//  @Test
//  void TestKeyDoorPass() {
//  	initializeDungeon(KEY_JSON_PASS);
//  	Dungeon dungeon = Mediator.getInstance().getDungeon();
//  	Player player = dungeon.getPlayer();
//  	Entity key = getEntity(1 , 3 , dungeon.getEntities() , Key.class);
//  	Entity door = getEntity(3 , 0 , dungeon.getEntities() , Door.class);
//  	
//  	//Player picks key up
//  	Mediator.getInstance().moveTo(player.getX(),player.getY(),1,3);
//  	Mediator.getInstance().moveTo(player.getX(),player.getY(),1,4);
//  	
//  	System.out.println(key);
//  	
//  	System.out.println(Mediator.getInstance().doorInVicinity(3, 1));
//  	
//  	//Mediator.getInstance().unlockDoor(3, 1);
//  	//assertTrue(door.stepOver());
//  	//assertTrue(door.isDoorOpen());
//  }
    
    
//  //Tests if sword destroys any enemy in vicinity
//  @Test
//  void test_sword_swing_kills_enemy() {
//  	test_sword_collected();
//  	Dungeon dungeon = Mediator.getInstance().getDungeon();
//  	Player player = dungeon.getPlayer();
//  	assert(Mediator.getInstance().getCollected(EntityType.SWORD) != null);
//  	
//  	// Checking if one enemy exists
//  	assertEquals(1, MediatorHelper.getEntities(3,2,Enemy.class).size());
//  	
//  	// Player moves to 1 unit below the enemy  
//  	Mediator.getInstance().moveTo(player.getX(), player.getY(), 3, 3);
//  	
//  	// Player swings sword
//  	Mediator.getInstance().swingSword(3, 3);
//  	
//  	// Checking if enemy is removed after sword swing
//  	assertEquals(0, MediatorHelper.getEntities(3,2,Enemy.class).size());
//  }
//  
}



