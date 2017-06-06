package com.badlogic.gdx.ingenuity.test.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.ingenuity.utils.GdxUtilities;

/**
 * @作者 Mitkey
 * @时间 2017年3月3日 上午11:38:03
 * @类说明:
 * @版本 xx
 */
public class HallScreen extends BaseTestScreen {

	@Override
	public void show() {
		super.show();
		stage().addActor(GdxUtilities.center(newNativeLabel("我是大厅界面", 30, Color.WHITE)));
	}

	@Override
	public void dispose() {
		super.dispose();
	}

}
