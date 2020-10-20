//package com.gitlab.project.core;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * An abstract class that is inherited by anything with a position -- nearly all
 * game objects
 */
public abstract class Entity {

    public Vector2 position;
    public int width;
    public int height;
    public boolean active = true;

    /**
     * Creates an entity at a position
     * 
     * @param position The position of the new entity
     * @param width    The width
     * @param height   The height
     */
    public Entity(Vector2 position, int width, int height) {
        this.position = position;
        this.width = width;
        this.height = height;
    }

    /**
     * The default constructor that creates an inactive entity
     */
    public Entity() {
        position = new Vector2(0, 0);
        width = 0;
        height = 0;
        active = false;
    }

    /**
     * Tests for collision between this entity and another
     * 
     * @param entity The entity to collide with
     * @return returns whether or not it collided
     */
    public boolean collide(Entity entity) {
        if (getRectangle().overlaps(entity.getRectangle()) && active && entity.active)
            return true;
        return false;
    }

    /**
     * Gets a Rectangle from the LibGDX API
     * 
     * @return the collision rectangle of the object
     */
    public Rectangle getRectangle() {
        return new Rectangle(position.x, position.y, width, height);
    }

    /**
     * Gets the center position of the entity
     * 
     * @return The position of the center
     */
    public Vector2 getCenter() {
        return new Vector2(position.x + width / 2, position.y + height / 2);
    }

    /**
     * An abstract method that is defined by the children to handle the updating of
     * entity state.
     */
    public abstract void update();

    /**
     * An abstract method for rendering the entity. This method is mean to be as
     * stateless as possible. Anything that has to do with rendering but changes
     * state (such as animation) should be put in the update method.
     * 
     * @param batch The batch for the current render
     */
    public abstract void render(SpriteBatch batch);
}
