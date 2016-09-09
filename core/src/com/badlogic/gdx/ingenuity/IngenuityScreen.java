package com.badlogic.gdx.ingenuity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.ingenuity.utils.MoveListener;
import com.badlogic.gdx.ingenuity.utils.scene2d.GeneralScreen;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * @作者 Mitkey
 * @时间 2016年9月7日 下午7:06:48
 * @类说明:
 * @版本 xx
 */
public class IngenuityScreen extends GeneralScreen {

	Texture texture;

	@Override
	public void show() {
		super.show();

		texture = new Texture("badlogic.jpg");
		Image image = new Image(texture);
		image.setPosition(250, 150);
		image.addListener(new MoveListener(image));
		stage().addActor(image);

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
		texture.dispose();
	}

}
