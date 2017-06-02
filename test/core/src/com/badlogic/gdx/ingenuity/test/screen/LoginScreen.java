package com.badlogic.gdx.ingenuity.test.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.ingenuity.GdxData;
import com.badlogic.gdx.ingenuity.test.GdxR;
import com.badlogic.gdx.ingenuity.utils.GdxUtil;
import com.badlogic.gdx.ingenuity.utils.MoveListener;
import com.badlogic.gdx.ingenuity.utils.scene2d.FilterImage;
import com.badlogic.gdx.ingenuity.utils.scene2d.FilterImage.FilterType;
import com.badlogic.gdx.ingenuity.utils.scene2d.RemoteImage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;

import net.mwplay.nativefont.NativeLabel;

/**
 * @作者 Mitkey
 * @时间 2017年3月2日 下午5:14:54
 * @类说明:
 * @版本 xx
 */
public class LoginScreen extends BaseTestScreen {

	RemoteImage image;

	@Override
	public void show() {
		super.show();
		Texture texture = assetManager().getTexture(GdxR.def_badlogic_jpg);

		image = new RemoteImage(texture, "http://img.lanrentuku.com/img/allimg/1605/14647058959840.jpg");
		image.setPosition(250, 256);
		image.addListener(new MoveListener(image));
		stage().addActor(image);

		NativeLabel label = newNativeLabel("我是登录界面", 30, Color.YELLOW);
		GdxUtil.center(label);
		stage().addActor(label);

		// 滤镜 image 测试
		VerticalGroup verticalGroup = new VerticalGroup();
		for (FilterType type : FilterType.values()) {
			FilterImage filterImage = new FilterImage(texture);
			filterImage.setType(type);
			filterImage.setRadius(5);
			filterImage.setSigma(5);
			filterImage.setSepiaTone(.5f, .5f, .1f);
			filterImage.invalidate();
			filterImage.validate();
			verticalGroup.addActor(filterImage);
		}
		verticalGroup.pack();
		ScrollPane scrollPane = new ScrollPane(verticalGroup);
		scrollPane.setSize(image.getWidth() + 50, GdxData.HEIGHT);
		scrollPane.setX(GdxData.WIDTH - scrollPane.getWidth());
		stage().addActor(scrollPane);
	}

	@Override
	public void dispose() {
		super.dispose();
		if (image != null) {
			image.dispose();
			image = null;
		}
	}

}
