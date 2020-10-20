
import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.mockito.Mockito;


import static org.mockito.Mockito.when;


public class GdxTest {
    // test app
    public static Application application;


    // Before running any tests, initialize the application with the headless backend
    @BeforeAll
    public static void init() {

        // Note that we don't need to implement any of the listener's methods
        application = new HeadlessApplication(
                new ApplicationListener() {
                    @Override public void create() {}

                    @Override public void resize(int width, int height) {}

                    @Override public void render() {}

                    @Override public void pause() {}

                    @Override public void resume() {}

                    @Override public void dispose() {}
                });

        // Use Mockito to mock the OpenGL methods since we are running headlessly
        Gdx.gl20 = Mockito.mock(GL20.class);
        Gdx.gl = Gdx.gl20;

        // Mock the graphics class.
        Gdx.graphics = Mockito.mock(Graphics.class);
        when(Gdx.graphics.getWidth()).thenReturn(1000);
        when(Gdx.graphics.getHeight()).thenReturn(1000);
    }

    // After we are done, clean up the application
    @AfterAll
    public static void cleanUp() {
        // Exit the application first
        application.exit();
        application = null;
    }
}