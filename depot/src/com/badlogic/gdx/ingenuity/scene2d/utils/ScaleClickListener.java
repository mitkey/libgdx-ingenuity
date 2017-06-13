package com.badlogic.gdx.ingenuity.scene2d.utils;

import java.util.Objects;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

/**
 * @作者 Mitkey
 * @时间 2015-8-4 下午02:36:26
 * @类说明:
 * @版本 xx
 */
public class ScaleClickListener extends ClickListener {

	/** 按下时的缩放比例 */
	private float downScale;
	/** 抬起时的缩放比例 */
	private float upScale;

	public ScaleClickListener(float downScale, float upScale) {
		this.downScale = downScale;
		this.upScale = upScale;
	}

	public static <T extends Actor> T register(T actor) {
		return register(actor, 0.96f, 1f);
	}

	public static <T extends Actor> T register(T actor, float downScale, float upScale) {
		Objects.requireNonNull(actor, "register actor must not be null");
		actor.setOrigin(Align.center);
		actor.addListener(new ScaleClickListener(downScale, upScale));
		return actor;
	}

	@Override
	public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
		Actor listenerActor = event.getListenerActor();
		listenerActor.setScale(downScale);
		return super.touchDown(event, x, y, pointer, button);
	}

	@Override
	public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
		Actor listenerActor = event.getListenerActor();
		listenerActor.setScale(upScale);
		super.touchUp(event, x, y, pointer, button);
	}

}
