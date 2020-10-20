//package com.gitlab.project.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

//import static Game.elapsed;
//import static Map.addBullet;

/**
 * The player class. Handles all movement and player state
 */
public class Player extends Entity {

    public Vector2 direction = new Vector2(1, 0);

    private Animation runAnimation, idleAnimation, shootAnimation, hurtAnimation;
    private TextureRegion region;

    private float shootTime;
    private float hurtTime = 0;

    public float speed = 175;

    private Sound sound;

    private float reloadTime = 0;
    private Round[] rounds;
    private int maxRounds = 6;

    public int currentRounds = maxRounds;

    private Health[] health;
    private int maxHealth = 10;

    public int currentHealth = maxHealth;

    /**
     * The main constructor for creating a player.
     * 
     * @param position The position of where the player will respawn
     */
    public Player(Vector2 position) {
        super(position, 0, 0);

        sound = Gdx.audio.newSound(Gdx.files.internal("sound.wav"));

        TextureAtlas atlas = new TextureAtlas("policeman/policeman.txt");

        runAnimation = new Animation(0.1f, atlas.findRegions("right"), Animation.PlayMode.LOOP);
        idleAnimation = new Animation(0.2f, atlas.findRegions("idle"), Animation.PlayMode.LOOP);
        shootAnimation = new Animation(0.1f, atlas.findRegions("shoot"), Animation.PlayMode.LOOP);
        hurtAnimation = new Animation(0.2f, atlas.findRegions("hurt"), Animation.PlayMode.LOOP);

        region = idleAnimation.getKeyFrame(Game.elapsed, true);

        width = region.getRegionWidth();
        height = region.getRegionHeight();

        // rounds gui
        rounds = new Round[maxRounds];

        for (int i = 0; i < rounds.length; i++) {
            rounds[i] = new Round(new Vector2(10, i * 25 + 15));
        }

        // health gui
        health = new Health[maxHealth];

        for (int i = 0; i < health.length; i++) {
            health[i] = new Health(new Vector2(734, i * 15 + 15));
        }
    }

    public boolean allRoundsExplosive() {
        boolean explosive = true;
        for (Round round : rounds) {
            if (!round.isExplosive)
                explosive = false;
        }
        return explosive;
    }

    /**
     * Removes a health point and reflects changes in user interface
     */
    public void hurt() {
        if (currentHealth <= 1) {
            currentHealth = 0;
            health[currentHealth].hurt();

        } else {
            currentHealth -= 1;
            health[currentHealth].hurt();
        }

        hurtTime += Gdx.graphics.getDeltaTime();

    }

    /**
     * @see #hurt()
     * 
     * @param healthLoss the number of times to call it
     */
    public void hurt(int healthLoss) {
        for (int i = 0; i < healthLoss; i++) {
            hurt();
        }
    }

    /**
     * Heals player
     */
    public void heal() {
        if (currentHealth < maxHealth) {
            health[currentHealth].heal();
            currentHealth++;
        }
    }

    /**
     * @see #heal()
     * @param healthGain The number of times to heal the player
     */
    public void heal(int healthGain) {
        for (int i = 0; i < healthGain; i++) {
            heal();
        }
    }

    /**
     * gives the player explosive rounds
     */
    public void giveExplosiveRounds() {
        for (int i = 0; i < rounds.length; i++) {
            rounds[i].reload(true);
        }

        currentRounds = rounds.length;
    }

    @Override
    public void update() {
        region = idleAnimation.getKeyFrame(Game.elapsed, true);
        boolean isMoving = false;

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            isMoving = true;
            direction = new Vector2(0, 1);
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            isMoving = true;
            direction = new Vector2(0, -1);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            if (isMoving) {
                direction.x = 1;
                direction.nor();
            } else {
                isMoving = true;
                direction = new Vector2(1, 0);
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            if (isMoving) {
                direction.x = -1;
                direction.nor();
            } else {
                isMoving = true;
                direction = new Vector2(-1, 0);
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.R)) {
            reloadTime = 0.9f + currentRounds * 0.2f;
        }

        // moving
        if (hurtTime == 0) {
            if (isMoving) {
                region = runAnimation.getKeyFrame(Game.elapsed, true);
                position.add(direction.cpy().scl(speed * Gdx.graphics.getDeltaTime()));
            }
        } else {
            region = hurtAnimation.getKeyFrame(hurtTime, false);
            hurtTime += Gdx.graphics.getDeltaTime();

            if (hurtAnimation.isAnimationFinished(hurtTime)) {
                hurtTime = 0;
            }
        }

        // shooting
        if (reloadTime == 0 && shootTime == 0) {
            if (Gdx.input.justTouched()) {
                shoot();
            }
        }

        // reloading
        if (reloadTime != 0) {
            reloadTime += Gdx.graphics.getDeltaTime();

            for (int i = 0; i < rounds.length; i += 1) {
                if (i * 0.2 + 1 < reloadTime) {
                    rounds[i].ejected = false;
                }
            }

            if (rounds[rounds.length - 1].ejected == false) {
                reloadTime = 0;
                currentRounds = rounds.length;
            }
        }

        for (Round round : rounds) {
            round.update();
        }

        for (Health health : health) {
            health.update();
        }

        if (shootTime != 0) {
            region = shootAnimation.getKeyFrame(shootTime, false);
            shootTime += Gdx.graphics.getDeltaTime();

            if (shootAnimation.isAnimationFinished(shootTime)) {
                shootTime = 0;
            }
        }

        boolean isRight = direction.angle() <= 90 || 270 <= direction.angle();

        if ((isRight && region.isFlipX()) || (!isRight && !region.isFlipX())) {
            region.flip(true, false);
        }

    }

