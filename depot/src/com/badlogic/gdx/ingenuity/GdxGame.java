package com.badlogic.gdx.ingenuity;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.ingenuity.extend.desktop.DesktopCoreHelper;
import com.badlogic.gdx.ingenuity.helper.DebugHelper;
import com.badlogic.gdx.ingenuity.helper.PixmapHelper;
import com.badlogic.gdx.ingenuity.helper.RHelper;
import com.badlogic.gdx.ingenuity.helper.ScreenShotsHelper;
import com.badlogic.gdx.ingenuity.helper.WidgetHelper;
import com.badlogic.gdx.ingenuity.scene2d.SimpleScreen;
import com.badlogic.gdx.ingenuity.utils.OnlyAssetManager;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import net.mwplay.nativefont.NativeFont;
import net.mwplay.nativefont.NativeFontPaint;

/**
 * @作者 mitkey
 * @时间 2017年6月9日 下午5:40:11
 * @类说明 GdxGame.java <br/>
 * @版本 0.0.1
 */
public abstract class GdxGame extends Game {

	/** 默认的字体 */
	private Map<Integer, NativeFont> defaultFonts = new HashMap<Integer, NativeFont>();

	private SimpleScreen currentScreen;

	private OnlyAssetManager onlyAssetManager;

	private SpriteBatch spriteBatch;

	@Override
	public final void create() {
		if (Gdx.app.getType() == ApplicationType.Desktop) {
			GdxData.getInstance().initRuntime(new DesktopCoreHelper());
			RHelper.generated(getClass());
		}

		onlyAssetManager = new OnlyAssetManager();
		spriteBatch = new SpriteBatch();

		Texture.setAssetManager(onlyAssetManager.getManager());

		onCreate();
	}

	@Override
	public final void dispose() {
		super.dispose();
		if (currentScreen != null) {
			currentScreen.dispose();
			currentScreen = null;
		}
		if (onlyAssetManager != null) {
			onlyAssetManager.dispose();
			onlyAssetManager = null;
		}
		if (spriteBatch != null) {
			spriteBatch.dispose();
			spriteBatch = null;
		}

		defaultFonts.values().forEach(new Consumer<Disposable>() {
			@Override
			public void accept(Disposable t) {
				t.dispose();
			}
		});
		defaultFonts.clear();

		PixmapHelper.getInstance().dispose();

		onDispose();
	}

	@Override
	public final void render() {
		super.render();

		// 开启当前所有的 Actor
		if (Gdx.input.isKeyJustPressed(Keys.F9) && currentScreen != null) {
			WidgetHelper.getInstance().clearInvalids();
			Array<Actor> actors = currentScreen.stage().getActors();
			for (Actor actor : actors) {
				WidgetHelper.getInstance().register(actor);
			}
		}

		// 截屏
		if (Gdx.input.isKeyJustPressed(Keys.F10)) {
			ScreenShotsHelper.saveScreenShot();
		}

		// 显示 debug 监控指标消息
		if (Gdx.input.isKeyJustPressed(Keys.F11)) {
			DebugHelper.getInstance().showDebugOnOff();
		}
		DebugHelper.getInstance().renderDebug();

		// 显示 widget 调试的帮助消息
		if (Gdx.input.isKeyJustPressed(Keys.F12)) {
			WidgetHelper.getInstance().showHelpOnOff();
		}
		WidgetHelper.getInstance().renderHelp();

		onRender();
	}

	public abstract void onCreate();

	public abstract void onDispose();

	public abstract void onRender();

	public final NativeFont getFont(int size) {
		if (defaultFonts.containsKey(size)) {
			return defaultFonts.get(size);
		} else {
			NativeFont nativeFont = new NativeFont(new NativeFontPaint(size));
			defaultFonts.put(size, nativeFont);
			return nativeFont;
		}
	}

	public final void updateScreen(SimpleScreen simpleScreen) {
		if (currentScreen != null) {
			currentScreen.dispose();
			currentScreen = null;
		}
		setScreen(currentScreen = simpleScreen);
	}

	public final OnlyAssetManager getOnlyAssetManager() {
		return onlyAssetManager;
	}

	public final SpriteBatch getSpriteBatch() {
		return spriteBatch;
	}

}
