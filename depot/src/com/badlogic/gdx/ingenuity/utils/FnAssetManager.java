package com.badlogic.gdx.ingenuity.utils;

import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;

/**
 * @作者 Mitkey
 * @时间 2016年5月17日 下午7:25:39
 * @类说明:基于文件名的 assets 管理
 * @版本 xx
 */
public final class FnAssetManager implements Disposable {

	private static final String tag = FnAssetManager.class.getSimpleName();

	/** 是否开启资源监控日志 */
	public static boolean enableAssetMonitorLog = false;

	AssetManager manager;

	public FnAssetManager() {
		this.manager = new AssetManager() {
			@Override
			public synchronized <T> void load(String fileName, Class<T> type, AssetLoaderParameters<T> parameter) {
				super.load(fileName, type, parameter);
				if (enableAssetMonitorLog) {
					Gdx.app.log(tag, type.getSimpleName() + " 加载资源 " + fileName);
				}
			}

			@Override
			public synchronized void unload(String fileName) {
				super.unload(fileName);
				if (enableAssetMonitorLog) {
					Gdx.app.log(tag, "卸载资源 " + fileName);
				}
			}
		};
	}

	public AssetManager getManager() {
		return manager;
	}

	public void load(Set<String> fileNames) {
		for (String fileName : fileNames) {
			if (fileName.endsWith(".png") || fileName.endsWith(".jpg")) {
				manager.load(fileName, Texture.class);
			} else if (fileName.endsWith(".json")) {
				manager.load(fileName, Skin.class);
			} else if (fileName.endsWith(".ogg")) {
				manager.load(fileName, Sound.class);
			} else if (fileName.endsWith(".mp3")) {
				manager.load(fileName, Music.class);
			} else if (fileName.endsWith(".atlas") || fileName.endsWith("pack")) {
				manager.load(fileName, TextureAtlas.class);
			} else {
				Gdx.app.log(tag, fileName + " 无法判断加载资源类型");
			}
		}
	}

	public void unload(Set<String> fileNames) {
		for (String fileName : fileNames) {
			if (manager.isLoaded(fileName)) {
				manager.unload(fileName);
			}
		}
	}

	public Sound getSound(String fileName) {
		return getAssets(fileName, Sound.class);
	}

	public Music getMusic(String fileName) {
		return getAssets(fileName, Music.class);
	}

	public Skin getSkin(String fileName) {
		return getAssets(fileName, Skin.class);
	}

	public Texture getTexture(String fileName) {
		Texture texture = getAssets(fileName, Texture.class);
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		texture.setWrap(TextureWrap.ClampToEdge, TextureWrap.ClampToEdge);
		return texture;
	}

	public TextureAtlas getAtlas(String fileName) {
		return getAssets(fileName, TextureAtlas.class);
	}

	public TextureRegion getRegion(String fileName) {
		return new TextureRegion(getTexture(fileName));
	}

	public Drawable newDrawable(String fileName) {
		return new TextureRegionDrawable(getRegion(fileName));
	}

	public NinePatchDrawable new9Drawable(String fileName, int left, int right, int top, int bottom) {
		return new9Drawable(getTexture(fileName), left, right, top, bottom);
	}

	public NinePatchDrawable new9Drawable(Texture texture, int left, int right, int top, int bottom) {
		return new NinePatchDrawable(new NinePatch(texture, left, right, top, bottom));
	}

	<T extends Disposable> T getAssets(String fileName, Class<T> clazz) {
		if (!manager.isLoaded(fileName, clazz)) {
			Gdx.app.log(tag, clazz.getSimpleName() + " 资源未加载 " + fileName);
			manager.load(fileName, clazz);
			manager.finishLoadingAsset(fileName);
		}
		return manager.get(fileName, clazz);
	}

	@Override
	public void dispose() {
		manager.dispose();
		manager.clear();
	}

}
