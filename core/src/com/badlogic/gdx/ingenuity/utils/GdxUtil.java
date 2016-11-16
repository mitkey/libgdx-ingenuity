package com.badlogic.gdx.ingenuity.utils;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;

public enum GdxUtil {
	;

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

	public static void center(Stage stage, Actor actor) {
		float wd = stage.getWidth() - actor.getWidth();
		float ht = stage.getHeight() - actor.getHeight();
		actor.setPosition(wd / 2, ht / 2);
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
