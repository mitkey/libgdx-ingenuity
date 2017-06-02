package com.badlogic.gdx.ingenuity.test.screen;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.ingenuity.screen.SimpleLoadingScreen;
import com.badlogic.gdx.ingenuity.test.Asset;
import com.badlogic.gdx.ingenuity.utils.GdxUtil;
import com.badlogic.gdx.ingenuity.utils.helper.PixmapHelper;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle;

import net.mwplay.nativefont.NativeLabel;

/**
 * @作者 Mitkey
 * @时间 2017年3月2日 下午5:51:44
 * @类说明:
 * @版本 xx
 */
public class LoadingScreen extends SimpleLoadingScreen {

	private ProgressBar progressBar;

	private boolean doneProgress;

	public LoadingScreen(ILoadingComplete loadingComplete, Asset... assets) {
		super(loadingComplete, parseUnloadAssets(assets), parseLoadAssets(assets));
	}

	@Override
	public void show() {
		super.show();
		// 文字标识
		NativeLabel label = newNativeLabel("我是加载界面", 35, Color.WHITE);
		GdxUtil.center(label);
		stage().addActor(label);

		// 进度条样式
		ProgressBarStyle style = new ProgressBarStyle();
		style.background = PixmapHelper.getInstance().newRectangleDrawable(Color.WHITE, 20, 20);
		style.knobBefore = PixmapHelper.getInstance().newRectangleDrawable(Color.BLACK, 10, 20);
		// 进度条
		progressBar = new ProgressBar(0, 1000, 5f, false, style);
		progressBar.setSize(600, 20);
		GdxUtil.center(progressBar);
		progressBar.setY(120);
		stage().addActor(progressBar);
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		float curValue = progressBar.getValue();
		float maxValue = progressBar.getMaxValue();
		if (curValue < maxValue) {
			float stepSize = progressBar.getStepSize();
			curValue += isloaded() ? stepSize * 7 : stepSize;
			progressBar.setValue(curValue);
		} else {
			doneProgress = true;
		}
	}

	@Override
	public boolean renderProgressDone() {
		return doneProgress;
	}

	/** 解析需要卸载的资源 */
	static Set<String> parseUnloadAssets(Asset... assets) {
		Set<String> result = new HashSet<String>();
		for (Asset all : Asset.values()) {
			result.addAll(all.names());
		}
		for (Asset not : assets) {
			result.removeAll(not.names());
		}
		// 每次卸载都排除 common
		result.removeAll(Asset.common.names());
		return result;
	}

	/** 解析需要加载的资源 */
	static Set<String> parseLoadAssets(Asset... assets) {
		Set<String> result = new HashSet<String>();
		for (Asset item : assets) {
			result.addAll(item.names());
		}
		return result;
	}

}
