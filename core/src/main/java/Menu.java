//package com.gitlab.project.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;

/**
 * The menu class create a menu for user to change the gamestate. The menu will
 * show the pressable button on the screen for user to go next step.
 */
public class Menu {

    private Stage stage = new Stage(new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
    // set new stage include the width and height
    private Texture buttonTexture, buttonPressedTexture;
    // this shows the image change when user presse the button
    private ImageButton imageButton;
    // this is a image button directly
    private boolean buttonPressed = false;

    //
    /**
     * The menu class create a menu for user to change the gamestate.
     * 
     * @param buttonPath contains a directory which go in the assets and point to a
     *                   png file, this is the image that imagebutton shows
     *                   {@link FileHandle}.
     * @param buttonPath contains a directory which go in the assets and point to a
     *                   png file, this is the image that imagebutton shows
     *                   {@link FileHandle}.
     * @param x          is the width position of button.
     * @param y          is the height position of button.
     */
    public Menu(FileHandle buttonPath, FileHandle buttonPressedPath, int x, int y) {
        Gdx.input.setInputProcessor(stage);
        buttonTexture = new Texture(buttonPath);
        buttonPressedTexture = new Texture(buttonPressedPath);

        imageButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(buttonTexture)),
                new TextureRegionDrawable(new TextureRegion(buttonPressedTexture)));

        imageButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // Pressed Image Button
                buttonPressed = true;
                return true;
            }
        });

        imageButton.setPosition(x, y);
        stage.addActor(imageButton);
    }

    /**
     * 
     * @return if the button was pressed
     */
    public boolean isButtonPressed() {
        return buttonPressed;
    }

    public void render() {
        stage.act();
        stage.draw();
    }

}
