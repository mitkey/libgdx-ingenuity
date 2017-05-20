package com.badlogic.gdx.ingenuity.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.ingenuity.utils.GdxUtil;
import com.badlogic.gdx.ingenuity.utils.MoveListener;
import com.badlogic.gdx.ingenuity.utils.helper.PixmapHelper;
import com.badlogic.gdx.ingenuity.utils.scene2d.SimpleScreen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import net.mwplay.nativefont.NativeButton;
import net.mwplay.nativefont.NativeLabel;

/**
 * @作者 Mitkey
 * @时间 2017年3月3日 上午11:53:37
 * @类说明:
 * @版本 xx
 */
public class RoomScreen extends SimpleScreen {

	private static final String tag = RoomScreen.class.getSimpleName();

	@Override
	public void show() {
		super.show();

		NativeLabel label = newNativeLabel("我是房间界面", 30);
		GdxUtil.center(label);
		stage().addActor(label);

		Drawable up = PixmapHelper.getInstance().newRectangleDrawable(Color.CORAL, 120, 60);
		Drawable down = PixmapHelper.getInstance().newRectangleDrawable(Color.MAROON, 120, 60);
		NativeButton btnExitRoom = newNativeButton("退出房间", up, down, null, 30);
		btnExitRoom.addListener(new MoveListener(btnExitRoom));
		btnExitRoom.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				Gdx.app.log(tag, "点击退出房间按钮");
				game().loading2Hall();
			}
		});
		GdxUtil.center(btnExitRoom);
		btnExitRoom.setY(label.getY() - btnExitRoom.getHeight() - 10);
		stage().addActor(btnExitRoom);
	}

	@Override
	public void dispose() {
		super.dispose();
	}

}
