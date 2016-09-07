package com.badlogic.gdx.ingenuity;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.ingenuity.utils.LazyBitmapFont;
import com.badlogic.gdx.ingenuity.utils.scene2d.GeneralScreen;

public class IngenuityGdx extends Game {

	FreeTypeFontGenerator fontGenerator;
	GeneralScreen ingenuityScreen;

	@Override
	public void create() {
		LazyBitmapFont.setGlobalGenerator(fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf")));

		ingenuityScreen = new IngenuityScreen();
		setScreen(ingenuityScreen);
	}

	@Override
	public void dispose() {
		super.dispose();
		fontGenerator.dispose();
		ingenuityScreen.dispose();
	}

}
