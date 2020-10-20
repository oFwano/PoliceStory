import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.Gdx;

/**
 * The Tile class is used to build a Map object which consists of a 2D array of
 * tiles A tile has a position, a texture, and a collidable property
 */
public class Tile extends Entity {
    public boolean collidable;
    public int row, col;
    public int cost = 99; // for use with pathfinding
    public Texture texture;

    BitmapFont font; // for debugging

    /**
     * Tile constructor Creates a new tile
     * 
     * @param path       the path to the file containing the texture for the tile
     * @param collidable a boolean which specifies whether or not an entity can pass
     *                   through the tile
     * @param row        the y position of the tile with respect to the 2D tiles
     *                   array
     * @param col        the x position of the tile with respect to the 2D tiles
     *                   array
     */
    public Tile(String path, boolean collidable, int row, int col) {
        super(new Vector2(col * 48, (15 - row) * 48), 48, 48);
        this.row = row;
        this.col = col;
        texture = new Texture(Gdx.files.internal(path));
        this.collidable = collidable;

        font = new BitmapFont();
    }

    /**
     * Overloaded Tile constructor Creates a new tile at the specified row, col
     * position, sets the tile texture to a default texture of floor/00.png, and
     * sets collidable to false
     * 
     * @param row the y position of the tile with respect to the 2D tiles array
     * @param col the x position of the tile with respect to the 2D tiles array
     */
    public Tile(int row, int col) {
        super(new Vector2(col * 48, (15 - row) * 48), 48, 48);
        texture = new Texture(Gdx.files.internal("floor/00.png"));
        collidable = false;
    }

    /**
     * The interact method specifies what happens when the player collides with the
     * tile The method is overridden in each child class to implement what should
     * happen if the input player interacts with that type of tile
     * 
     * @param player the player in which the interaction will happen to
     */
    public void interact(Player player) {
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y);

        if (Game.debug) {
            font.draw(batch, "" + cost, position.x + width / 2, position.y + height / 2);
        }
    }

    @Override
    public void update() {
    }
}