package com.badlogic.gdx.ingenuity;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.ingenuity.extend.desktop.DesktopCoreHelper;
import com.badlogic.gdx.ingenuity.utils.helper.DebugHelper;
import com.badlogic.gdx.ingenuity.utils.helper.PixmapHelper;
import com.badlogic.gdx.ingenuity.utils.helper.RHelper;
import com.badlogic.gdx.ingenuity.utils.helper.ScreenShotsHelper;
import com.badlogic.gdx.ingenuity.utils.helper.WidgetHelper;
import com.badlogic.gdx.ingenuity.utils.scene2d.SimpleScreen;

/**
 * @作者 mitkey
 * @时间 2017年6月9日 下午5:40:11
 * @类说明 GdxGame.java <br/>
 * @版本 0.0.1
 */
public abstract class GdxGame extends Game {

	private SimpleScreen currentScreen;

	@Override
	public final void create() {
		if (Gdx.app.getType() == ApplicationType.Desktop) {
			GdxData.getInstance().initRuntime(new DesktopCoreHelper());
		}

		SimpleScreen.initContext();
		Texture.setAssetManager(SimpleScreen.onlyAssetManager().getManager());
		if (Gdx.app.getType() == ApplicationType.Desktop) {
			RHelper.generated(getClass());
		}

		onCreate();
	}

	@Override
	public final void dispose() {
		super.dispose();
		if (currentScreen != null) {
			currentScreen.dispose();
			currentScreen = null;
		}
		SimpleScreen.disposeStatic();
		GdxData.getInstance().disposeFont();
		PixmapHelper.getInstance().dispose();

		onDispose();
	}

	@Override
	public final void render() {
		super.render();

		// 截屏
		if (Gdx.input.isKeyJustPressed(Keys.F10)) {
			ScreenShotsHelper.saveScreenShot();
		}

		// 显示 debug 监控指标消息
		if (Gdx.input.isKeyJustPressed(Keys.F11)) {
			DebugHelper.showDebugOnOff();
		}
		DebugHelper.renderDebug();

		// 显示 widget 调试的帮助消息
		if (Gdx.input.isKeyJustPressed(Keys.F12)) {
			WidgetHelper.showHelpOnOff();
		}
		WidgetHelper.renderHelp();

		onRender();
	}

	public abstract void onCreate();

	public abstract void onDispose();

	public abstract void onRender();

	public final void updateScreen(SimpleScreen simpleScreen) {
		if (currentScreen != null) {
			currentScreen.dispose();
			currentScreen = null;
		}
		setScreen(currentScreen = simpleScreen);
	}

}
