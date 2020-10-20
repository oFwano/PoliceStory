//package com.gitlab.project.core;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

//import static Game.elapsed;

/**
 * The Reward class extends Tile
 * A Reward consists of an Animation, a TextureRegion, and a TextureAtlas
 */
public class Reward extends Tile {

    private Animation animation;
    private TextureRegion region;
    private TextureAtlas atlas;

    /**
     * The Reward constructor
     * Creates a new Reward at the specified row, col position, sets the texture, creates a looping animation,
     * and creates a TextureRegion
     * @param row the y position of the tile with respect to the 2D tiles array
     * @param col the x position of the tile with respect to the 2D tiles array
     */
    public Reward(int row, int col) {
        super(row, col);

        atlas = new TextureAtlas("rewards/folder.txt");
        animation = new Animation(0.30f, atlas.findRegions("folder"), Animation.PlayMode.LOOP_PINGPONG);
        region = animation.getKeyFrame(Game.elapsed, true);
    }

    @Override
    public void interact(Player player) {
        if (player.getRectangle().overlaps(this.getRectangle())) {
            active = false;
        }
    }

    /**
     * Renders the Reward at its position x,y if it is active, otherwise just renders the floor texture
     * The floor texture is always rendered
     * @param batch the SpriteBatch in which this texture will be rendered
     */
    public void render(SpriteBatch batch) {
        batch.draw(this.texture, position.x, position.y);

        if (active) {
            region = animation.getKeyFrame(Game.elapsed, true);
            batch.draw(region, position.x, position.y);
        }
    }

}
