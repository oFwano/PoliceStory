//package com.gitlab.project.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

//import static Game.elapsed;

import java.util.Random;

/**
 * The Food class contains the Food Tile which gives the player explosive rounds
 * Food consists of an Animation, a TextureRegion, a TextureAtlas, a duration, a time, and a collected boolean
 */

public class Food extends Tile {

    private Animation animation;
    private TextureRegion region;
    private TextureAtlas atlas;
    private float time;
    private float duration;
    private boolean collected = false;

    /**
     * Food Constructor
     * Creates a new Food at the specified row, col position, sets the animation, and region to it's defaults
     * @param row the y position of the tile with respect to the 2D tiles array
     * @param col the x position of the tile with respect to the 2D tiles array
     */
    public Food(int row, int col) {
        super(row, col);

        atlas = new TextureAtlas("rewards/donut.txt");
        animation = new Animation(0.20f, atlas.findRegions("idle"), Animation.PlayMode.LOOP_PINGPONG);
        region = animation.getKeyFrame(Game.elapsed, true);

        active = false;

        duration = new Random().nextInt(4) + 5;
        time = new Random().nextInt((int) duration);

    }

    public boolean isCollected() {
        return collected;
    }

    /**
     * Checks if the Food hasn't been collected and is still active
     */
    public void update() {
        if (!collected) {
            if (active) {
                region = animation.getKeyFrame(Game.elapsed, true);
            }
            time += Gdx.graphics.getDeltaTime();
            if (duration < time) {
                time = 0;
                this.active = !this.active;
            }
        }
    }

    @Override
    public void interact(Player player) {
        if (active && !collected) {
            player.heal(3);
            collected = true;
        }
    }

    /**
     * Renders the Food to the screen if it is still active and not collected
     * @param batch the SpriteBatch in which this texture will be rendered
     */
    public void render(SpriteBatch batch) {
        batch.draw(this.texture, position.x, position.y);

        if (this.active && !collected) {
            batch.draw(region, position.x, position.y);
        }
    }

}