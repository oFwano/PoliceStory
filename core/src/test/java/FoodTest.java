
import com.badlogic.gdx.math.Vector2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FoodTest extends GdxTest {

    private Food testFood;
    private Player testPlayer;

    //run before each test
    @BeforeEach
    void setUp() {
        testFood = new Food(0, 0);
        testPlayer = new Player(new Vector2(0, 0));
    }

    @Test
    void initialize() {
        assertEquals(0, testFood.row,  "Initial row is 0");
        assertEquals(0, testFood.col,  "Initial col is 0");
        assertEquals(false, testFood.active, "Food starts with active = false");
        assertEquals(false, testFood.collidable, "Food isn't collidable");
        assertEquals(false, testFood.isCollected(), "collected starts false");
    }

    //player shouldn't pick up food when it isn't active
    @Test
    void notActivePickUp() {
        testPlayer.currentHealth -= 3; //7
        testFood.active = false;
        testFood.interact(testPlayer);
        assertEquals(7, testPlayer.currentHealth, "Health doesn't change");
        assertEquals(false, testFood.isCollected(), "Food hasn't been collected");
    }

    @Test
    void activePickUp() {
        testFood.active = true;
        testFood.interact(testPlayer);
        assertEquals(true, testFood.isCollected(), "Food has been collected");
    }

    //player heals when interacting with food
    @Test
    void playerHeals() {
        testPlayer.currentHealth -= 3; //7
        assertEquals(7, testPlayer.currentHealth, "Health decreases by 3");
        testFood.active = true;
        testFood.interact(testPlayer);
        assertEquals(10, testPlayer.currentHealth, "Health increases by 3");
    }
}