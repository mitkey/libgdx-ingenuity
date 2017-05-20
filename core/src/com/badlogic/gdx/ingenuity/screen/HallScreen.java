package com.badlogic.gdx.ingenuity.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.ingenuity.utils.GdxUtil;
import com.badlogic.gdx.ingenuity.utils.MoveListener;
import com.badlogic.gdx.ingenuity.utils.helper.PixmapHelper;
import com.badlogic.gdx.ingenuity.utils.scene2d.SimpleScreen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import net.mwplay.nativefont.NativeButton;
import net.mwplay.nativefont.NativeLabel;

/**
 * @作者 Mitkey
 * @时间 2017年3月3日 上午11:38:03
 * @类说明:
 * @版本 xx
 */
public class HallScreen extends SimpleScreen {

	private static String tag = HallScreen.class.getSimpleName();

	@Override
	public void show() {
		super.show();

		NativeLabel nativeLabel = newNativeLabel("我是大厅界面", 30, Color.WHITE);
		GdxUtil.center(nativeLabel);
		stage().addActor(nativeLabel);

		Drawable up = PixmapHelper.getInstance().newRectangleDrawable(Color.CORAL, 120, 60);
		Drawable down = PixmapHelper.getInstance().newRectangleDrawable(Color.MAROON, 120, 60);

		NativeButton btnLogout = newNativeButton("注销", up, down, null, 30);
		btnLogout.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				Gdx.app.log(tag, "点击注销按钮");
				game().loading2Login();
			}
		});

		NativeButton btnEnterRoom = newNativeButton("进入房间", up, down, null, 30);
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
		horizontalGroup.setY(nativeLabel.getY() - horizontalGroup.getHeight() - 10);
		stage().addActor(horizontalGroup);
	}

	@Override
	public void dispose() {
		super.dispose();
	}

}
