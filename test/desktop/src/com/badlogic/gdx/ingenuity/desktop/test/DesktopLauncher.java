package com.badlogic.gdx.ingenuity.desktop.test;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.ingenuity.GdxData;
import com.badlogic.gdx.ingenuity.extend.DesktopCoreHelper;
import com.badlogic.gdx.ingenuity.test.GdxGame;
import com.badlogic.gdx.ingenuity.utils.FnAssetManager;

public class DesktopLauncher {

	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;
	private static final float SCALE = 1.02f;

	public static void main(String[] arg) {
		// 初始化运行环境
		GdxData.getInstance().initRuntime(new DesktopCoreHelper());

		FnAssetManager.enableAssetMonitorLog = true;// 桌面版开启资源监控日志
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = Math.round(WIDTH / SCALE);
		config.height = Math.round(HEIGHT / SCALE);
		config.y = 0;
		config.resizable = false;
		config.vSyncEnabled = true;// 垂直同步
		config.samples = 8;// 抗锯齿
		new LwjglApplication(new GdxGame(), config);
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
	}
}
