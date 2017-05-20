package com.badlogic.gdx.ingenuity.screen;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.ingenuity.GdxData;
import com.badlogic.gdx.ingenuity.GdxR;
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

	public LoadingScreen(AssetsCategory category, ILoadingComplete loadingComplete) {
		Gdx.app.log(tag, "加载场景资源" + category);

		this.loadingComplete = loadingComplete;
		this.isLoaded = false;
		this.isProgressFinished = false;

		try {
			// 卸载除了当前类别之外的所有资源
			assetManager().unload(generalOtherAssetsNames(category));
			// 加载当前类别的资源
			assetManager().load(generalAssetsNames(category));
		} catch (IllegalArgumentException | IllegalAccessException e) {
			Gdx.app.error(tag, "加载 " + category + " 类别的资源异常", e);
		}
	}

	@Override
	public void show() {
		super.show();

		NativeLabel label = newNativeLabel("我是加载界面", 35, Color.WHITE);
		GdxUtil.center(label);
		stage().addActor(label);

		ProgressBarStyle style = new ProgressBarStyle();
		style.background = PixmapHelper.getInstance().newRectangleDrawable(Color.WHITE, 20, 20);
		style.knobBefore = PixmapHelper.getInstance().newRectangleDrawable(Color.BLACK, 10, 20);

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

	Set<String> generalOtherAssetsNames(AssetsCategory target) throws IllegalArgumentException, IllegalAccessException {
		Set<String> assetsNames = new HashSet<String>();
		for (AssetsCategory category : target.generalOtherCategorty()) {
			assetsNames.addAll(generalAssetsNames(category));
		}
		return assetsNames;
	}

	Set<String> generalAssetsNames(AssetsCategory category) throws IllegalArgumentException, IllegalAccessException {
		Set<String> assetsNames = new HashSet<String>();
		Field[] fields = GdxR.class.getFields();
		for (String folderName : category.folderNames) {
			for (Field field : fields) {
				if (field.getName().startsWith(folderName)) {
					if (field.getType() == String.class) {
						assetsNames.add(field.get(null).toString());
					} else {
						Gdx.app.log(tag, "获取 " + category + " 类的资源名字时，" + folderName + " 开头的字段对应的类型不是 String");
					}
				}
			}
		}
		assetsNames.addAll(category.assetNames);
		return assetsNames;
	}

	public enum AssetsCategory {
		login() {
			@Override
			public void initAssetNames() {
				// TODO
				// 类似 folderNames.add("login");
				// 类似 folderNames.add("data/login");
				// 类似 assetNames.add("data/xxx.png");
				// 类似 assetNames.add("data/xxxaa.jpg");
				// 类似 assetNames.add("data/xxx.ogg");
			}
		},
		hall() {
			@Override
			public void initAssetNames() {
				// TODO
				// 类似 folderNames.add("hall");
				// 类似 folderNames.add("data/hall");
				// 类似 assetNames.add("data/xxx.png");
				// 类似 assetNames.add("data/xxxaa.jpg");
				// 类似 assetNames.add("data/xxx.ogg");
			}
		},
		room() {
			@Override
			public void initAssetNames() {
				// TODO
				// 类似 folderNames.add("room");
				// 类似 folderNames.add("data/room");
				// 类似 assetNames.add("data/xxx.png");
				// 类似 assetNames.add("data/xxxaa.jpg");
				// 类似 assetNames.add("data/xxx.ogg");
			}
		},
		common() {
			@Override
			public void initAssetNames() {
				// TODO
				// 类似 folderNames.add("common");
				// 类似 folderNames.add("data/hall/common");
				// 类似 assetNames.add("data/xxx.png");
				// 类似 assetNames.add("data/xxxaa.jpg");
				// 类似 assetNames.add("data/xxx.ogg");
			}
		};

		// 资源文件夹名（该文件名是基于 GdxR.java 中的字段名。如 dataroom 对应的真实路径为 data/room）
		public Set<String> folderNames = new HashSet<String>();
		// 资源名（包含路径如：data/loading.bg）
		public Set<String> assetNames = new HashSet<String>();

		/** 获取除了当前类别外的其他资源类。common 除外 */
		public Set<AssetsCategory> generalOtherCategorty() {
			Set<AssetsCategory> categories = new HashSet<AssetsCategory>(Arrays.asList(AssetsCategory.values()));
			categories.remove(this);
			categories.remove(common);
			return categories;
		}

		public abstract void initAssetNames();
	}

	public static interface ILoadingComplete {
		boolean complete();
	}

}
