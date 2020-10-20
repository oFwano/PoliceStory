//package com.gitlab.project.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

//import static Game.elapsed;

/**
 * Places a mine that explodes when the player walks on it
 */
public class Punishment extends Tile {

    private Animation mineAnimation, explosionAnimation;
    TextureRegion region;
    TextureAtlas mineAtlas, explosionAtlas;
    float explosionTime = 0;

    private Sound explosionSound = Gdx.audio.newSound(Gdx.files.internal("bullet/explosion.mp3"));

    /**
     * Places a mine
     * 
     * @param row The row starting from zero and counting from top to bottom
     * @param col The column starting from zero and counting from left to right
     */
    public Punishment(int row, int col) {
        super(row, col);

        mineAtlas = new TextureAtlas("punishment/mine.txt");
        mineAnimation = new Animation(0.4f, mineAtlas.findRegions("mine"), Animation.PlayMode.LOOP_PINGPONG);
        region = mineAnimation.getKeyFrame(Game.elapsed, true);

        explosionAtlas = new TextureAtlas("bullet/explosion.txt");
        explosionAnimation = new Animation(0.07f, explosionAtlas.findRegions("explosion"),
                Animation.PlayMode.LOOP_PINGPONG);
    }

    @Override
    public void update() {
        if (active) {
            if (explosionTime == 0) {
                region = mineAnimation.getKeyFrame(Game.elapsed, true);
            } else {
                region = explosionAnimation.getKeyFrame(explosionTime, false);
                explosionTime += Gdx.graphics.getDeltaTime();
                if (explosionAnimation.isAnimationFinished(explosionTime)) {
                    active = false;
                }
            }
        }
    }

    @Override
    public void interact(Player player) {
        if (active && explosionTime == 0) {
            explosionTime += Gdx.graphics.getDeltaTime();
            player.hurt(3);
            explosionSound.play();
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(this.texture, this.position.x, this.position.y);

        if (this.active) {
            if (explosionTime == 0) {
                batch.draw(region, this.position.x, this.position.y);
            } else {
                batch.draw(region, this.position.x - 34, this.position.y);
            }
        }
    }
}
