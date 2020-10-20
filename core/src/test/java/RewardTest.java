import com.badlogic.gdx.math.Vector2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RewardTest extends GdxTest {

    private Reward testReward;
    private Player testPlayer;

    @BeforeEach
    void setUp() {
        testReward = new Reward(0,0);
        testPlayer = new Player(new Vector2(0,0));
    }

    @Test
    void initialize() {
        assertEquals(0, testReward.row,  "Initial row is 0");
        assertEquals(0, testReward.col,  "Initial col is 0");
        assertEquals(true, testReward.active, "active starts true");
        assertEquals(99, testReward.cost, "cost is 99");
        assertEquals(false, testReward.collidable, "Reward isn't collidable");
    }

    @Test
    void notActivePickUp() {
        testReward.active = false;
        testReward.interact(testPlayer);
        assertEquals(false, testReward.active, "Player doesn't pick up Reward");
    }

    @Test
    void activePickUp() {
        testReward.interact(testPlayer);
        assertEquals(true, testReward.active, "Player picks up Reward and active is set to false");
    }
}