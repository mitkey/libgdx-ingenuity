package com.badlogic.gdx.ingenuity.screen;

import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ingenuity.GdxData;
import com.badlogic.gdx.ingenuity.utils.Utils;
import com.badlogic.gdx.ingenuity.utils.scene2d.SimpleScreen;

/**
 * @作者 mitkey
 * @时间 2017年6月2日 下午3:28:02
 * @类说明 SimpleLoadingScreen.java <br/>
 * @版本 0.0.1
 */
public abstract class SimpleLoadingScreen extends SimpleScreen {

	private static final String tag = SimpleLoadingScreen.class.getSimpleName();

	// 加载资源完成后的回调
	private ILoadingComplete loadingComplete;
	// 标识是否已经加载完全部资源
	private boolean loaded;

	public SimpleLoadingScreen(ILoadingComplete loadingComplete, Set<String> unloads, Set<String> loads) {
		super();
		this.loadingComplete = loadingComplete;
		this.loaded = false;

		// 卸载除了当前类别之外的所有资源
		assetManager().unload(unloads);
		// 加载当前类别的资源
		assetManager().load(loads);
	}

	@Override
	public void render(float delta) {
		super.render(delta);

		if (assetManager().getManager().update()) {
			loaded = true;
		}

		if (loaded && renderProgressDone()) {
			if (loadingComplete != null && loadingComplete.complete()) {
				loadingComplete = null;

				if (!GdxData.RELEASE_VERSION) {
					Gdx.app.log(tag, "已加载的资源数量：" + assetManager().getManager().getLoadedAssets());
					Gdx.app.log(tag, "已加载的资源列表：" + assetManager().getManager().getAssetNames());
					Gdx.app.log(tag, "资源依赖：" + assetManager().getManager().getDiagnostics());
					Utils.printManagedTextures();
				}
				System.gc();
			}
		}
	}

	/** 资源是否加载完 */
	public final boolean isloaded() {
		return loaded;
	}

	/** 真实加载进度值为 0 到 1 */
	public final float getRealProgress() {
		return assetManager().getManager().getProgress();
	}

	/** 渲染进度是否完成 */
	public abstract boolean renderProgressDone();

	/** 加载完成回调处理 */
	public static interface ILoadingComplete {
		boolean complete();
	}

}
