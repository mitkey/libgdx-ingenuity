package com.badlogic.gdx.ingenuity;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.ingenuity.screen.HallScreen;
import com.badlogic.gdx.ingenuity.screen.LoadingScreen;
import com.badlogic.gdx.ingenuity.screen.LoadingScreen.AssetsCategory;
import com.badlogic.gdx.ingenuity.screen.LoadingScreen.ILoadingComplete;
import com.badlogic.gdx.ingenuity.screen.LoginScreen;
import com.badlogic.gdx.ingenuity.screen.RoomScreen;
import com.badlogic.gdx.ingenuity.utils.FnAssetManager;
import com.badlogic.gdx.ingenuity.utils.LazyBitmapFont;
import com.badlogic.gdx.ingenuity.utils.helper.PixmapHelper;
import com.badlogic.gdx.ingenuity.utils.helper.RHelper;
import com.badlogic.gdx.ingenuity.utils.scene2d.DebugMonitor;
import com.badlogic.gdx.ingenuity.utils.scene2d.SimpleScreen;

public class GdxGame extends Game {

	private FreeTypeFontGenerator fontGenerator;

	private FnAssetManager assetManager;

	private SpriteBatch spriteBatch;

	private DebugMonitor debugMonitor;

	private SimpleScreen chessScreen;

	@Override
	public void create() {
		LazyBitmapFont.setGlobalGenerator(fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf")));
		this.spriteBatch = new SpriteBatch();
		this.assetManager = new FnAssetManager();
		this.debugMonitor = new DebugMonitor();

		if (Gdx.app.getType() == ApplicationType.Desktop && GdxData.AUTO_GENERATE_GDX_R) {
			RHelper.generated();
		}

		// 默认启动欢迎界面
		loading2Welcome();
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
		fontGenerator.dispose();
		assetManager.dispose();
		chessScreen.dispose();
		debugMonitor.dispose();
		spriteBatch.dispose();
		PixmapHelper.getInstance().dispose();
	}

	public SpriteBatch getSpriteBatch() {
		return spriteBatch;
	}

	public FnAssetManager getAssetManager() {
		return assetManager;
	}

	public void loading2Hall() {
		updateScreen(new LoadingScreen(AssetsCategory.hall, new ILoadingComplete() {
			@Override
			public boolean complete() {
				// 加载完资源后，切换到大厅场景
				updateScreen(new HallScreen());
				return true;
			}
		}));
	}

	public void loading2Login() {
		updateScreen(new LoadingScreen(AssetsCategory.login, new ILoadingComplete() {
			@Override
			public boolean complete() {
				// 加载完资源后，切换到登录场景
				updateScreen(new LoginScreen());
				return true;
			}
		}));
	}

	public void loading2Room() {
		updateScreen(new LoadingScreen(AssetsCategory.room, new ILoadingComplete() {
			@Override
			public boolean complete() {
				// 加载完资源后，切换到房间场景
				updateScreen(new RoomScreen());
				return true;
			}
		}));
	}

	public void loading2Welcome() {
		updateScreen(new LoadingScreen(AssetsCategory.common, new ILoadingComplete() {
			@Override
			public boolean complete() {
				// 加载完资源后，切换到登录场景
				updateScreen(new LoginScreen());
				return true;
			}
		}));
	}

	private void updateScreen(SimpleScreen simpleScreen) {
		if (chessScreen != null) {
			chessScreen.dispose();
			chessScreen = null;
		}
		setScreen(chessScreen = simpleScreen);
	}

}
