//package com.gitlab.project.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * These are the bullets that fly through the air (not the ones on the UI)
 */
public class Bullet extends Entity {

    public Vector2 direction;

    private float speed = 400;

    private boolean isExplosive;
    private float explodeTime = 0;

    private Texture bulletTexture;
    private Texture explosiveTexture;
    private TextureRegion region;
    private Animation explodeAnimation;

    private Sound explosionSound = Gdx.audio.newSound(Gdx.files.internal("bullet/explosion.mp3"));
    private Sound impactSound = Gdx.audio.newSound(Gdx.files.internal("bullet/impact.ogg"));

    /**
     * Creates bullet
     * 
     * @param position    The position of the bullet
     * @param direction   The direction for the bullet to be travelling
     * @param isExplosive Whether or not it is explosive
     */
    public Bullet(Vector2 position, Vector2 direction, boolean isExplosive) {
        super(position, 22, 12);

        this.direction = direction;
        this.isExplosive = isExplosive;

        bulletTexture = new Texture(Gdx.files.internal("bullet/bullet.png"));
        explosiveTexture = new Texture(Gdx.files.internal("bullet/explosiveBullet.png"));

        if (isExplosive) {
            region = new TextureRegion(explosiveTexture);
        } else {
            region = new TextureRegion(bulletTexture);
        }

        TextureAtlas atlas = new TextureAtlas("bullet/explosion.txt");

        explodeAnimation = new Animation(0.07f, atlas.findRegions("explosion"), Animation.PlayMode.NORMAL);
    }

    /**
     * Uses the Entity default constructor which creates an inactive bullet
     */
    public Bullet() {
        super();
    }

    /**
     * Called when a bullet collides with something
     */
    public void hit() {
        if (explodeTime == 0) {
            if (isExplosive) {
                region = explodeAnimation.getKeyFrame(explodeTime, false);
                explodeTime += Gdx.graphics.getDeltaTime();
                width = region.getRegionWidth();
                height = region.getRegionHeight();
                position.sub(width / 2, height / 2);
                direction = new Vector2(1, 0);
                explosionSound.play();
            } else {
                impactSound.play();
                active = false;
            }
        }
    }

    @Override
    public void update() {
        if (active) {
            if (explodeTime == 0) {
                position = position.add(direction.cpy().scl(speed * Gdx.graphics.getDeltaTime()));
            } else {
                if (explodeAnimation.isAnimationFinished(explodeTime)) {
                    active = false;
                }

                region = explodeAnimation.getKeyFrame(explodeTime, false);
                explodeTime += Gdx.graphics.getDeltaTime();
            }

        }
    }

    @Override
    public void render(SpriteBatch batch) {
        if (active) {
            batch.draw(region, position.x, position.y, width / 2, height / 2, width, height, 1, 1, direction.angle());
        }
    }
}
