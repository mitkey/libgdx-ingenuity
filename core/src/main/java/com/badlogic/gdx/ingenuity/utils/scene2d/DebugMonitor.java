package com.badlogic.gdx.ingenuity.utils.scene2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.ingenuity.GdxData;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

/**
 * @作者 mitkey
 * @时间 2017年5月18日 下午3:04:53
 * @类说明 DebugMonitor.java <br/>
 * @版本 0.0.1
 */
public class DebugMonitor extends Table {

	public DebugMonitor() {
		LabelStyle labelStyle = new Label.LabelStyle(GdxData.getInstance().getFont(18), Color.WHITE);

		defaults().left().pad(10);

		add(new NumberLabel<Integer>("Fps:", -1, labelStyle) {
			@Override
			public Integer getValue() {
				return Gdx.graphics.getFramesPerSecond();
			}
		}).row();
		add(new NumberLabel<Float>("Heap:", -1f, labelStyle) {
			@Override
			public Float getValue() {
				return Gdx.app.getJavaHeap() * 1f / 1024 / 1024;
			}
		}).row();
		add(new NumberLabel<Float>("Native:", -1f, labelStyle) {
			@Override
			public Float getValue() {
				return Gdx.app.getNativeHeap() * 1f / 1024 / 1024;
			}
		}).row();
		add(new NumberLabel<Integer>("ManagedTextures:", -1, labelStyle) {
			@Override
			public Integer getValue() {
				return Texture.getNumManagedTextures();
			}
		}).row();
		add(new NumberLabel<Integer>("RenderCalls:", -1, labelStyle) {
			@Override
			public Integer getValue() {
				SpriteBatch spriteBatch = SimpleScreen.spriteBatch();
				return spriteBatch == null ? -1 : spriteBatch.renderCalls;
			}
		}).row();

		setPosition(10, 10);
		pack();
	}

}
