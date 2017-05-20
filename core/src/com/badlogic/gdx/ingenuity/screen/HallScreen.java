package com.badlogic.gdx.ingenuity.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.ingenuity.utils.GdxUtil;

import net.mwplay.nativefont.NativeLabel;

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

		NativeLabel nativeLabel = newNativeLabel("我是大厅界面", 30, Color.WHITE);
		GdxUtil.center(nativeLabel);
		stage().addActor(nativeLabel);
	}

	@Override
	public void dispose() {
		super.dispose();
	}

}
