//package com.gitlab.project.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

//import static Game.elapsed;

/**
 * the enemy class creates a instance of a new enemy from Map.java class
 */
public class Enemy extends Entity {

    private Animation runningAnimation, idleAnimation, attackAnimation, hurtAnimation;
    private float hurtTime = 0;
    public float attackTime = 0;

    TextureRegion region;
    public Vector2 direction = new Vector2(1, 0);

    private Tile targetTile;
    public boolean isMoving;

    public boolean didDamage = false;

    public float speed = 115;

    public float attackDuration = 1.0f;
    public int type;

    private Sound hurtSound = Gdx.audio.newSound(Gdx.files.internal("enemy/hurt.mp3"));
    private Sound attackSound = Gdx.audio.newSound(Gdx.files.internal("enemy/attack.mp3"));

    /**
     * The main constructor for creating an enemy.
     * 
     * @param position The position of where the enemy will spawn.
     * @param type     The type of enemy it is (Type = 0 is path-finding enemy, Type
     *                 = 1 is a non-moving enemy).
     */
    public Enemy(Vector2 position, int type) {
        super(position, 0, 0);

        TextureAtlas atlas = new TextureAtlas("enemy/enemy.txt");

        // CREATE ANIMATION FIRST
        runningAnimation = new Animation(0.15f, atlas.findRegions("run"), Animation.PlayMode.LOOP);
        idleAnimation = new Animation(0.15f, atlas.findRegions("idle"), Animation.PlayMode.LOOP);
        attackAnimation = new Animation(attackDuration / 2, atlas.findRegions("attack"), Animation.PlayMode.LOOP);
        hurtAnimation = new Animation(0.40f, atlas.findRegions("hurt"), Animation.PlayMode.NORMAL);

        region = idleAnimation.getKeyFrame(Game.elapsed, true); // default

        this.width = region.getRegionWidth();
        this.height = region.getRegionHeight();
        this.type = type;
    }

    /**
     * Entity default constructor to make an non-active enemy
     */
    public Enemy() {
        super();
    }

    /**
     * Takes a tile as argument and the enemy will walk towards that tile.
     * 
     * @param tile the tile the enemy tries to walk towards.
     */
    public void setTarget(Tile tile) {
        this.targetTile = tile;
        this.direction = tile.getCenter().sub(this.getCenter()).nor();
        isMoving = true;
    }

    /**
     * Boolean function to check if the enemy is at target tile.
     * 
     * @return boolean This returns true if enemy is at target tile and false if
     *         not.
     */
    public boolean isAtTarget() {
        return targetTile.getRectangle().contains(this.getRectangle());
    }

    /**
     * The move function moves the enemy forward based on this instance's
     * this.direction.
     * 
     * @see Enemy#update()
     */
    public void move() {
        this.region = runningAnimation.getKeyFrame(Game.elapsed, true);
        position.add(this.direction.cpy().scl(this.speed * Gdx.graphics.getDeltaTime()));
    }

    /**
     * Function is used to count the difference in time when a enemy attacks
     * 
     * @see Enemy#update()
     */
    public void attack() {
        attackTime += Gdx.graphics.getDeltaTime();
        attackSound.play();
    }

    /**
     * function to determine the difference in time of when an enemy gets hit.
     * 
     * @see Enemy#update()
     */
    public void hit() {
        if (hurtTime == 0) {
            this.region = this.hurtAnimation.getKeyFrame(hurtTime, false);
            this.hurtTime += Gdx.graphics.getDeltaTime();
            hurtSound.play();
        }
    }

    @Override
    public void update() {
        if (this.active) {
            if (this.hurtTime != 0) {

                this.region = this.hurtAnimation.getKeyFrame(hurtTime, false);
                this.hurtTime += Gdx.graphics.getDeltaTime();

                if (this.hurtAnimation.isAnimationFinished(hurtTime)) {
                    this.active = false;
                }

            } else if (this.attackTime != 0) {

                this.region = attackAnimation.getKeyFrame(attackTime, false);
                attackTime += Gdx.graphics.getDeltaTime();

                if (attackDuration / 2 < attackTime) {
                    float attackSpeed = 500 * ((attackDuration - attackTime) / attackDuration);
                    position.add(this.direction.cpy().scl(attackSpeed * Gdx.graphics.getDeltaTime()));
                }

                if (this.attackAnimation.isAnimationFinished(attackTime)) {
                    this.attackTime = 0;
                    didDamage = false;
                }

            } else {
                region = idleAnimation.getKeyFrame(Game.elapsed, true);

                if (isAtTarget()) {
                    isMoving = false;
                }

                if (isMoving) {
                    if (this.type == 0) {
                        move();
                    }
                }

                if (targetTile.cost == 0) {
                    attack();
                }
            }

            boolean isRight = direction.angle() <= 90 || 270 <= direction.angle();

            if ((!isRight && region.isFlipX()) || (isRight && !region.isFlipX())) {
                region.flip(true, false);
            }
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        if (this.active) {
            batch.draw(region, this.position.x, this.position.y);
        }
    }
}
