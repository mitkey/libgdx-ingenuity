package com.badlogic.gdx.ingenuity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.ingenuity.utils.GdxUtil;
import com.badlogic.gdx.ingenuity.utils.MoveListener;
import com.badlogic.gdx.ingenuity.utils.scene2d.SimpleScreen;
import com.badlogic.gdx.ingenuity.utils.scene2d.RemoteImage;

/**
 * @作者 Mitkey
 * @时间 2016年9月7日 下午7:06:48
 * @类说明:
 * @版本 xx
 */
public class IngenuityScreen extends SimpleScreen {

	Texture texture;
	RemoteImage image;

	@Override
	public void show() {
		super.show();

		texture = new Texture("badlogic.jpg");
		image = new RemoteImage(texture, "http://img.lanrentuku.com/img/allimg/1605/14647058959840.jpg");
		image.setPosition(250, 256);
		image.addListener(new MoveListener(image));
		stage().addActor(image);
		GdxUtil.center(stage(), image);

		InputProcessor processor = new InputMultiplexer(stage());
		Gdx.input.setInputProcessor(processor);
	}

	@Override
	public void dispose() {
		super.dispose();
		if (texture != null) {
			texture.dispose();
			texture = null;
		}
		if (image != null) {
			image.dispose();
			image = null;
		}
	}

}
