package com.badlogic.gdx.ingenuity.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * @作者 Mitkey
 * @时间 2016年5月17日 下午7:25:39
 * @类说明:
 * @版本 xx
 */
public final class AssetsMgr {

	private static AssetsMgr instance;

	static AssetManager manager;

	private AssetsMgr() {
	}

	public static void initManager(AssetManager assetManager) {
		manager = assetManager;
	}

	public static synchronized AssetsMgr instance() {
		if (instance == null) {
			if (manager == null)
				throw new IllegalArgumentException("manager no init");
			instance = new AssetsMgr();
		}
		return instance;
	}

	/*
	 * #######################################################
	 */

	public Texture texture(String fileName) {
		Texture texture = null;
		if (!manager.isLoaded(fileName)) {
			manager.load(fileName, Texture.class);
			manager.finishLoading();
		}
		texture = manager.get(fileName, Texture.class);
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		texture.setWrap(TextureWrap.ClampToEdge, TextureWrap.ClampToEdge);
		return texture;
	}

	public TextureAtlas atlas(String packFileName) {
		TextureAtlas atlas = null;
		if (!manager.isLoaded(packFileName)) {
			manager.load(packFileName, TextureAtlas.class);
			manager.finishLoading();
		}
		atlas = manager.get(packFileName, TextureAtlas.class);
		return atlas;
	}

	/*
	 * #######################################################
	 */

	public TextureRegion region(String fileName) {
		return new TextureRegion(texture(fileName));
	}

	public Drawable drawable(String fileName) {
		return new TextureRegionDrawable(region(fileName));
	}

	public NinePatchDrawable ninePatchDrawable(String fileName, int left, int right, int top, int bottom) {
		return ninePatchDrawable(texture(fileName), left, right, top, bottom);
	}

	public NinePatchDrawable ninePatchDrawable(Texture texture, int left, int right, int top, int bottom) {
		return new NinePatchDrawable(new NinePatch(texture, left, right, top, bottom));
	}

	/*
	 * #######################################################
	 */

	public Image image(String fileName) {
		return new Image(texture(fileName));
	}

}
