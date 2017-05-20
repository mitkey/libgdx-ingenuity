package com.badlogic.gdx.ingenuity;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.ingenuity.screen.LoadingScreen;
import com.badlogic.gdx.ingenuity.screen.LoadingScreen.ILoadingComplete;
import com.badlogic.gdx.ingenuity.screen.LoginScreen;
import com.badlogic.gdx.ingenuity.utils.FnAssetManager;
import com.badlogic.gdx.ingenuity.utils.helper.PixmapHelper;
import com.badlogic.gdx.ingenuity.utils.helper.RHelper;
import com.badlogic.gdx.ingenuity.utils.scene2d.DebugMonitor;
import com.badlogic.gdx.ingenuity.utils.scene2d.SimpleScreen;

public class GdxGame extends Game {

	private FnAssetManager assetManager;

	private SpriteBatch spriteBatch;

	private DebugMonitor debugMonitor;

	private SimpleScreen chessScreen;

	@Override
	public void create() {
		this.spriteBatch = new SpriteBatch();
		this.assetManager = new FnAssetManager();
		this.debugMonitor = new DebugMonitor();
		Texture.setAssetManager(assetManager.getManager());

		if (Gdx.app.getType() == ApplicationType.Desktop) {
			RHelper.generated();
		}

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
	public void render() {
		super.render();

		spriteBatch.begin();
		debugMonitor.draw(spriteBatch, 1);
		debugMonitor.act(Gdx.graphics.getDeltaTime());
		spriteBatch.end();
	}

	@Override
	public void dispose() {
		super.dispose();
		assetManager.dispose();
		chessScreen.dispose();
		spriteBatch.dispose();
		GdxData.getInstance().disposeFont();
		PixmapHelper.getInstance().dispose();
	}

	public SpriteBatch getSpriteBatch() {
		return spriteBatch;
	}

	public FnAssetManager getAssetManager() {
		return assetManager;
	}

	public void updateScreen(SimpleScreen simpleScreen) {
		if (chessScreen != null) {
			chessScreen.dispose();
			chessScreen = null;
		}
		setScreen(chessScreen = simpleScreen);
	}

}
