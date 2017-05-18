package com.badlogic.gdx.ingenuity.utils.scene2d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.ingenuity.GlobalData;
import com.badlogic.gdx.ingenuity.utils.GdxUtil;
import com.badlogic.gdx.ingenuity.utils.LazyBitmapFont;
import com.badlogic.gdx.ingenuity.utils.helper.PixmapHelper;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

/**
 * @作者 Mitkey
 * @时间 2017年3月24日 下午3:20:43
 * @类说明:
 * @版本 xx
 */
public class SimpleToast implements Disposable {

	private Array<Window> arrayShowMessage = new Array<Window>(true, 50);

	private BitmapFont bitmapFont = new LazyBitmapFont(25);

	public void showToast(Stage stage, String content) {
		for (Window window : arrayShowMessage) {
			window.addAction(Actions.moveBy(0, window.getHeight(), .2f));
		}

		// 文本内容
		Label labContent = new Label(content, new LabelStyle(bitmapFont, Color.YELLOW));

		// 背景
		Image imgContentBg = new Image(PixmapHelper.getInstance().newRectangleDrawable(Color.BLACK, 10, 10));
		imgContentBg.setSize(labContent.getWidth() + 100, labContent.getHeight() + 20);
		GdxUtil.center(imgContentBg, labContent);

		// window 层显示的
		final Window window = new Window("", new WindowStyle(bitmapFont, Color.WHITE, null));
		window.setSize(imgContentBg.getWidth(), imgContentBg.getHeight());
		window.addActor(imgContentBg);
		window.addActor(labContent);
		window.setMovable(false);
		window.setResizable(false);
		window.setY(GlobalData.HEIGHT + window.getHeight() * 2);
		GdxUtil.center(window, true);
		// 该 window(包括子 actor) 忽略所有监听
		window.addCaptureListener(new EventListener() {
			@Override
			public boolean handle(Event event) {
				event.cancel();
				return false;
			}
		});
		window.addAction(Actions.sequence(Actions.moveTo(window.getX(), GlobalData.HEIGHT / 2 - window.getHeight() / 2, .2f), Actions.delay(2), Actions.run(new Runnable() {
			@Override
			public void run() {
				window.remove();
				arrayShowMessage.removeValue(window, true);
			}
		})));
		arrayShowMessage.add(window);
		stage.addActor(window);
	}

	@Override
	public void dispose() {
		if (bitmapFont != null) {
			bitmapFont.dispose();
			bitmapFont = null;
		}
		for (Window window : arrayShowMessage) {
			window.clear();
		}
		arrayShowMessage.clear();
	}

}
