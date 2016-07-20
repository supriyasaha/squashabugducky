package com.squashabug.library;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UtilClass {

	public static final String APP_LIST = "list";

	public static void saveDataToSharedPref(Context context, List<String> arrayList) {
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sharedPrefs.edit();
		Gson gson = new Gson();

		String json = gson.toJson(arrayList);

		editor.putString(APP_LIST, json);
		editor.commit();
	}

	public static List<String> getPackage(Context context) {
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		Gson gson = new Gson();
		String json = sharedPrefs.getString(APP_LIST, null);
		Type type = new TypeToken<ArrayList<String>>() {
		}.getType();
		return gson.fromJson(json, type);
	}
}
