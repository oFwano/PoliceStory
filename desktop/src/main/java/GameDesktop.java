//package com.gitlab.project.java;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

//import com.gitlab.project.core.Game;

public class GameDesktop {
	public static void main (String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.height = 768;
        config.width = 768;
        config.resizable = false;
        new LwjglApplication(new Game(), config);
	}
}
