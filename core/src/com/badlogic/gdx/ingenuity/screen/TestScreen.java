package com.badlogic.gdx.ingenuity.screen;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ingenuity.utils.GdxUtil;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputEvent.Type;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Pools;

import net.mwplay.nativefont.NativeButton;

/**
 * @作者 mitkey
 * @时间 2017年5月21日 下午5:25:03
 * @类说明 TestScreen.java <br/>
 * @版本 0.0.1
 */
public class TestScreen extends BaseTestScreen {

	private NativeButton btnToast;

	@Override
	public void show() {
		super.show();

		NativeButton btnLoading = newNativeButton("加载进度", dabUp, dabDown, null, 25);
		btnLoading.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				showLoading();
			}
		});

		btnToast = newNativeButton("提示信息", dabUp, dabDown, null, 25);
		btnToast.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				showMesssage(stage().toString());
			}
		});

		VerticalGroup verticalGroup = new VerticalGroup();
		verticalGroup.addActor(btnLoading);
		verticalGroup.addActor(btnToast);
		verticalGroup.space(20);
		verticalGroup.pack();
		GdxUtil.center(verticalGroup);
		stage().addActor(verticalGroup);
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.SPACE) {
			hideLoading();
		} else if (keycode == Keys.T) {
			// 程序代码触发监听事件
			InputEvent obtain = Pools.obtain(InputEvent.class);
			obtain.setStage(stage());
			obtain.setTarget(btnToast);
			obtain.setType(Type.touchUp);
			obtain.setBubbles(false);
			btnToast.fire(obtain);
		}

		return super.keyDown(keycode);
	}

}
