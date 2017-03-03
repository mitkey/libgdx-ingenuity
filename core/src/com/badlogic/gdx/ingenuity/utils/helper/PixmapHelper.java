package com.badlogic.gdx.ingenuity.utils.helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Entries;
import com.badlogic.gdx.utils.ObjectMap.Entry;

/**
 * @作者 Mitkey
 * @时间 2016年10月28日 下午3:57:42
 * @类说明:
 * @版本 xx
 */
public class PixmapHelper implements Disposable {

	private static final String tag = PixmapHelper.class.getSimpleName();

	private static PixmapHelper ourInstance = new PixmapHelper();

	ObjectMap<String, Texture> textureObjectMap = new ObjectMap<String, Texture>();

	public static PixmapHelper getInstance() {
		return ourInstance;
	}

	private PixmapHelper() {
	}

	/** 新建一个矩形纯色的 Drawable */
	public Drawable newRectangleDrawable(Color color, int width, int height) {
		return newDrawable(getRectangleTexture(color, width, height));
	}

	/** 新建一个圆形纯色的 Drawable */
	public Drawable newCircleDrawable(Color color, int radius) {
		return newDrawable(getCircleTexture(color, radius));
	}

	/** 新建一个半透明的 Drawable */
	public Drawable newTranslucentDrawable(int width, int height) {
		Color color = Color.BLACK;
		color.a = .5f;
		return newDrawable(getRectangleTexture(color, width, height));
	}

	public Texture getRectangleTexture(Color color, int width, int height) {
		String parameter2Key = parameter2Key(color, width, height);
		if (textureObjectMap.containsKey(parameter2Key)) {
			return textureObjectMap.get(parameter2Key);
		}
		// 画布
		Pixmap pixmap = new Pixmap(width, height, Format.RGBA8888);
		pixmap.setColor(color);
		pixmap.fill();
		// 纹理
		Texture texture = new Texture(pixmap);
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		pixmap.dispose();
		// 缓存
		textureObjectMap.put(parameter2Key, texture);
		Gdx.app.log(tag, "创建矩形纯色纹理 --> " + parameter2Key);
		return texture;
	}

	public Texture getCircleTexture(Color color, int radius) {
		String parameter2Key = parameter2Key(color, radius);
		if (textureObjectMap.containsKey(parameter2Key)) {
			return textureObjectMap.get(parameter2Key);
		}
		// 画布
		int size = radius * 2;
		Pixmap pixmap = new Pixmap(size, size, Format.RGBA8888);
		pixmap.setColor(color);
		pixmap.fillCircle(radius, radius, radius);
		// 纹理
		Texture texture = new Texture(pixmap);
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		// 缓存
		textureObjectMap.put(parameter2Key, texture);
		Gdx.app.log(tag, "创建圆形纯色纹理 --> " + parameter2Key);
		return texture;
	}

	@Override
	public void dispose() {
		Entries<String, Texture> iterator = textureObjectMap.entries().iterator();
		while (iterator.hasNext()) {
			Entry<String, Texture> entry = iterator.next();
			entry.value.dispose();
			iterator.remove();
			Gdx.app.log(tag, "释放资源 --> " + entry.key);
		}
	}

	Drawable newDrawable(Texture texture) {
		return new TextureRegionDrawable(new TextureRegion(texture));
	}

	String parameter2Key(Object... objects) {
		StringBuilder builder = new StringBuilder();
		for (Object object : objects) {
			builder.append(object);
		}
		return builder.toString();
	}

}
