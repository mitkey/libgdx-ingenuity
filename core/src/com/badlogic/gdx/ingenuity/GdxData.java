package com.badlogic.gdx.ingenuity;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.ingenuity.extend.ICoreHelper;

import net.mwplay.nativefont.NativeFont;
import net.mwplay.nativefont.NativeFontPaint;

/**
 * @作者 Mitkey
 * @时间 2017年3月24日 下午2:38:43
 * @类说明:
 * @版本 xx
 */
public class GdxData {

	/** 开启调试已加载纹理 */
	public static boolean DEBUG_MANAGED_TEXTURES = true;

	/** 素材宽比例 */
	public static final int WIDTH = 1280;
	/** 素材高比例 */
	public static final int HEIGHT = 720;

	private static GdxData ourInstance = new GdxData();

	private ICoreHelper coreHelper;

	/** 默认的字体 */
	private Map<Integer, NativeFont> DEFAULT_FONTS = new HashMap<Integer, NativeFont>();

	private GdxData() {
	}

	public static GdxData getInstance() {
		return ourInstance;
	}

	public void initRuntime(ICoreHelper coreHelper) {
		this.coreHelper = coreHelper;
	}

	public NativeFont getFont(int size) {
		if (DEFAULT_FONTS.containsKey(size)) {
			return DEFAULT_FONTS.get(size);
		} else {
			return DEFAULT_FONTS.put(size, new NativeFont(new NativeFontPaint(size)).appendText(FreeTypeFontGenerator.DEFAULT_CHARS));
		}
	}

	public ICoreHelper getCoreHelper() {
		return coreHelper;
	}

}
