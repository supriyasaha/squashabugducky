package com.squashabug.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import com.squashabug.app.database.DatabaseHelper;

public class TriggerActionReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {

	}

	public void deleteDataFromTable(){
		SQLiteDatabase db = DatabaseHelper.getInstance().getDatabseManipulater();

	}
}
