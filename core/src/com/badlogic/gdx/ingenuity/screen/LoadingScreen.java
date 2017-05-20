package com.badlogic.gdx.ingenuity.screen;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.ingenuity.Asset;
import com.badlogic.gdx.ingenuity.GdxData;
import com.badlogic.gdx.ingenuity.utils.FnAssetManager;
import com.badlogic.gdx.ingenuity.utils.GdxUtil;
import com.badlogic.gdx.ingenuity.utils.Utils;
import com.badlogic.gdx.ingenuity.utils.helper.PixmapHelper;
import com.badlogic.gdx.ingenuity.utils.scene2d.SimpleScreen;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle;

import net.mwplay.nativefont.NativeLabel;

/**
 * @作者 Mitkey
 * @时间 2017年3月2日 下午5:51:44
 * @类说明:
 * @版本 xx
 */
public class LoadingScreen extends SimpleScreen {

	private static final String tag = LoadingScreen.class.getSimpleName();

	// 加载资源完成后的回调
	private ILoadingComplete loadingComplete;

	// 标识是否已经加载完全部资源
	private boolean isLoaded = false;
	// 进度条是否已经完成
	private boolean isProgressFinished = false;

	private ProgressBar progressBar;

	public LoadingScreen(ILoadingComplete loadingComplete, Asset... assets) {
		this.loadingComplete = loadingComplete;
		this.isLoaded = false;
		this.isProgressFinished = false;

		// 卸载除了当前类别之外的所有资源
		assetManager().unload(parseUnloadAssets(assets));
		// 加载当前类别的资源
		assetManager().load(parseLoadAssets(assets));
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
		if (!isLoaded) {
			if (assetManager().getManager().update()) {
				isLoaded = true;
			}
		}

		if (!isProgressFinished) {
			float curValue = progressBar.getValue();
			float maxValue = progressBar.getMaxValue();
			if (curValue < maxValue) {
				float stepSize = progressBar.getStepSize();
				curValue += isLoaded ? stepSize * 7 : stepSize;
				progressBar.setValue(curValue);

			} else {
				if (isLoaded && !isProgressFinished) {
					if (loadingComplete != null) {
						if (loadingComplete.complete()) {
							isProgressFinished = true;
							if (FnAssetManager.enableAssetMonitorLog) {
								Gdx.app.log(tag, "已加载的资源数量：" + assetManager().getManager().getLoadedAssets());
								Gdx.app.log(tag, "已加载的资源列表：" + assetManager().getManager().getAssetNames());
								Gdx.app.log(tag, "资源依赖：" + assetManager().getManager().getDiagnostics());

								if (GdxData.DEBUG_MANAGED_TEXTURES) {
									Utils.printManagedTextures();
								}
							}
							System.gc();
						}
					}
				}
			}
		}
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	/** 解析需要卸载的资源 */
	Set<String> parseUnloadAssets(Asset... assets) {
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
	Set<String> parseLoadAssets(Asset... assets) {
		Set<String> result = new HashSet<String>();
		for (Asset item : assets) {
			result.addAll(item.names());
		}
		return result;
	}

	public static interface ILoadingComplete {
		boolean complete();
	}

}
