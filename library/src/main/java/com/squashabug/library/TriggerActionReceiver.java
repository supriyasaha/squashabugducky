package com.squashabug.library;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import com.squashabug.library.database.DatabaseHelper;

public class TriggerActionReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {

	}

	public void deleteDataFromTable(){
		SQLiteDatabase db = DatabaseHelper.getInstance().getDatabseManipulater();

	}
}
