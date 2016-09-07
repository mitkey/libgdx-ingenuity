package com.badlogic.gdx.ingenuity.utils.scene2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;

/**
 * @作者 Mitkey
 * @时间 2016年9月1日 上午11:19:05
 * @类说明:
 * @版本 xx
 */
public abstract class GeneralScreen extends ScreenAdapter {

	public static int GameWidth = 1280;
	public static int GameHeight = 720;

	Stage stage;

	@Override
	public void show() {
		super.show();
		this.stage = new Stage(new StretchViewport(GameWidth, GameHeight));
	}

	@Override
	public final void render(float delta) {
		super.render(delta = Math.min(delta, 1.0f / 30.0f));
		Gdx.gl20.glClearColor(.5f, .5f, .5f, 1);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.draw();
		stage.act(delta);

		update(delta);
		draw(delta);
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
	}

	public final Stage stage() {
		return stage;
	}

	protected abstract void update(float delta);

	protected abstract void draw(float delta);

}
