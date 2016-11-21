package com.toprunner.ubii.toprunner.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
/**
 * 保存软件参数
 * @author 
 *
 */
public class CacheUtils {
	private Context context;
	/**
	 * 保存在手机里面的文件名
	 */
	private String FILE_NAME = "share_date";
	private static SharedPreferences sp;
	public CacheUtils(Context context) {
		this.context=context;

	}
	/**
	 * 保存boolean类型的参数
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void putBoolean(Context context, String key, boolean value) {
		if (sp == null) {
			sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		}

		Editor editor = sp.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	/**
	 * 得到boolean类型的参数
	 * 
	 * @param context
	 * @param key
	 * @param
	 */
	public static boolean getBoolean(Context context, String key) {
		if (sp == null) {
			sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		}
		return sp.getBoolean(key, false);
	}

	/**
	 * 保存Stirng类型的参数
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void putString(Context context, String key, String value) {
		if (sp == null) {
			sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		}

		Editor editor = sp.edit();
		editor.putString(key, value);
		editor.commit();
	}

	/**
	 * 得到String类型的参数
	 * 
	 * @param context
	 * @param key
	 * @return
	 */
	public static String getString(Context context, String key) {
		if (sp == null) {
			sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		}
		return sp.getString(key, "");
	}
	/**
	 * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
	 *
	 * @param key
	 * @param object
	 */
	public void setParam(String key, Object object) {

		String type = object.getClass().getSimpleName();
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
				Context.MODE_MULTI_PROCESS);
		SharedPreferences.Editor editor = sp.edit();
		if ("String".equals(type)) {
			editor.putString(key,  object.toString());
		} else if ("Integer".equals(type)) {
			editor.putInt(key, (Integer) object);
		} else if ("Boolean".equals(type)) {
			editor.putBoolean(key, (Boolean) object);
		} else if ("Float".equals(type)) {
			editor.putFloat(key, (Float) object);
		} else if ("Long".equals(type)) {
			editor.putLong(key, (Long) object);
		}

		editor.commit();
	}

	/**
	 * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
	 *
	 * @param key
	 * @param defaultObject
	 * @return
	 */
	public Object getParam(String key, Object defaultObject) {
		String type = defaultObject.getClass().getSimpleName();
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);

		if ("String".equals(type)) {
			return sp.getString(key, (String) defaultObject);
		} else if ("Integer".equals(type)) {
			return sp.getInt(key, (Integer) defaultObject);
		} else if ("Boolean".equals(type)) {
			return sp.getBoolean(key, (Boolean) defaultObject);
		} else if ("Float".equals(type)) {
			return sp.getFloat(key, (Float) defaultObject);
		} else if ("Long".equals(type)) {
			return sp.getLong(key, (Long) defaultObject);
		}

		return null;
	}

	/**
	 * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
	 *
	 * @param key
	 * @return
	 */
	// Delete
	public void remove( String key) {
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.remove(key);
		editor.commit();
	}

	public void clear() {
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.clear();
		editor.commit();
	}
}
