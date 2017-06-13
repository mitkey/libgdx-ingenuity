package com.badlogic.gdx.ingenuity.utils;

import java.awt.Desktop;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.function.BiConsumer;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ETC1TextureData;
import com.badlogic.gdx.graphics.glutils.FileTextureData;
import com.badlogic.gdx.graphics.glutils.FloatTextureData;
import com.badlogic.gdx.graphics.glutils.GLOnlyTextureData;
import com.badlogic.gdx.graphics.glutils.KTXTextureData;
import com.badlogic.gdx.graphics.glutils.MipMapTextureData;
import com.badlogic.gdx.graphics.glutils.PixmapTextureData;
import com.badlogic.gdx.ingenuity.GdxData;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Field;
import com.badlogic.gdx.utils.reflect.ReflectionException;

public enum GdxUtilities {
	;
	private static final String tag = GdxUtilities.class.getSimpleName();

	/** 尝试播放次数 */
	private static final int TRY_PLAY_COUNT = 5;

	/** 是否把 asset manager 管理的纹理保存为 png ---> 仅用于开发阶段，发布时该值一定需要为 false */
	private static final boolean SAVE_2_PNG = false;

	/** 打印代码调用栈追踪信息 */
	public static void printCodeInvokingStackTrace() {
		new Throwable("代码调用栈追踪信息 ---> ").printStackTrace();
	}

	/** 苛刻的播放 */
	public static void harshPlay(Sound sound) {
		if (sound != null) {
			int count = 0;
			while (sound.play() == -1 && count <= TRY_PLAY_COUNT) {
				count++;
			}
		}
	}

	/** 打开某个文件或目录，若是文件则 pc 必须有关联打开的程序 */
	public static void openFileExplorer(FileHandle startDirectory) throws IOException {
		Objects.requireNonNull(startDirectory, "FileHandle 不能为空");
		if (startDirectory.exists()) {
			Desktop.getDesktop().open(startDirectory.file());
		} else {
			throw new IOException("Directory doesn't exist: " + startDirectory.path());
		}
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

							if (SAVE_2_PNG) {
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

	public static void clearScreen() {
		Gdx.gl20.glClearColor(.5f, .5f, .2f, 1);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}

	public static Vector2 getCursorPosition() {
		return new Vector2(Gdx.input.getX(), Gdx.input.getY());
	}

	public static void clearInputProcessor() {
		Gdx.input.setInputProcessor(null);
	}

	public static void setMultipleInputProcessors(final InputProcessor... processors) {
		Gdx.input.setInputProcessor(new InputMultiplexer(processors));
	}

	public static Rectangle getTextBounds(String text, BitmapFont bitmapFont) {
		GlyphLayout glyphLayout = new GlyphLayout();
		glyphLayout.setText(bitmapFont, text);
		return new Rectangle().setSize(glyphLayout.width, glyphLayout.height);
	}

	public static Rectangle getWarppedTextBounds(String text, float wrapWidth, BitmapFont bitmapFont) {
		GlyphLayout glyphLayout = new GlyphLayout();
		glyphLayout.setText(bitmapFont, text, bitmapFont.getColor(), wrapWidth, Align.left, true);
		return new Rectangle().setSize(glyphLayout.width, glyphLayout.height);
	}

	public static <T extends Actor> T center(T actor, boolean horizontal) {
		if (horizontal) {
			float wd = GdxData.WIDTH - actor.getWidth();
			actor.setX(wd / 2);
		} else {
			float ht = GdxData.HEIGHT - actor.getHeight();
			actor.setY(ht / 2);
		}
		return actor;
	}

	public static <T extends Actor> T center(T actor) {
		float wd = GdxData.WIDTH - actor.getWidth();
		float ht = GdxData.HEIGHT - actor.getHeight();
		actor.setPosition(wd / 2, ht / 2);
		return actor;
	}

	public static void center(Actor staticActhor, Actor actor) {
		actor.setPosition(staticActhor.getX() + (staticActhor.getWidth() - actor.getWidth()) / 2, staticActhor.getY() + (staticActhor.getHeight() - actor.getHeight()) / 2);
	}

	public static void replace(Actor oldActor, Actor newActor) {
		if (oldActor != null && oldActor.getParent() != null) {
			newActor.setBounds(oldActor.getX(), oldActor.getY(), oldActor.getWidth(), oldActor.getHeight());
			oldActor.getParent().addActorBefore(oldActor, newActor);
			oldActor.remove();
		}
	}

}
