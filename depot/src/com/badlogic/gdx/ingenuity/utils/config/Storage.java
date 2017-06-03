package com.badlogic.gdx.ingenuity.utils.config;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public enum Storage {
	;
	public static String getStr(String key, String defValue) {
		return getSharedPreferences().getString(key, defValue);
	}

	public static void saveStr(String key, String val) {
		getSharedPreferences().putString(key, val).flush();
	}

	public static boolean getBoolean(String key, boolean defValue) {
		return getSharedPreferences().getBoolean(key, defValue);
	}

	public static void saveBoolean(String key, boolean val) {
		getSharedPreferences().putBoolean(key, val).flush();
	}

	public static int getInt(String key, int defValue) {
		return getSharedPreferences().getInteger(key, defValue);
	}

	public static void saveInt(String key, int val) {
		getSharedPreferences().putInteger(key, val).flush();
	}

	public static long getLong(String key, long defValue) {
		return getSharedPreferences().getLong(key, defValue);
	}

	public static void saveLong(String key, long val) {
		getSharedPreferences().putLong(key, val).flush();
	}

	public static float getFloat(String key, float defValue) {
		return getSharedPreferences().getFloat(key, defValue);
	}

	public static void saveFloat(String key, float val) {
		getSharedPreferences().putFloat(key, val).flush();
	}

	public static void delete(String key) {
		Preferences prefs = getSharedPreferences();
		prefs.remove(key);
		prefs.flush();
	}

	static Preferences getSharedPreferences() {
		return Gdx.app.getPreferences("Preferences");
	}

}
