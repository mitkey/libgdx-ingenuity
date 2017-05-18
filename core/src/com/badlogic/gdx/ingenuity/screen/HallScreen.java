package com.badlogic.gdx.ingenuity.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.ingenuity.utils.GdxUtil;
import com.badlogic.gdx.ingenuity.utils.LazyBitmapFont;
import com.badlogic.gdx.ingenuity.utils.MoveListener;
import com.badlogic.gdx.ingenuity.utils.helper.PixmapHelper;
import com.badlogic.gdx.ingenuity.utils.scene2d.SimpleScreen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/**
 * @作者 Mitkey
 * @时间 2017年3月3日 上午11:38:03
 * @类说明:
 * @版本 xx
 */
public class HallScreen extends SimpleScreen {

	private static String tag = HallScreen.class.getSimpleName();

	private LazyBitmapFont bitmapFont;

	@Override
	public void show() {
		super.show();
		bitmapFont = new LazyBitmapFont(30);

		Label label = new Label("我是大厅界面", new LabelStyle(bitmapFont, Color.WHITE));
		GdxUtil.center(label);
		stage().addActor(label);

		Drawable up = PixmapHelper.getInstance().newRectangleDrawable(Color.CORAL, 120, 60);
		Drawable down = PixmapHelper.getInstance().newRectangleDrawable(Color.MAROON, 120, 60);

		TextButton btnLogout = new TextButton("注销", new TextButtonStyle(up, down, null, bitmapFont));
		btnLogout.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				Gdx.app.log(tag, "点击注销按钮");
				game().loading2Login();
			}
		});

		TextButton btnEnterRoom = new TextButton("进入房间", new TextButtonStyle(up, down, null, bitmapFont));
		btnEnterRoom.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				Gdx.app.log(tag, "点击进入房间按钮");
				game().loading2Room();
			}
		});

		HorizontalGroup horizontalGroup = new HorizontalGroup();
		horizontalGroup.space(50);
		horizontalGroup.addActor(btnLogout);
		horizontalGroup.addActor(btnEnterRoom);
		horizontalGroup.pack();
		horizontalGroup.addListener(new MoveListener(horizontalGroup));
		GdxUtil.center(horizontalGroup);
		horizontalGroup.setY(label.getY() - horizontalGroup.getHeight() - 10);
		stage().addActor(horizontalGroup);
	}

	@Override
	public void dispose() {
		super.dispose();
		if (bitmapFont != null) {
			bitmapFont.dispose();
			bitmapFont = null;
		}
	}

}