    /**
     * Starts the shoot animation and places a bullet at current position
     */
    public void shoot() {
        sound.play();

        if (reloadTime == 0) {
            currentRounds -= 1;

            Vector2 clickPosition = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
            Vector2 shootDirection = clickPosition.cpy().sub(getCenter()).nor();
            Map.addBullet(new Bullet(getCenter(), shootDirection, rounds[currentRounds].isExplosive));

            region = shootAnimation.getKeyFrame(shootTime, false);

            direction = shootDirection;
            shootTime += Gdx.graphics.getDeltaTime();

            rounds[currentRounds].eject();

            if (currentRounds == 0) {
                reloadTime += Gdx.graphics.getDeltaTime();
            }
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        if (active) {
            batch.draw(region, position.x, position.y);
        }

        for (int i = rounds.length - 1; 0 <= i; i--) {
            rounds[i].render(batch);
        }

        for (int i = health.length - 1; 0 <= i; i--) {
            health[i].render(batch);
        }
    }
}

/**
 * This is from the UI element that shows how many bullets you have. It is
 * strictly visual. Six of these are rendered.
 */
class Round extends Entity {
    private Texture roundTexture = new Texture(Gdx.files.internal("bullet/round.png"));
    private Texture shadowTexture = new Texture(Gdx.files.internal("bullet/roundShadow.png"));
    private Texture casingTexture = new Texture(Gdx.files.internal("bullet/casing.png"));
    private Texture explosiveRoundTexture = new Texture(Gdx.files.internal("bullet/explosiveRound.png"));

    private Vector2 ejectPosition;
    private float angle = 0;
    public boolean isExplosive = false;

    private Vector2 velocity = new Vector2(100, 500);

    public boolean ejected = false;

    /**
     * 
     * @param position
     */
    Round(Vector2 position) {
        this.position = position;
        ejectPosition = position.cpy();
    }

    /**
     * Eject the round
     */
    void eject() {
        ejected = true;
        isExplosive = false;
    }

    /**
     * Reload non explosive round
     */
    void reload() {
        reload(false);
    }

    /**
     * Reload round
     * 
     * @param isExplosive
     */
    void reload(boolean isExplosive) {
        ejected = false;
        this.isExplosive = isExplosive;
    }

    @Override
    public void update() {
        if (ejected) {
            ejectPosition.add(velocity.cpy().scl(Gdx.graphics.getDeltaTime()));
            velocity.y -= 2000 * Gdx.graphics.getDeltaTime();
            angle += 500 * Gdx.graphics.getDeltaTime();
        } else {
            ejectPosition = position.cpy();
            velocity = new Vector2(100, 500);
            angle = 0;
        }

    }

    @Override
    public void render(SpriteBatch batch) {
        if (ejected) {
            batch.draw(shadowTexture, position.x, position.y);
            batch.draw(casingTexture, ejectPosition.x, ejectPosition.y, casingTexture.getWidth() / 2,
                    casingTexture.getHeight() / 2, casingTexture.getWidth(), casingTexture.getHeight(), 1, 1, angle, 0,
                    0, casingTexture.getWidth(), casingTexture.getHeight(), false, false);
        } else {
            if (isExplosive) {
                batch.draw(explosiveRoundTexture, position.x, position.y);
            } else {
                batch.draw(roundTexture, position.x, position.y);
            }
        }
    }
}

/**
 * This is a health bar from the UI. Ten of them are rendered.
 */
class Health extends Entity {

    private Texture healthTexture = new Texture(Gdx.files.internal("health/health.png"));
    private Texture shadowTexture = new Texture(Gdx.files.internal("health/shadow.png"));

    private Animation bloodAnimation;
    private TextureRegion region;

    public float hurtTime = 0;

    /**
     * Creates a new health bar
     * 
     * @param position The position of where to put the health bar
     */
    Health(Vector2 position) {
        this.position = position;

        TextureAtlas atlas = new TextureAtlas("health/blood.txt");
        bloodAnimation = new Animation(0.05f, atlas.findRegions("blood"), Animation.PlayMode.NORMAL);
        region = bloodAnimation.getKeyFrame(hurtTime, false);
    }

    /**
     * Removes a health
     */
    public void hurt() {
        hurtTime += Gdx.graphics.getDeltaTime();
    }

    /**
     * Adds a health
     */
    public void heal() {
        hurtTime = 0;
    }

    @Override
    public void update() {
        if (hurtTime != 0) {
            region = bloodAnimation.getKeyFrame(hurtTime, false);
            hurtTime += Gdx.graphics.getDeltaTime();
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        if (hurtTime == 0) {
            batch.draw(healthTexture, position.x, position.y);
        } else if (!bloodAnimation.isAnimationFinished(hurtTime)) {
            batch.draw(shadowTexture, position.x, position.y);
            batch.draw(region, position.x - 36, position.y - 20);
        } else {
            batch.draw(shadowTexture, position.x, position.y);
        }
    }
}
