package com.badlogic.gdx.ingenuity;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.ingenuity.utils.AssetsMgr;
import com.badlogic.gdx.ingenuity.utils.LazyBitmapFont;
import com.badlogic.gdx.ingenuity.utils.helper.RHelper;
import com.badlogic.gdx.ingenuity.utils.scene2d.GeneralScreen;

public class IngenuityGdx extends Game {

	FreeTypeFontGenerator fontGenerator;
	AssetManager assetManager;
	GeneralScreen ingenuityScreen;

	@Override
	public void create() {
		LazyBitmapFont.setGlobalGenerator(fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf")));
		AssetsMgr.initManager(assetManager = new AssetManager());

		// 生成 R.java 文件
		if (Gdx.app.getType() == ApplicationType.Desktop) {
			RHelper.autoWriteRes();
		}

		ingenuityScreen = new IngenuityScreen();
		setScreen(ingenuityScreen);
	}

	@Override
	public void dispose() {
		super.dispose();
		fontGenerator.dispose();
		assetManager.dispose();
		ingenuityScreen.dispose();
	}

}
