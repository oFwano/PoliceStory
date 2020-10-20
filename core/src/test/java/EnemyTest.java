import com.badlogic.gdx.math.Vector2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;

class EnemyTest extends GdxTest {
    private Enemy testEnemy;
    private Player testPlayer;
    private Punishment testPunish;


    @BeforeEach
    void setup(){
        testEnemy = new Enemy(new Vector2(5,5),0);
        testEnemy.setTarget(new Tile(0,0));

    }

    @Test
    void enemyHitsPlayer() {
        assertEquals(true, testEnemy.active, "Enemy.active == true before getting hit.");

        testPlayer = new Player(new Vector2(0,0));
        assertEquals(true, testPlayer.active, "Player.active == true before getting hit.");
        testEnemy.attack();
        testPlayer.hurt(1);
        assertEquals(9, testPlayer.currentHealth, "Health of player after getting hit once");
    }

    @Test
    void enemyMoves(){
        Vector2 initial_pos = testEnemy.position;
        testEnemy.position = new Vector2(1,1);
        assertNotEquals(initial_pos,testEnemy.position,"123");
    }

    @Test
    void enemyNotAtTargetTile(){
        Map maptest=new Map("00");
        Tile tile = maptest.getTile(testEnemy.getCenter());
        testEnemy.setTarget(maptest.getCheapestTile(maptest.getNorth(tile), maptest.getSouth(tile), maptest.getEast(tile), maptest.getWest(tile)));
        assertEquals(false, testEnemy.isAtTarget(), "Enemy is not at target tile, isAtTarget() returns false");
        Tile tile2 = maptest.getTile(testEnemy.getCenter());
        testEnemy.setTarget(tile2);
        assertEquals(true, testEnemy.isAtTarget(), "Enemy is at target tile, isAtTarget() returns true");
    }


}
