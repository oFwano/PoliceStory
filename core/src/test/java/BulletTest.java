import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
//import Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

public class BulletTest extends GdxTest {
    Bullet testBullet = new Bullet(new Vector2(0, 0), new Vector2(0, 0), false);

    @Test
    void testHit() {
        testBullet.hit();
        assertTrue(!testBullet.active, "bullet has been hit");
    }

}