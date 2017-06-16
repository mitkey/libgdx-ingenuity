package com.badlogic.gdx.ingenuity.test.screen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.ingenuity.GdxData;
import com.badlogic.gdx.ingenuity.helper.PixmapHelper;
import com.badlogic.gdx.ingenuity.scene2d.SimpleScreen;
import com.badlogic.gdx.ingenuity.screen.SimpleLoadingScreen.ILoadingComplete;
import com.badlogic.gdx.ingenuity.test.Asset;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;

import net.mwplay.nativefont.NativeButton;

/**
 * @作者 mitkey
 * @时间 2017年5月20日 下午12:14:45
 * @类说明 BaseTestScreen.java <br/>
 * @版本 0.0.1
 */
public abstract class BaseTestScreen extends SimpleScreen {

	public static final List<Class<? extends SimpleScreen>> tests = new ArrayList<Class<? extends SimpleScreen>>(
			Arrays.asList(LoginScreen.class, HallScreen.class, RoomScreen.class, TestScreen.class));

	protected Drawable dabUp = PixmapHelper.getInstance().newRectangleDrawable(new Color(51 / 255f, 122 / 255f, 183 / 255f, 1), 150, 50);
	protected Drawable dabDown = PixmapHelper.getInstance().newRectangleDrawable(new Color(40 / 255f, 96 / 255f, 144 / 255f, 1), 150, 50);
	protected Drawable dabChecked = PixmapHelper.getInstance().newRectangleDrawable(new Color(39 / 255f, 72 / 255f, 100 / 255f, 1), 150, 50);

	@Override
	public void show() {
		super.show();

		// 按钮组
		final ButtonGroup<NativeButton> buttonGroup = new ButtonGroup<NativeButton>();
		// 垂直容器
		Table table = new Table();
		table.pad(10).center().defaults().align(Align.center).top().space(10);
		for (final Class<? extends SimpleScreen> clazz : tests) {
			NativeButton button = newNativeButton(clazz.getSimpleName(), dabUp, dabDown, dabChecked, 20);
			table.add(button).size(150, 50).row();
			boolean checkCurChecked = checkCurChecked(button);
			if (checkCurChecked) {
				button.setDisabled(true);
				button.setTouchable(Touchable.disabled);
			}
			buttonGroup.add(button);
			button.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					super.clicked(event, x, y);
					getAppGame().updateScreen(new LoadingScreen(new ILoadingComplete() {
						@Override
						public boolean complete() {
							try {
								getAppGame().updateScreen(clazz.newInstance());
							} catch (InstantiationException | IllegalAccessException e) {
								e.printStackTrace();
							}
							return true;
						}
					}, Asset.none));
				}
			});
		}
		table.pack();
		table.setPosition(0, GdxData.HEIGHT - table.getHeight());
		stage().addActor(table);

		// 选中当前的
		buttonGroup.uncheckAll();
		for (NativeButton temp : buttonGroup.getButtons()) {
			if (checkCurChecked(temp)) {
				temp.setChecked(true);
			}
		}
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	public Class<? extends SimpleScreen> getCurScreenClazz() {
		return getClass();
	}

	private boolean checkCurChecked(NativeButton temp) {
		return getCurScreenClazz().getSimpleName().equals(temp.getText().toString());
	}

}
