package com.badlogic.gdx.ingenuity.scene2d;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.ingenuity.GdxData;
import com.badlogic.gdx.ingenuity.utils.GdxUtilities;
import com.badlogic.gdx.ingenuity.utils.OnlyAssetManager;
import com.badlogic.gdx.ingenuity.utils.common.StrUtil;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import net.mwplay.nativefont.NativeButton;
import net.mwplay.nativefont.NativeFont;
import net.mwplay.nativefont.NativeLabel;
import net.mwplay.nativefont.NativeTextField;

/**
 * @作者 Mitkey
 * @时间 2016年9月1日 上午11:19:05
 * @类说明:
 * @版本 xx
 */
public abstract class SimpleScreen extends ScreenAdapter implements InputProcessor {

	private static OnlyAssetManager ONLY_ASSET_MANAGER;
	private static SpriteBatch SPRITE_BATCH;

	Stage stage;
	Loading loading;
	SimpleToast simpleToast;

	public static void initContext() {
		if (SPRITE_BATCH == null) {
			SPRITE_BATCH = new SpriteBatch();
		}
		if (ONLY_ASSET_MANAGER == null) {
			ONLY_ASSET_MANAGER = new OnlyAssetManager();
		}
	}

	@Override
	public void show() {
		super.show();
		this.stage = new Stage(new StretchViewport(GdxData.WIDTH, GdxData.HEIGHT), SPRITE_BATCH);
		this.loading = new Loading();
		this.simpleToast = new SimpleToast();

		GdxUtilities.setMultipleInputProcessors(stage, this);
	}

	@Override
	public void render(float delta) {
		super.render(delta = Math.min(delta, 1.0f / 30.0f));
		GdxUtilities.clearScreen();
		stage.draw();
		stage.act(delta);
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void dispose() {
		super.dispose();
		stage.dispose();
		loading.dispose();
		simpleToast.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	public static SpriteBatch spriteBatch() {
		return SPRITE_BATCH;
	}

	public static OnlyAssetManager onlyAssetManager() {
		return ONLY_ASSET_MANAGER;
	}

	public static void disposeStatic() {
		if (SPRITE_BATCH != null) {
			SPRITE_BATCH.dispose();
			SPRITE_BATCH = null;
		}
		if (ONLY_ASSET_MANAGER != null) {
			ONLY_ASSET_MANAGER.dispose();
			ONLY_ASSET_MANAGER = null;
		}
	}

	// utils =============== start
	public void showLoading() {
		loading.show(stage);
	}

	public void hideLoading() {
		loading.hide();
	}

	public void showMesssage(String content) {
		simpleToast.showToast(this, content);
	}

	public void showDialog(Group group) {
		stage.addActor(group);
		group.addAction(Actions.alpha(0));
		group.addAction(Actions.scaleTo(.8f, .8f));
		group.addAction(Actions.alpha(1, .2f));
		group.addAction(Actions.scaleTo(1f, 1f, .2f));
	}
	// utils =============== end

	// native ============== start
	public NativeFont newNativeFont(int size) {
		return GdxData.getInstance().getFont(size);
	}

	public NativeLabel newNativeLabel(CharSequence text, int fontSize) {
		return new NativeLabel(text, newNativeFont(fontSize));
	}

	public NativeLabel newNativeLabel(CharSequence text, int fontSize, Color color) {
		return new NativeLabel(text, newNativeFont(fontSize), color);
	}

	public NativeLabel newNativeLabel(CharSequence text, NativeFont font, Color color) {
		return new NativeLabel(text, font, color);
	}

	public NativeLabel newNativeLabel(CharSequence text, NativeFont style) {
		return new NativeLabel(text, style);
	}

	public NativeButton newNativeButton(String text, TextButton.TextButtonStyle style) {
		return new NativeButton(text, style);
	}

	public NativeButton newNativeButton(String text, Drawable up, Drawable down, Drawable checked, int fontSize) {
		return new NativeButton(text, new TextButtonStyle(up, down, checked, newNativeFont(fontSize)));
	}

	public NativeButton newNativeButton(String text, String upFileName, String downFileName, String checkedFileName, int fontSize) {
		return newNativeButton(text, newDrawable(upFileName), //
				StrUtil.isBlank(downFileName) ? null : newDrawable(downFileName), //
				StrUtil.isBlank(checkedFileName) ? null : newDrawable(checkedFileName), fontSize);
	}

	public NativeButton newNativeButton(String text, String upFileName, String downFileName, String checkedFileName, NativeFont font) {
		return newNativeButton(text,
				new TextButtonStyle(newDrawable(upFileName), //
						StrUtil.isBlank(downFileName) ? null : newDrawable(downFileName), //
						StrUtil.isBlank(checkedFileName) ? null : newDrawable(checkedFileName), font));
	}

	public NativeTextField newNativeTextField(String text, TextFieldStyle style) {
		return new NativeTextField(text, style);
	}

	public NativeTextField newNativeTextField(String text, BitmapFont font, Color fontColor, Drawable cursor, Drawable selection, Drawable background) {
		return new NativeTextField(text, new TextFieldStyle(font, fontColor, cursor, selection, background));
	}

	public NativeTextField newNativeTextField(String text, int fontSize, Color fontColor, String cursorFileName, String selectionFileName, String backgroundFileName) {
		return new NativeTextField(text,
				new TextFieldStyle(newNativeFont(fontSize), fontColor, //
						StrUtil.isBlank(cursorFileName) ? null : newDrawable(cursorFileName), //
						StrUtil.isBlank(selectionFileName) ? null : newDrawable(selectionFileName), //
						StrUtil.isBlank(backgroundFileName) ? null : newDrawable(backgroundFileName)));
	}
	// native ============== end

	// other ============== start
	public ImageButton newImageButton(String upFileName, String downFileName, String checkedFileName) {
		return newImageButton(newDrawable(upFileName), //
				StrUtil.isBlank(downFileName) ? null : newDrawable(downFileName), //
				StrUtil.isBlank(checkedFileName) ? null : newDrawable(checkedFileName));
	}

	public ImageButton newImageButton(Drawable imageUp, Drawable imageDown, Drawable imageChecked) {
		return new ImageButton(imageUp, imageDown, imageChecked);
	}

	public Image newImage(String fileName) {
		return new Image(newDrawable(fileName));
	}

	public Image newImage(Drawable drawable) {
		return new Image(drawable);
	}

	public Drawable newDrawable(String fileName) {
		return onlyAssetManager().newDrawable(fileName);
	}

	// other ============== end

	public final Stage stage() {
		return stage;
	}

}
