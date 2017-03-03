package com.badlogic.gdx.ingenuity.desktop;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.ingenuity.IngenuityGdx;

public class DesktopLauncher {

	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;
	private static final float SCALE = 1.02f;

	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = Math.round(WIDTH / SCALE);
		config.height = Math.round(HEIGHT / SCALE);
		config.y = 0;
		config.resizable = false;
		config.vSyncEnabled = true;// 垂直同步
		config.samples = 8;// 抗锯齿
		new LwjglApplication(new IngenuityGdx(), config);
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
	}
}
