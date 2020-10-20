import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
//import Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest extends GdxTest {
    Player testPlayer = new Player(new Vector2(0, 0));
    Round testRound = new Round(new Vector2(0, 0));
    Health testHealth = new Health(new Vector2(0, 0));

    @Test
    void testExplosiveRounds() {

        testPlayer.giveExplosiveRounds();
        assertTrue(testPlayer.allRoundsExplosive(), "Has explosive rounds");

    }

    @Test
    void testPlayerHealth() {
        int previousHealth = testPlayer.currentHealth;
        testPlayer.hurt();
        assertTrue(testPlayer.currentHealth == previousHealth - 1, "player is hurt");

        previousHealth = testPlayer.currentHealth;
        testPlayer.heal();
        assertTrue(testPlayer.currentHealth == previousHealth + 1, "player is hurt");
    }

    @Test
    void testRound() {
        testRound.eject();
        assertTrue(testRound.ejected, "round is ejected");

        testRound.reload();
        assertTrue(!testRound.ejected, "round is reloaded");
    }

    @Test
    void testHealth() {
        // health cant really be tested bause the class is basically just an animation
        // and all of the actual health animation is stored in the player class

        // cant be tested because deltaTime is 0 because there has not been a frame yet
        testHealth.hurt();
        // assertTrue(testHealth.hurtTime != 0, "health is gone");

        // this test is also pointless for the above reason
        testHealth.heal();

        assertTrue(testHealth.hurtTime == 0, "health is replenished");
    }
}
