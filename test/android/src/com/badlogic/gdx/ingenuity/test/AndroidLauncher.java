package com.badlogic.gdx.ingenuity.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import android.os.Bundle;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new TestGdxGame(), config);
		Gdx.app.setLogLevel(LOG_DEBUG);
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		android.os.Process.killProcess(android.os.Process.myPid());
	}

}
