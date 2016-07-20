package com.squashabug.library;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.squashabug.library.database.DatabaseHelper;
import com.squashabug.library.database.DebugDatabase;

import java.util.ArrayList;
import java.util.List;

public class DebugDuckyService extends Service {

	public boolean unbind = false;

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d("service started", "quack quack");

	}

	private final DebugAIDL.Stub stub = new DebugAIDL.Stub() {
		@Override
		public void getEventLog(final String eventName, String message, String packageName) throws RemoteException {
			new Handler(Looper.getMainLooper()).post(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(getApplicationContext(), eventName, Toast.LENGTH_SHORT).show();
				}
			});
			SQLiteDatabase db = DatabaseHelper.getInstance().getDatabseManipulater();

			ContentValues values = new ContentValues();
			values.put(DebugDatabase.EventsEntryTable.EVENT_NAME, eventName);
			values.put(DebugDatabase.EventsEntryTable.PROPERTIES, message);
			values.put(DebugDatabase.EventsEntryTable.PACKAGE_NAME, packageName);
			values.put(DebugDatabase.EventsEntryTable.TIMESTAMP, System.currentTimeMillis());

			db.insert(DebugDatabase.EventsEntryTable.TABLE_NAME, null, values);

		}

		@Override
		public void getApiLog(String apiInfoString, String packageName) throws RemoteException {

			Log.d("debug_log_api", packageName);

			SQLiteDatabase db = DatabaseHelper.getInstance().getDatabseManipulater();

			ContentValues values = new ContentValues();
			values.put(DebugDatabase.ApiLogTable.API_LOG, apiInfoString);
			values.put(DebugDatabase.ApiLogTable.PACKAGE_NAME, packageName);
			values.put(DebugDatabase.ApiLogTable.TIMESTAMP, System.currentTimeMillis());

			db.insert(DebugDatabase.ApiLogTable.TABLE_NAME, null, values);

		}

		@Override
		public String getApiUrl() throws RemoteException {
			return PreferenceManager.getDefaultSharedPreferences(DebugApplication.getAppContext()).getString(MainActivity.API, null);
		}

		@Override
		public void getAppProfileInfo(int processId, String packageName) throws RemoteException {

			Log.d("package_name", getPackageName());
			List<String> list = UtilClass.getPackage(getApplicationContext());
			if (list != null) {
				if (!list.contains(packageName)) {
					list.add(packageName);
				}
			}
			else {
				list = new ArrayList<>();
				list.add(packageName);
			}
			UtilClass.saveDataToSharedPref(getApplicationContext(), list);
		}

	};

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		unbind = false;
		return stub;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		unbind = true;
		return super.onUnbind(intent);
	}

}
