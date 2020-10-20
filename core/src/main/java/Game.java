//package com.gitlab.project.core;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.audio.Music;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

/**
 * The game class create a whole game for user to play, all the UI stuff and
 * sound stuff build here. Also the game will change the gamestae depend on
 * user's operation, when user make operation, the game will receive it through
 * listener in menu, then change the state so different screens will shows to
 * user.
 * 
 */

public class Game implements ApplicationListener {

	/**
	 * the enum gamestate is the state we define firstly.
	 */

	public enum GameState {
		START, PAUSE, RUN, LOSE, WIN
	}

	private GameState state = GameState.START;
	SpriteBatch batch;
	static float elapsed;
	Map currentMap;
	Menu startMenu;
	Menu pauseMenu;
	BitmapFont font;
	Texture background;
	Texture win;
	Texture gameover;
	private Music music;
	private int level = 0;
	public static boolean debug = false;
	private float debugTime = 0;
	private ShapeRenderer shapeRenderer;

	/**
	 * First of all setting the background music play by looping. Then create the
	 * new batch, setting the background picture, creating win and gameover image.
	 * Finally, build the whole map fro game and set the size properly.
	 */
	@Override
	public void create() {
		shapeRenderer = new ShapeRenderer();

		music = Gdx.audio.newMusic(Gdx.files.internal("bgm.mp3"));
		music.setLooping(true);
		music.play();
		batch = new SpriteBatch();
		background = new Texture(Gdx.files.internal("background.png"));
		win = new Texture(Gdx.files.internal("win.png"));
		gameover = new Texture(Gdx.files.internal("gameover.png"));

		currentMap = new Map("0" + level);
		startMenu = new Menu(Gdx.files.internal("buttons/start.png"), Gdx.files.internal("buttons/start_pressed.png"),
				Gdx.graphics.getWidth() / 2 - 253 / 2, Gdx.graphics.getHeight() / 2 - 81 / 2);
	}

	@Override
	public void resize(int width, int height) {
	}

	/**
	 * In START, there is the detail code about when gamestate change under specific
	 * condition, when the user paly the game the gamestate is setted to START
	 * originally, then in START state, draw the background, start menu. If the
	 * start button was press, then the screen need to clean it all to build the
	 * map. In RUN, set the win condition firstly, make sure the screen is clean,
	 * then render the map and the score. Next, when user press the key p, the
	 * gamestate will become to pause, meanwhile the pause menu also need to be
	 * build, set the lose condition at the end. In pause, of couse the pasue menu
	 * need to be draw, then if user press the pause button, gamestate need to back
	 * to run state In lose and win should be same just draw the win and lose
	 * screen.
	 */
	@Override
	public void render() {

		switch (state) {
			case START:
				batch.begin();
				batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
				batch.end();
				batch.begin();
				startMenu.render();
				batch.end();

				if (startMenu.isButtonPressed()) {
					state = GameState.RUN;
					Gdx.gl.glClearColor(0, 0, 0, 0);
					Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

				}

				break;

			case RUN:

				elapsed += Gdx.graphics.getDeltaTime();
				Gdx.gl.glClearColor(0, 0, 0, 0);
				Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

				batch.begin();
				// rendering map
				currentMap.update();
				currentMap.render(batch);
				// rendering time
				font = new BitmapFont();
				font.setScale(2);
				font.draw(batch, "Time: " + String.valueOf((int) elapsed), Gdx.graphics.getWidth() - 200,
						Gdx.graphics.getHeight() - 6);

				batch.end();

				// if paused
				if (Gdx.input.isKeyPressed(Input.Keys.P)) {
					state = GameState.PAUSE;

					pauseMenu = new Menu(Gdx.files.internal("buttons/pause.png"),
							Gdx.files.internal("buttons/pause_pressed.png"), Gdx.graphics.getWidth() / 2 - 256 / 2,
							Gdx.graphics.getHeight() / 2 - 126 / 2);
				}

				// Checking if the players health is below 0
				if (currentMap.player.currentHealth <= 0) {
					state = GameState.LOSE;
				}

				// Checking if need to change maps
				if (currentMap.levelComplete) {
					level++;
					try {
						currentMap = new Map("0" + level);
					} catch (Exception e) {
						state = GameState.WIN;
					}
				}

				// debugging
				if (Gdx.input.isKeyPressed(Input.Keys.G) && 0.5f < elapsed - debugTime) {
					debug = !debug;
					debugTime = elapsed;
				}

				// the following draws lines for debugging when debug mode is enabled
				if (debug) {
					shapeRenderer.begin(ShapeType.Line);
					shapeRenderer.setColor(1, 0, 0, 1); // Red line
					Vector2 playerPosition = currentMap.player.getCenter();
					Vector2 mousePosition = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
					shapeRenderer.line(playerPosition, mousePosition);
					drawArrow(shapeRenderer, playerPosition, currentMap.player.direction);
					for (Enemy enemy : currentMap.enemies) {
						if (enemy.active) {
							drawArrow(shapeRenderer, enemy.getCenter(), enemy.direction);
						}
					}
					shapeRenderer.end();
				}

				break;

			case PAUSE:
				// pause menu
				batch.begin();
				pauseMenu.render();
				batch.end();
				if (pauseMenu.isButtonPressed()) {
					state = GameState.RUN;
				}

				break;

			case LOSE:
				batch.begin();
				batch.draw(gameover, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
				batch.end();
				break;

			case WIN:
				batch.begin();
				batch.draw(win, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
				font.draw(batch, "Time Completed: " + String.valueOf(elapsed), Gdx.graphics.getWidth() / 2 - 100,
						Gdx.graphics.getHeight() / 2 - 100);
				batch.end();
				break;

			default:
				break;
		}
	}

	private void drawArrow(ShapeRenderer shapeRenderer, Vector2 position, Vector2 direction) {
		Vector2 tip = position.cpy().add(direction.cpy().scl(48));
		Vector2 rightTip = tip.cpy().sub(direction.cpy().rotate(30).scl(12));
		Vector2 leftTip = tip.cpy().sub(direction.cpy().rotate(-30).scl(12));

		shapeRenderer.line(position, tip);
		shapeRenderer.triangle(rightTip.x, rightTip.y, leftTip.x, leftTip.y, tip.x, tip.y);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
	}
}
