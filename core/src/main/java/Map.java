//package com.gitlab.project.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Renders the entire map and maintains its state
 */
public class Map {
    public boolean levelComplete = false;

    private String level;
    private int rewardsLeft = 0;

    public Player player;

    private Tile[][] tiles = new Tile[16][16];
    private Tile endTile;

    // for pathfinding
    private ArrayList<Tile> passableTiles = new ArrayList<Tile>();

    static private Bullet[] bullets = new Bullet[16];
    static public Enemy[] enemies = new Enemy[16];

    /**
     * Creates a new map
     * 
     * @param level the level number correspoding to what can be found in
     *              assets/maps
     */
    public Map(String level) {
        this.level = level;
        Arrays.fill(bullets, new Bullet());
        Arrays.fill(enemies, new Enemy());

        player = new Player(new Vector2(0, 0));

        initializeMap();

        for (Enemy enemy : enemies) {
            if (enemy.active) {
                enemy.setTarget(getTile(11, 4));
            }
        }

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                if (tiles[i][j].collidable == false) {
                    passableTiles.add(tiles[i][j]);
                }
            }
        }

    }

    /**
     * Reads the map file and fills the {@link #tiles} array
     */
    private void initializeMap() {
        Scanner scanner = new Scanner(Gdx.files.internal("maps/" + level + ".txt").readString());

        for (int row = 0; row < 16; row++) {
            for (int col = 0; col < 16; col++) {
                String name = scanner.next();

                if (name.equals("bonus")) {
                    tiles[row][col] = new Bonus(row, col);
                } else if (name.equals("reward")) {
                    tiles[row][col] = new Reward(row, col);
                    rewardsLeft++;
                } else if (name.equals("punishment")) {
                    tiles[row][col] = new Punishment(row, col);
                } else if (name.equals("food")) {
                    tiles[row][col] = new Food(row, col);
                } else if (name.equals("enemy")) {
                    tiles[row][col] = new Tile("floor/00.png", false, row, col);
                    addEnemy(new Enemy(tiles[row][col].position.cpy(), 0));
                } else if (name.equals("enemy1")) {
                    tiles[row][col] = new Tile("floor/00.png", false, row, col);
                    addEnemy(new Enemy(tiles[row][col].position.cpy(), 1));
                } else if (name.equals("player")) {
                    tiles[row][col] = new Tile("floor/00.png", false, row, col);
                    player.position = tiles[row][col].position.cpy();
                } else if (name.equals("end")) {
                    tiles[row][col] = new Tile("floor/00.png", false, row, col);
                    endTile = tiles[row][col];
                } else if (name.substring(0, 5).equals("floor")) {
                    tiles[row][col] = new Tile("floor/00.png", false, row, col);
                } else {
                    tiles[row][col] = new Tile(name + ".png", true, row, col);
                }
            }
        }
    }

    /**
     * Sets the distance from every passable tile. Uses Bellman-Ford Algorithm
     * 
     * @param target The target tile
     */
    private void setCosts(Tile target) {
        for (Tile tile : passableTiles) {
            tile.cost = 99;
        }

        target.cost = 0;

        boolean hasChanged = true;

        while (hasChanged) {
            hasChanged = false;

            for (Tile tile : passableTiles) {
                if (setCost(tile, getNorth(tile))) {
                    hasChanged = true;
                }
                if (setCost(tile, getSouth(tile))) {
                    hasChanged = true;
                }
                if (setCost(tile, getEast(tile))) {
                    hasChanged = true;
                }
                if (setCost(tile, getWest(tile))) {
                    hasChanged = true;
                }
            }
        }
    }

    /**
     * A healper function for the path finding algorithm. Sets the cost of tile to
     * be the lowest of the two.
     * 
     * @param tile      The tile to set the cost for
     * @param neighbour The neighbouring tile
     * @return If the cost was updates
     */
    private boolean setCost(Tile tile, Tile neighbour) {
        if (neighbour.cost + 1 < tile.cost) {
            tile.cost = neighbour.cost + 1;
            return true;
        }

        return false;
    }

    public Tile getCheapestTile(Tile... tiles) {
        Tile cheapestTile = tiles[0];

        for (Tile tile : tiles) {
            if (tile.cost < cheapestTile.cost) {
                cheapestTile = tile;
            }
        }

        return cheapestTile;
    }

    /**
     * Add a new active bullet to {@link #bullets}
     * 
     * @param bullet The bullet to add
     */
    public static void addBullet(Bullet bullet) {
        for (int i = 0; i < bullets.length; i++) {
            if (!bullets[i].active) {
                bullets[i] = bullet;
                break;
            }
        }
    }

    /**
     * Add a new active enemy to {@link #enemies}
     * 
     * @param enemy The enemy to add
     */
    public static void addEnemy(Enemy enemy) {
        for (int i = 0; i < enemies.length; i++) {
            if (!enemies[i].active) {
                enemies[i] = enemy;
                break;
            }
        }
    }

    /**
     * Basically the same as indexing {@link #tiles} but does bound checks
     * 
     * @param row The row starting from zero and counting from top to bottom
     * @param col The column starting from zero and counting from left to right
     * @return The tile
     */
    public Tile getTile(int row, int col) {
        if (col < 0)
            col = 0;
        else if (15 < col)
            col = 15;
        if (row < 0)
            row = 0;
        else if (15 < row)
            row = 15;

        return tiles[row][col];
    }

    /**
     * Get tile at the position
     * 
     * @see #getTile(int, int)
     * @param position The postion of the tile
     * @return The tile at the position
     */
    public Tile getTile(Vector2 position) {
        int row = 15 - (int) (position.y / 48);
        int col = (int) (position.x / 48);

        return getTile(row, col);
    }

    /**
     * The tile to the north of the specified tile
     * 
     * @param tile The base tile to check from
     * @return The tile to the north
     */
    public Tile getNorth(Tile tile) {
        return getTile(tile.row - 1, tile.col);
    }

    /**
     * The tile to the south of the specified tile
     * 
     * @param tile The base tile to check from
     * @return The tile to the south
     */
    public Tile getSouth(Tile tile) {
        return getTile(tile.row + 1, tile.col);
    }

    /**
     * The tile to the west of the specified tile
     * 
     * @param tile The base tile to check from
     * @return The tile to the west
     */
    public Tile getWest(Tile tile) {
        return getTile(tile.row, tile.col - 1);
    }

    /**
     * The tile to the east of the specified tile
     * 
     * @param tile The base tile to check from
     * @return The tile to the east
     */
    public Tile getEast(Tile tile) {
        return getTile(tile.row, tile.col + 1);
    }

    /**
     * Checks collision between the map and an entity. Used mostly for movement
     * 
     * @param entity The entity to collide with the map
     * @return whether or not there was a collision
     */
    public boolean collide(Entity entity) {
        return getTile(entity.position).collidable || getTile(entity.position.cpy().add(0, entity.height)).collidable
                || getTile(entity.position.cpy().add(entity.width, 0)).collidable
                || getTile(entity.position.cpy().add(entity.width, entity.height)).collidable;
    }

    /**
     * Same as the update method from Entity
     * 
     * @see Entity#update()
     */
    public void update() {
        player.update();

        setCosts(getTile(player.getCenter()));

        // update tiles and checks if rewards have been collected
        int inactveRewards = 0;

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                tiles[i][j].update();

                if (tiles[i][j] instanceof Reward && !tiles[i][j].active) {
                    inactveRewards++;
                }
            }
        }

        if (inactveRewards == rewardsLeft && endTile.getRectangle().overlaps(player.getRectangle())) {
            levelComplete = true;
        }

        // Player interact with map
        getTile(player.position.cpy().add(24, 24)).interact(player);

        // Update all enemies
        for (Enemy enemy : enemies) {
            enemy.update();

            Tile tile = getTile(enemy.getCenter());

            if (enemy.attackTime == 0) {
                enemy.setTarget(getCheapestTile(getNorth(tile), getSouth(tile), getEast(tile), getWest(tile)));
            } else if (!enemy.didDamage && enemy.attackDuration / 2 < enemy.attackTime && enemy.collide(player)) {
                player.hurt(1);
                enemy.didDamage = true;
            }
        }

        // Update all bullets
        for (Bullet bullet : bullets) {
            bullet.update();

            if (bullet.active) {
                for (Enemy enemy : enemies) {
                    if (bullet.collide(enemy)) {
                        enemy.hit();
                        bullet.hit();
                    }
                }
                if (collide(bullet)) {
                    bullet.hit();
                }
            }
        }

        // Collide player with map
        if (collide(player)) {
            player.position.add(player.direction.cpy().scl(-player.speed * Gdx.graphics.getDeltaTime()));
        }
    }

    /**
     * Same as a render method from Entity
     * 
     * @param batch The batch to draw to
     * @see Entity#render(SpriteBatch)
     */
    public void render(SpriteBatch batch) {
        for (Tile[] tileRow : tiles) {
            for (Tile tile : tileRow) {
                tile.render(batch);
            }
        }

        player.render(batch);

        for (Enemy enemy : enemies) {
            enemy.render(batch);
        }

        for (Bullet bullet : bullets) {
            bullet.render(batch);
        }
    }

    public Tile returntile(int row, int col) {
        return tiles[row][col];
    }

}
