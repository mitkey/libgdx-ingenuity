package com.badlogic.gdx.ingenuity.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.ingenuity.IngenuityGdx;

public class DesktopLauncher {
	static int width = 1280;
	static int height = 720;
	static float scale = 1.02f;
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.y = 0;
		config.width = Math.round(width / scale);
		config.height = Math.round(height / scale);
		config.resizable = false;
		new LwjglApplication(new IngenuityGdx(), config);
	}
}
