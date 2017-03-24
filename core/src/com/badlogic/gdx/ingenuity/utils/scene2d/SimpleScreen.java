package com.badlogic.gdx.ingenuity.utils.scene2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.ingenuity.IngenuityGdx;
import com.badlogic.gdx.ingenuity.utils.GdxUtil;
import com.badlogic.gdx.ingenuity.utils.LazyBitmapFont;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.github.czyzby.kiwi.util.gdx.GdxUtilities;

/**
 * @作者 Mitkey
 * @时间 2016年9月1日 上午11:19:05
 * @类说明:
 * @版本 xx
 */
public abstract class SimpleScreen extends ScreenAdapter {

	public static int GameWidth = 1280;
	public static int GameHeight = 720;

	IngenuityGdx game;
	Stage stage;
	BitmapFont font;

	Loading loading;
	SimpleToast simpleToast;

	Table tableMontitor;

	@Override
	public void show() {
		super.show();
		this.game = GdxUtil.getAppGame();
		this.stage = new Stage(new StretchViewport(GameWidth, GameHeight));

		this.font = new LazyBitmapFont(18);

		this.loading = new Loading(this);
		this.simpleToast = new SimpleToast();

		initMontitorWidget();

		GdxUtilities.setMultipleInputProcessors(stage, new InputAdapter() {
			@Override
			public boolean keyUp(int keycode) {
				// TODO Auto-generated method stub
				return super.keyUp(keycode);
			}
		});
	}

	private void initMontitorWidget() {
		LabelStyle labelStyle = new LabelStyle(font, Color.WHITE);
		NumberLabel<Integer> labFps = new NumberLabel<Integer>("Fps: ", -1, labelStyle) {
			@Override
			public Integer getValue() {
				return Gdx.graphics.getFramesPerSecond();
			}
		};
		NumberLabel<Float> labHeap = new NumberLabel<Float>("Heap: ", -1f, labelStyle) {
			@Override
			public Float getValue() {
				return Gdx.app.getJavaHeap() * 1f / 1024 / 1024;
			}
		};
		NumberLabel<Float> labNative = new NumberLabel<Float>("Native: ", -1f, labelStyle) {
			@Override
			public Float getValue() {
				return Gdx.app.getNativeHeap() * 1f / 1024 / 1024;
			}
		};

		tableMontitor = new Table();
		tableMontitor.defaults().width(110).left().pad(5);
		tableMontitor.add(labFps).row();
		tableMontitor.add(labHeap).row();
		tableMontitor.add(labNative).row();
		tableMontitor.setPosition(10, 10);
		tableMontitor.pack();
		tableMontitor.setTouchable(Touchable.disabled);
	}

	@Override
	public void render(float delta) {
		super.render(delta = Math.min(delta, 1.0f / 30.0f));
		Gdx.gl20.glClearColor(.5f, .5f, .5f, 1);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.draw();
		stage.act(delta);

		Batch batch = this.stage.getBatch();
		if (batch != null) {
			batch.begin();
			tableMontitor.draw(batch, 1f);
			tableMontitor.act(delta);
			batch.end();
		}
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void dispose() {
		super.dispose();
		if (stage != null) {
			stage.dispose();
			stage = null;
		}
		if (font != null) {
			font.dispose();
			font = null;
		}
		if (loading != null) {
			loading.dispose();
			loading = null;
		}
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
		return game.getAssetManager().newImage(fileName);
	}

	public Drawable newDrawable(String fileName) {
		return game.getAssetManager().newDrawable(fileName);
	}

	public final Stage stage() {
		return stage;
	}

	public final IngenuityGdx game() {
		return game;
	}

}
