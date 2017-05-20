package com.badlogic.gdx.ingenuity.utils;

import java.awt.Desktop;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.function.BiConsumer;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.glutils.ETC1TextureData;
import com.badlogic.gdx.graphics.glutils.FileTextureData;
import com.badlogic.gdx.graphics.glutils.FloatTextureData;
import com.badlogic.gdx.graphics.glutils.GLOnlyTextureData;
import com.badlogic.gdx.graphics.glutils.KTXTextureData;
import com.badlogic.gdx.graphics.glutils.MipMapTextureData;
import com.badlogic.gdx.graphics.glutils.PixmapTextureData;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Field;
import com.badlogic.gdx.utils.reflect.ReflectionException;

/**
 * @作者 mitkey
 * @时间 2017年5月17日 上午10:44:42
 * @类说明 Utils.java <br/>
 * @版本 0.0.1
 */
public class Utils {

	private static final String tag = Utils.class.getName();

	private static final boolean savePng = false;

	/** 打开某个文件或目录，若是文件则 pc 必须有关联打开的程序 */
	public static void openFileExplorer(FileHandle startDirectory) throws IOException {
		Objects.requireNonNull(startDirectory, "FileHandle 不能为空");
		if (startDirectory.exists()) {
			Desktop.getDesktop().open(startDirectory.file());
		} else {
			throw new IOException("Directory doesn't exist: " + startDirectory.path());
		}
	}

	/** 打印代码调用栈追踪信息 */
	public static void printCodeInvokingStackTrace() {
		new Throwable("代码调用栈追踪信息 ---> ").printStackTrace();
	}

	/** 打印已加载管理的纹理 */
	public static void printManagedTextures() {
		try {
			Field declaredField = ClassReflection.getDeclaredField(Texture.class, "managedTextures");
			declaredField.setAccessible(true);

			@SuppressWarnings("unchecked")
			Map<Application, Array<Texture>> managedTextures = (Map<Application, Array<Texture>>) declaredField.get(null);
			BiConsumer<Application, Array<Texture>> action = new BiConsumer<Application, Array<Texture>>() {
				@Override
				public void accept(Application t, Array<Texture> u) {
					for (Texture texture : u) {
						TextureData textureData = texture.getTextureData();
						if (textureData instanceof ETC1TextureData) {
							Gdx.app.log(tag, "managedTexture ETC1TextureData --> " + textureData);
						} else if (textureData instanceof FileTextureData) {
							FileTextureData temp = (FileTextureData) textureData;
							Gdx.app.log(tag, "managedTexture FileTextureData --> " + temp.getFileHandle());
						} else if (textureData instanceof FloatTextureData) {
							Gdx.app.log(tag, "managedTexture FloatTextureData --> " + textureData);
						} else if (textureData instanceof GLOnlyTextureData) {
							Gdx.app.log(tag, "managedTexture GLOnlyTextureData --> " + textureData);
						} else if (textureData instanceof KTXTextureData) {
							Gdx.app.log(tag, "managedTexture KTXTextureData --> " + textureData);
						} else if (textureData instanceof MipMapTextureData) {
							Gdx.app.log(tag, "managedTexture MipMapTextureData --> " + textureData);
						} else if (textureData instanceof PixmapTextureData) {
							Gdx.app.log(tag, "managedTexture PixmapTextureData --> " + textureData);

							if (savePng) {
								PixmapTextureData textureData2 = (PixmapTextureData) textureData;
								Pixmap consumePixmap = textureData2.consumePixmap();
								PixmapIO.writePNG(Gdx.files.external("gdxTest/" + System.currentTimeMillis() + new Random().nextInt(15) + ".png"), consumePixmap);
							}
						} else {
							Gdx.app.log(tag, "managedTexture --> unkonw type " + textureData);
						}
					}
				}
			};
			managedTextures.forEach(action);
		} catch (ReflectionException e) {
			Gdx.app.error(tag, "printManagedTextures error", e);
		}
	}

}
