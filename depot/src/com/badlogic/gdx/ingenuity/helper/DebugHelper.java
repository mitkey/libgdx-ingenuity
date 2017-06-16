package com.badlogic.gdx.ingenuity.helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.ingenuity.GdxData;
import com.badlogic.gdx.ingenuity.scene2d.NumberLabel;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

/**
 * @作者 mitkey
 * @时间 2017年6月9日 下午6:06:21
 * @类说明 DebugHelper.java <br/>
 * @版本 0.0.1
 */
public class DebugHelper {

	private static DebugHelper ourInstance;

	private Table root;

	private boolean needShow;

	private DebugHelper() {
	}

	public static DebugHelper getInstance() {
		if (ourInstance == null) {
			synchronized (DebugHelper.class) {
				if (ourInstance == null) {
					ourInstance = new DebugHelper();
				}
			}
		}
		return ourInstance;
	}

	public void showDebugOnOff() {
		generateRoot();
		needShow = !needShow;
	}

	/** 渲染绘制 */
	public void renderDebug() {
		if (root != null && needShow) {
			SpriteBatch spriteBatch = GdxData.getInstance().getAppGame().getSpriteBatch();
			if (spriteBatch != null) {
				spriteBatch.begin();
				root.draw(spriteBatch, 1);
				root.act(Gdx.graphics.getDeltaTime());
				spriteBatch.end();
			}
		}
	}

	// ====================
	// ====================
	// ====== 分割线 =======
	// ====================
	// ====================

	private void generateRoot() {
		if (root != null) {
			return;
		}
		LabelStyle labelStyle = new Label.LabelStyle(GdxData.getInstance().getAppGame().getFont(18), Color.WHITE);
		root = new Table();
		root.defaults().left().pad(10);
		root.add(new NumberLabel<Integer>("Fps:", -1, labelStyle) {
			@Override
			public Integer getValue() {
				return Gdx.graphics.getFramesPerSecond();
			}
		}).row();
		root.add(new NumberLabel<Float>("Heap:", -1f, labelStyle) {
			@Override
			public Float getValue() {
				return Gdx.app.getJavaHeap() * 1f / 1024 / 1024;
			}
		}).row();
		root.add(new NumberLabel<Float>("Native:", -1f, labelStyle) {
			@Override
			public Float getValue() {
				return Gdx.app.getNativeHeap() * 1f / 1024 / 1024;
			}
		}).row();
		root.add(new NumberLabel<Integer>("ManagedTextures:", -1, labelStyle) {
			@Override
			public Integer getValue() {
				return Texture.getNumManagedTextures();
			}
		}).row();
		root.layout();
		root.pack();
		root.setPosition(10, 10);
	}

}
