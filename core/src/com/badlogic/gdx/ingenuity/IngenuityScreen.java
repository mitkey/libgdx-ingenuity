package com.badlogic.gdx.ingenuity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.ingenuity.utils.LazyBitmapFont;
import com.badlogic.gdx.ingenuity.utils.MoveListener;
import com.badlogic.gdx.ingenuity.utils.scene2d.GeneralScreen;
import com.badlogic.gdx.ingenuity.utils.scene2d.NumberLabel;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

/**
 * @作者 Mitkey
 * @时间 2016年9月7日 下午7:06:48
 * @类说明:
 * @版本 xx
 */
public class IngenuityScreen extends GeneralScreen {

	BitmapFont font;
	Texture texture;

	@Override
	public void show() {
		super.show();

		font = new LazyBitmapFont(20);
		LabelStyle labelStyle = new LabelStyle(font, Color.WHITE);

		NumberLabel<Integer> labFps = new NumberLabel<Integer>("FPS: ", -99, labelStyle) {
			@Override
			public Integer getValue() {
				return Gdx.graphics.getFramesPerSecond();
			}
		};
		NumberLabel<Float> labHeap = new NumberLabel<Float>("Heap: ", -99f, labelStyle) {
			@Override
			public Float getValue() {
				return Gdx.app.getJavaHeap() * 1f / 1024 / 1024;
			}
		};
		NumberLabel<Float> labNative = new NumberLabel<Float>("Native: ", -99f, labelStyle) {
			@Override
			public Float getValue() {
				return Gdx.app.getNativeHeap() * 1f / 1024 / 1024;
			}
		};

		Table table = new Table();
		table.defaults().width(150).left();
		table.add(labFps).left().row();
		table.add(labHeap).row();
		table.add(labNative).row();
		table.pack();
		table.layout();
		table.setPosition(50, 100);
		stage().addActor(table);
		table.clipBegin();
		table.clipEnd();

		texture = new Texture("badlogic.jpg");
		Image image = new Image(texture);
		image.setPosition(250, 150);
		stage().addActor(image);
		image.addListener(new MoveListener(image));

		InputProcessor processor = new InputMultiplexer(stage());
		Gdx.input.setInputProcessor(processor);
	}

	@Override
	protected void update(float delta) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void draw(float delta) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		super.dispose();
		font.dispose();
		texture.dispose();
	}

}
