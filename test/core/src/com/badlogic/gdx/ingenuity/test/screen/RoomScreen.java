package com.badlogic.gdx.ingenuity.test.screen;

import com.badlogic.gdx.ingenuity.utils.GdxUtilities;

/**
 * @作者 Mitkey
 * @时间 2017年3月3日 上午11:53:37
 * @类说明:
 * @版本 xx
 */
public class RoomScreen extends BaseTestScreen {

	@Override
	public void show() {
		super.show();

		stage().addActor(GdxUtilities.center(newNativeLabel("我是房间界面", 30)));
	}

	@Override
	public void dispose() {
		super.dispose();
	}

}
