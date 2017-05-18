package com.badlogic.gdx.ingenuity.utils.scene2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.ingenuity.GlobalData;
import com.badlogic.gdx.ingenuity.IngenuityGdx;
import com.badlogic.gdx.ingenuity.utils.GdxUtil;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.github.czyzby.kiwi.util.gdx.GdxUtilities;

/**
 * @作者 Mitkey
 * @时间 2016年9月1日 上午11:19:05
 * @类说明:
 * @版本 xx
 */
public abstract class SimpleScreen extends ScreenAdapter implements InputProcessor {

	Stage stage;
	Loading loading;
	SimpleToast simpleToast;

	@Override
	public void show() {
		super.show();
		this.stage = new Stage(new StretchViewport(GlobalData.WIDTH, GlobalData.HEIGHT), game().getSpriteBatch());
		this.loading = new Loading();
		this.simpleToast = new SimpleToast();

		GdxUtilities.setMultipleInputProcessors(stage, this);
	}

	@Override
	public void render(float delta) {
		super.render(delta = Math.min(delta, 1.0f / 30.0f));
		Gdx.gl20.glClearColor(.5f, .5f, .2f, 1);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
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

	public void showLoading() {
		loading.show(stage);
	}

	public void hideLoading() {
		loading.hide();
	}

	public void showMesssage(String content) {
		simpleToast.showToast(stage, content);
	}

	public void showDialog(Group group) {
		stage.addActor(group);
		group.addAction(Actions.alpha(0));
		group.addAction(Actions.scaleTo(.8f, .8f));
		group.addAction(Actions.alpha(1, .2f));
		group.addAction(Actions.scaleTo(1f, 1f, .2f));
	}

	public Image newImage(String fileName) {
		return game().getAssetManager().newImage(fileName);
	}

	public Drawable newDrawable(String fileName) {
		return game().getAssetManager().newDrawable(fileName);
	}

	public final Stage stage() {
		return stage;
	}

	public final IngenuityGdx game() {
		return GdxUtil.getAppGame();
	}

}
