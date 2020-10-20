import com.badlogic.gdx.math.Vector2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BonusTest extends GdxTest {

    private Bonus testBonus;
    private Player testPlayer;

    @BeforeEach
    void setUp() {
        testBonus = new Bonus(0,0);
        testPlayer = new Player(new Vector2(0,0));
    }

    @Test
    void initialize() {
        assertEquals(0, testBonus.row,  "Initial row is 0");
        assertEquals(0, testBonus.col,  "Initial col is 0");
        assertEquals(true, testBonus.active, "active starts true");
        assertEquals(99, testBonus.cost, "cost is 99");
        assertEquals(false, testBonus.collidable, "Bonus isn't collidable");
        assertEquals(false, testBonus.isCollected(), "collected starts false");
    }

    @Test
    void notActivePickUp() {
        testBonus.active = false;
        testBonus.interact(testPlayer);
        assertEquals(false, testPlayer.allRoundsExplosive(), "Player doesn't pick up Bonus");
    }

    @Test
    void activePickUp() {
        testBonus.interact(testPlayer);
        assertEquals(true, testPlayer.allRoundsExplosive(), "Player picks up Bonus and has explosive rounds");
    }

    @Test
    void bonusHasBeenCollected() {
        testBonus.interact(testPlayer);
        assertEquals(true, testBonus.isCollected(), "Bonus no longer active after pick up");
    }
}