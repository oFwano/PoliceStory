//package com.gitlab.project.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.Random;

//import static Game.elapsed;

/**
 * The Bonus class contains the bonus reward which gives the player explosive
 * rounds Bonus consists of an Animation, a TextureRegion, a TextureAtlas, a
 * duration, a time, and a collected boolean
 */
public class Bonus extends Tile {

    private Animation animation;
    private TextureRegion region;
    private TextureAtlas atlas;
    private float duration;
    private float time;
    private boolean collected = false;

    /**
     * Bonus constructor Creates a new Bonus at the specified row, col position,
     * sets the texture, animation, and region to it's defaults Sets a duration and
     * time at which the Bonus will render to the screen
     * 
     * @param row the y position of the tile with respect to the 2D tiles array
     * @param col the x position of the tile with respect to the 2D tiles array
     */
    public Bonus(int row, int col) {
        super(row, col);

        atlas = new TextureAtlas("rewards/ammo.txt");
        animation = new Animation(0.30f, atlas.findRegions("ammo"), Animation.PlayMode.LOOP_PINGPONG);
        region = animation.getKeyFrame(Game.elapsed, true);

        duration = (float) new Random().nextInt(4) + 5;
        time = new Random().nextInt((int) duration);
    }

    public boolean isCollected() {
        return collected;
    }

    @Override
    public void interact(Player player) {
        if (active && !collected) {
            player.giveExplosiveRounds();
            collected = true;
        }
    }

    /**
     * Checks if the Bonus hasn't been collected and is still active
     */
    public void update() {
        if (!collected) {
            if (active) {
                region = animation.getKeyFrame(Game.elapsed, true);
            }
            time += Gdx.graphics.getDeltaTime();
            if (duration < time) {
                time = 0;
                active = !active;
            }
        }
    }

    /**
     * Renders the Bonus to the screen if it is still active and not collected
     * 
     * @param batch the SpriteBatch in which this texture will be rendered
     */
    public void render(SpriteBatch batch) {
        batch.draw(this.texture, position.x, position.y);

        if (this.active && !collected) {
            batch.draw(region, position.x, position.y);
        }
    }

}
