package com.badlogic.gdx.ingenuity;

import com.badlogic.gdx.ingenuity.extend.ICoreHelper;

/**
 * @作者 Mitkey
 * @时间 2017年3月24日 下午2:38:43
 * @类说明:
 * @版本 xx
 */
public class GdxData {

	/** 是否自动生成 R 文件 */
	public static boolean AUTO_GENERATE_GDX_R = false;

	/** 开启调试已加载纹理 */
	public static boolean DEBUG_MANAGED_TEXTURES = true;

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

	public ICoreHelper getCoreHelper() {
		return coreHelper;
	}

}
