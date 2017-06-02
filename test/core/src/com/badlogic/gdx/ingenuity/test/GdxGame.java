package com.badlogic.gdx.ingenuity.test;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.ingenuity.GdxData;
import com.badlogic.gdx.ingenuity.screen.SimpleLoadingScreen.ILoadingComplete;
import com.badlogic.gdx.ingenuity.test.screen.LoadingScreen;
import com.badlogic.gdx.ingenuity.test.screen.LoginScreen;
import com.badlogic.gdx.ingenuity.utils.helper.PixmapHelper;
import com.badlogic.gdx.ingenuity.utils.helper.RHelper;
import com.badlogic.gdx.ingenuity.utils.scene2d.DebugMonitor;
import com.badlogic.gdx.ingenuity.utils.scene2d.SimpleScreen;

public class GdxGame extends Game {

	private DebugMonitor debugMonitor;

	private SimpleScreen chessScreen;

	@Override
	public void create() {
		this.debugMonitor = new DebugMonitor();
		Texture.setAssetManager(SimpleScreen.assetManager().getManager());

		if (Gdx.app.getType() == ApplicationType.Desktop) {
			RHelper.generated(getClass());
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

		SpriteBatch spriteBatch = SimpleScreen.spriteBatch();
		if (spriteBatch != null) {
			spriteBatch.begin();
			debugMonitor.draw(spriteBatch, 1);
			debugMonitor.act(Gdx.graphics.getDeltaTime());
			spriteBatch.end();
		}
	}

	@Override
	public void dispose() {
		super.dispose();
		chessScreen.dispose();
		SimpleScreen.disposeStatic();
		GdxData.getInstance().disposeFont();
		PixmapHelper.getInstance().dispose();
	}

	public void updateScreen(SimpleScreen simpleScreen) {
		if (chessScreen != null) {
			chessScreen.dispose();
			chessScreen = null;
		}
		setScreen(chessScreen = simpleScreen);
	}

}
