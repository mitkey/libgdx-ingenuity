package com.badlogic.gdx.ingenuity.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.ingenuity.GdxData;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;

public enum GdxUtilities {
	;

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
