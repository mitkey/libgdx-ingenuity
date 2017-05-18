package com.badlogic.gdx.ingenuity.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.ingenuity.utils.GdxUtil;
import com.badlogic.gdx.ingenuity.utils.LazyBitmapFont;
import com.badlogic.gdx.ingenuity.utils.MoveListener;
import com.badlogic.gdx.ingenuity.utils.helper.PixmapHelper;
import com.badlogic.gdx.ingenuity.utils.scene2d.RemoteImage;
import com.badlogic.gdx.ingenuity.utils.scene2d.SimpleScreen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/**
 * @作者 Mitkey
 * @时间 2017年3月2日 下午5:14:54
 * @类说明:
 * @版本 xx
 */
public class LoginScreen extends SimpleScreen {

	private static final String tag = LoginScreen.class.getSimpleName();

	private LazyBitmapFont bitmapFont;

	Texture texture;
	RemoteImage image;

	@Override
	public void show() {
		super.show();
		bitmapFont = new LazyBitmapFont(30);

		texture = new Texture("badlogic.jpg");
		image = new RemoteImage(texture, "http://img.lanrentuku.com/img/allimg/1605/14647058959840.jpg");
		image.setPosition(250, 256);
		image.addListener(new MoveListener(image));
		stage().addActor(image);

		Label label = new Label("我是登录界面", new LabelStyle(bitmapFont, Color.WHITE));
		GdxUtil.center(label);
		stage().addActor(label);

		Drawable up = PixmapHelper.getInstance().newRectangleDrawable(Color.CORAL, 120, 60);
		Drawable down = PixmapHelper.getInstance().newRectangleDrawable(Color.MAROON, 120, 60);

		TextButton btnLogin = new TextButton("登录", new TextButtonStyle(up, down, null, bitmapFont));
		btnLogin.addListener(new MoveListener(btnLogin));
		btnLogin.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				Gdx.app.log(tag, "点击登录按钮");
				game().loading2Hall();
			}
		});
		GdxUtil.center(btnLogin);
		btnLogin.setY(label.getY() - btnLogin.getHeight() - 10);
		stage().addActor(btnLogin);
	}

	@Override
	public void dispose() {
		super.dispose();
		if (bitmapFont != null) {
			bitmapFont.dispose();
			bitmapFont = null;
		}
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
