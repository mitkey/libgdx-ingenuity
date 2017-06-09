package com.badlogic.gdx.ingenuity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ingenuity.extend.ICoreHelper;
import com.badlogic.gdx.ingenuity.extend.desktop.DesktopCoreHelper;

import net.mwplay.nativefont.NativeFont;
import net.mwplay.nativefont.NativeFontPaint;

/**
 * @作者 Mitkey
 * @时间 2017年3月24日 下午2:38:43
 * @类说明:
 * @版本 xx
 */
public class GdxData {

	private static final String tag = GdxData.class.getName();

	/** 是否为发布版本 */
	public static boolean RELEASE_VERSION = true;

	/** 素材宽比例 */
	public static final int WIDTH = 1280;
	/** 素材高比例 */
	public static final int HEIGHT = 720;

	private static GdxData ourInstance = new GdxData();

	private ICoreHelper coreHelper;

	/** 默认的字体 */
	private Map<Integer, NativeFont> defaultFonts = new HashMap<Integer, NativeFont>();

	private GdxData() {
		if (Gdx.app.getType() == ApplicationType.Desktop && this.coreHelper == null) {
			initRuntime(new DesktopCoreHelper());
		}
	}

	public static GdxData getInstance() {
		return ourInstance;
	}

	public void initRuntime(ICoreHelper coreHelper) {
		this.coreHelper = coreHelper;
	}

	public NativeFont getFont(int size) {
		if (defaultFonts.containsKey(size)) {
			return defaultFonts.get(size);
		} else {
			NativeFont nativeFont = new NativeFont(new NativeFontPaint(size));
			defaultFonts.put(size, nativeFont);
			return nativeFont;
		}
	}

	public void disposeFont() {
		Gdx.app.log(tag, "释放 NativeFont 数量:" + defaultFonts.size());
		Iterator<NativeFont> iterator = defaultFonts.values().iterator();
		while (iterator.hasNext()) {
			NativeFont font = iterator.next();
			font.dispose();
			iterator.remove();
		}
		defaultFonts.clear();
	}

	public ICoreHelper getCoreHelper() {
		return coreHelper;
	}

}
