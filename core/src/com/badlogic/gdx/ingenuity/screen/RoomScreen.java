package com.badlogic.gdx.ingenuity.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.ingenuity.GdxData;
import com.badlogic.gdx.ingenuity.utils.GdxUtil;
import com.badlogic.gdx.ingenuity.utils.MoveListener;
import com.badlogic.gdx.ingenuity.utils.helper.PixmapHelper;
import com.badlogic.gdx.ingenuity.utils.scene2d.SimpleScreen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import net.mwplay.nativefont.NativeButton;
import net.mwplay.nativefont.NativeFont;
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
		NativeFont nativeFont = GdxData.getInstance().getFont(30);

		NativeLabel label = new NativeLabel("我是房间界面", new LabelStyle(nativeFont, Color.WHITE));
		GdxUtil.center(label);
		stage().addActor(label);

		Drawable up = PixmapHelper.getInstance().newRectangleDrawable(Color.CORAL, 120, 60);
		Drawable down = PixmapHelper.getInstance().newRectangleDrawable(Color.MAROON, 120, 60);
		NativeButton btnExitRoom = new NativeButton("退出房间", new TextButtonStyle(up, down, null, nativeFont));
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
