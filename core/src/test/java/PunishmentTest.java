import com.badlogic.gdx.math.Vector2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PunishmentTest extends GdxTest {

    private Punishment testPunish;
    private Player testPlayer;

    @BeforeEach
    void setUp() {
        testPunish = new Punishment(0,0);
        testPlayer = new Player(new Vector2(0,0));
    }

    @Test
    void initialize() {
        assertEquals(0, testPunish.row,  "Initial row is 0");
        assertEquals(0, testPunish.col,  "Initial col is 0");
        assertEquals(true, testPunish.active, "active starts true");
        assertEquals(99, testPunish.cost, "cost is 99");
        assertEquals(false, testPunish.collidable, "Punishment is collidable");
        assertEquals(0, testPunish.explosionTime, "Explosion time is 0");
    }

    @Test
    void playerHitsActivePunishment() {
        testPunish.interact(testPlayer);
        assertEquals(7, testPlayer.currentHealth, "Player correctly takes damage");
    }

    @Test
    void playerHitsInactivePunishment() {
        testPunish.active = false;
        testPunish.interact(testPlayer);
        assertEquals(10, testPlayer.currentHealth, "Player doesn't take damage");
    }
}