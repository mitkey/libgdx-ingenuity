package com.badlogic.gdx.ingenuity.screen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.ingenuity.GdxData;
import com.badlogic.gdx.ingenuity.utils.helper.PixmapHelper;
import com.badlogic.gdx.ingenuity.utils.scene2d.SimpleScreen;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
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
			Arrays.asList(LoginScreen.class, HallScreen.class, RoomScreen.class));

	@Override
	public void show() {
		super.show();

		// 纹理
		Drawable up = changeSpace(PixmapHelper.getInstance().newRectangleDrawable(Color.DARK_GRAY, 20, 20));
		Drawable down = changeSpace(PixmapHelper.getInstance().newRectangleDrawable(Color.GRAY, 20, 20));
		Drawable checked = changeSpace(PixmapHelper.getInstance().newRectangleDrawable(Color.LIGHT_GRAY, 20, 20));

		// 按钮组
		ButtonGroup<NativeButton> buttonGroup = new ButtonGroup<NativeButton>();
		// 垂直容器
		Table table = new Table();
		table.pad(5).center().defaults().align(Align.center).top().space(10);
		for (Class<? extends SimpleScreen> clazz : tests) {
			NativeButton button = newNativeButton(clazz.getSimpleName(), up, down, checked, 20);
			table.add(button).size(130, 35).row();
			buttonGroup.add(button);
		}
		table.pack();
		table.setPosition(0, GdxData.HEIGHT - table.getHeight());
		stage().addActor(table);

		// 选中当前的
		buttonGroup.uncheckAll();
		for (NativeButton temp : buttonGroup.getButtons()) {
			if (getCurScreenClazz().getSimpleName().equals(temp.getText().toString())) {
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

	private Drawable changeSpace(Drawable drawable) {
		drawable.setTopHeight(20);
		drawable.setRightWidth(20);
		drawable.setBottomHeight(20);
		drawable.setLeftWidth(20);
		return drawable;
	}

}
