package com.example.qiaoni.tvdemo;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class FileIOHelper {
	/**
	 * 保存在手机里面的文件名
	 */
	private static final String FILE_NAME = "channel";

	private static SharedPreferences sp;

	public static void init(SPFHelperListener listener){

	}

	private static SharedPreferences getSp() {
		if (sp == null) {
			synchronized (SharedPreferences.class) {
				if (sp == null) {
					sp = TvApp.getInstance().getApplicationContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
				}
			}
		}
		return sp;
	}

	public static void putString(String key, String value) {
		putValue(key, value, ShareEnum.STRING);
	}

	public static void putBoolean(String key, boolean value) {
		putValue(key, value, ShareEnum.BOOLEAN);
	}

	public static void putFloat(String key, float value) {
		putValue(key, value, ShareEnum.FLOAT);
	}

	public static void putInt(String key, int value) {
		putValue(key, value, ShareEnum.INT);
	}

	public static void putLong(String key, long value) {
		putValue(key, value, ShareEnum.LONG);
	}

	public static String getString(String key, String defValue) {
		return getSp().getString(key, defValue);
	}

	public static String getString(String key) {
		return getString(key, "");
	}

	public static float getFloat(String key, float defValue) {
		return getSp().getFloat(key, defValue);
	}

	public static float getFloat(String key) {
		return getFloat(key, 0);
	}

	public static boolean getBoolean(String key, boolean defValue) {
		return getSp().getBoolean(key, defValue);
	}

	public static boolean getBoolean(String key) {
		return getBoolean(key, false);
	}

	public static int getInt(String key, int defValue) {
		return getSp().getInt(key, defValue);
	}

	public static int getInt(String key) {
		return getInt(key, 0);
	}

	public static long getLong(String key, long defValue) {
		return getSp().getLong(key, defValue);
	}

	public static long getLong(String key) {
		return getLong(key, 0);
	}

	private static void putValue(String key, Object object, ShareEnum shareEnum) {
		if (object == null) {
			return;
		}
		Editor edit = getSp().edit();
		String vlaue = object.toString();
		switch (shareEnum) {
			case STRING:
				edit.putString(key, vlaue);
				break;
			case INT:
				edit.putInt(key, Integer.valueOf(vlaue));
				break;
			case BOOLEAN:
				edit.putBoolean(key, Boolean.valueOf(vlaue));
				break;
			case LONG:
				edit.putLong(key, Long.valueOf(vlaue));
				break;
			case FLOAT:
				edit.putFloat(key, Float.valueOf(vlaue));
				break;
		}
		edit.commit();
	}

	public static void clearData(String... keys) {
		Editor editor = getSp().edit();
		for (String key : keys) {
			editor.remove(key);
		}
		editor.commit();
	}

	private enum ShareEnum {
		INT, STRING, BOOLEAN, LONG, FLOAT
	}

	public interface SPFHelperListener{
			Context onInitListener();
	}

}
