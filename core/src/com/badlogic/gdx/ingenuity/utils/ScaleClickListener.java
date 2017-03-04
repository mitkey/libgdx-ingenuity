package com.badlogic.gdx.ingenuity.utils;

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

	@Override
	public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
		Actor listenerActor = event.getListenerActor();
		listenerActor.setOrigin(Align.center);
		listenerActor.setScale(1.1f);
		return super.touchDown(event, x, y, pointer, button);
	}

	@Override
	public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
		Actor listenerActor = event.getListenerActor();
		listenerActor.setOrigin(Align.center);
		listenerActor.setScale(1f);
		super.touchUp(event, x, y, pointer, button);
	}

}
