package com.badlogic.gdx.ingenuity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ingenuity.extend.ICoreHelper;

/**
 * @作者 Mitkey
 * @时间 2017年3月24日 下午2:38:43
 * @类说明:
 * @版本 xx
 */
public class GdxData {

	/** 是否为发布版本 */
	public static boolean RELEASE_VERSION = true;
	/** 是否监控追踪资源管理 */
	public static boolean TRACE_ASSET_MANAGER = false;

	/** 素材宽比例 */
	public static final int WIDTH = 1280;
	/** 素材高比例 */
	public static final int HEIGHT = 720;

	private static GdxData ourInstance = new GdxData();

	private ICoreHelper coreHelper;

	private GdxData() {
	}

	public static GdxData getInstance() {
		return ourInstance;
	}

	public void initRuntime(ICoreHelper coreHelper) {
		this.coreHelper = coreHelper;
	}

	@SuppressWarnings("unchecked")
	public <T extends GdxGame> T getAppGame() {
		return (T) Gdx.app.getApplicationListener();
	}

	public ICoreHelper getCoreHelper() {
		return coreHelper;
	}

}
