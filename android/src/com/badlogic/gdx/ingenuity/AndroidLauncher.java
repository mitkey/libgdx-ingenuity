package com.badlogic.gdx.ingenuity;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.ingenuity.GdxGame;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new GdxGame(), config);
	}
	
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		android.os.Process.killProcess(android.os.Process.myPid());
	}
	
}
