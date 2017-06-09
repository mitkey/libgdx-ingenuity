package com.badlogic.gdx.ingenuity.test;

import com.badlogic.gdx.ingenuity.GdxGame;
import com.badlogic.gdx.ingenuity.screen.SimpleLoadingScreen.ILoadingComplete;
import com.badlogic.gdx.ingenuity.test.screen.LoadingScreen;
import com.badlogic.gdx.ingenuity.test.screen.LoginScreen;

public class TestGdxGame extends GdxGame {

	@Override
	public void onCreate() {
		// 默认启动欢迎界面
		updateScreen(new LoadingScreen(new ILoadingComplete() {
			@Override
			public boolean complete() {
				updateScreen(new LoginScreen());
				return true;
			}
		}, Asset.common));
	}

	@Override
	public void onDispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRender() {
		// TODO Auto-generated method stub

	}

}
