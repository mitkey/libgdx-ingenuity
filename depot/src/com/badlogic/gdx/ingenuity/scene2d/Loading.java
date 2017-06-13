package com.badlogic.gdx.ingenuity.scene2d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.ingenuity.GdxData;
import com.badlogic.gdx.ingenuity.helper.PixmapHelper;
import com.badlogic.gdx.ingenuity.utils.GdxUtilities;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;

/**
 * @作者 Mitkey
 * @时间 2017年3月24日 下午3:09:56
 * @类说明:
 * @版本 xx
 */
public class Loading implements Disposable {

	private Group root = new Group();
	private Image imgOut;
	private Image imgInner;

	public Loading() {
		root.setSize(GdxData.WIDTH, GdxData.HEIGHT);

		Image imgBg = new Image(PixmapHelper.getInstance().newTranslucentDrawable(5, 5));
		imgBg.setFillParent(true);
		root.addActor(imgBg);

		imgOut = new Image(PixmapHelper.getInstance().newRectangleDrawable(Color.YELLOW, 40, 40));
		imgOut.setOrigin(Align.center);

		imgInner = new Image(PixmapHelper.getInstance().newCircleDrawable(Color.RED, 18));
		imgInner.setOrigin(Align.center);

		GdxUtilities.center(imgOut);
		GdxUtilities.center(imgInner);
		root.addActor(imgOut);
		root.addActor(imgInner);
	}

	public void show(Stage stage) {
		stage.addActor(root);
		root.toFront();

		imgOut.clearActions();
		imgOut.addAction(Actions.forever(Actions.rotateBy(-360, 1f)));
		imgInner.clearActions();
		imgInner.addAction(Actions.forever(Actions.rotateBy(360, 2f)));
	}

	public void hide() {
		root.remove();
	}

	@Override
	public void dispose() {
		hide();
	}

}
