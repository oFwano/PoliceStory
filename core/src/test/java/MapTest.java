//import com.gitlab.project.core.Map;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class MapTest extends GdxTest {
    Map maptest=new Map("00");
    
    

    @Test
     void test(){
        assertEquals(false, maptest.levelComplete, "make sure is false");
        assertEquals(maptest.returntile(15,15),maptest.getTile(16, 16), "make sure get tile have limit 15");
        assertEquals(maptest.returntile(13, 2),maptest.getTile(new Vector2(96,96)), "make sure get tile have limit 15"); 
    }

    @Test
    void testCheapesttile(){
        Tile tile1 = new Tile(15, 15);
        tile1.cost = 50;
        Tile tile2 = new Tile(15, 15);
        tile2.cost = 60;
        assertEquals(tile1, maptest.getCheapestTile(tile1,tile2), "make sure return the smallest tile");
    }

    @Test
    void testDirection(){
        assertEquals(maptest.returntile(6, 7), maptest.getNorth(maptest.returntile(7, 7)), "make sure it go north for one step");
        assertEquals(maptest.returntile(8, 7), maptest.getSouth(maptest.returntile(7, 7)), "make sure it go south for one step");
        assertEquals(maptest.returntile(7, 6), maptest.getWest(maptest.returntile(7, 7)), "make sure it go west for one step");
        assertEquals(maptest.returntile(7, 8), maptest.getEast(maptest.returntile(7, 7)), "make sure it go east for one step");
    }

    @Test
    void testcollide(){
        Enemy enemy=new Enemy(new Vector2(5,5), 1);
        assertEquals(true, maptest.collide(enemy), "check collision between the map and an entity");
        
    }

}
