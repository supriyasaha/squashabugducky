package com.squashabug.app;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.ListView;

import com.squashabug.app.database.DatabaseHelper;
import com.squashabug.app.database.DebugDatabase;
import com.squashabug.app.models.EventLogModel;

import java.util.ArrayList;
import java.util.List;


public class EventListActivity extends AppCompatActivity {


	ListView list;
	ListAdapter adapter;
	List<EventLogModel> eventList;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		setContentView(R.layout.log_list);
		list = (ListView) findViewById(R.id.list);
		adapter = new ListAdapter<EventLogModel>(EventListActivity.this);
		eventList = new ArrayList<>();

		if (!TextUtils.isEmpty(MainActivity.getPackage())) {
			getDataFromDatabase();

			if (eventList != null) {
				adapter.setData(eventList);
				list.setAdapter(adapter);
				adapter.notifyDataSetChanged();

			}
		}

	}

	public void getDataFromDatabase() {

		SQLiteDatabase db = DatabaseHelper.getInstance().getDatabseManipulater();

		Cursor cursor = db.rawQuery("Select * from " + DebugDatabase.EventsEntryTable.TABLE_NAME + " WHERE " + DebugDatabase.EventsEntryTable.PACKAGE_NAME + "='" + MainActivity.getPackage() + "'", null);
		if (cursor != null && cursor.getCount() > 0) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				EventLogModel eventModel = new EventLogModel();
				eventModel.id = cursor.getInt(cursor.getColumnIndexOrThrow(DebugDatabase.EventsEntryTable.ENTRY_ID));
				eventModel.eventName = cursor.getString(cursor.getColumnIndexOrThrow(DebugDatabase.EventsEntryTable.EVENT_NAME));
				eventModel.eventMessage = cursor.getString(cursor.getColumnIndexOrThrow(DebugDatabase.EventsEntryTable.PROPERTIES));
				eventModel.timestamp = cursor.getLong(cursor.getColumnIndexOrThrow(DebugDatabase.EventsEntryTable.TIMESTAMP));

				eventList.add(eventModel);
			}
			cursor.close();
		}
	}
}
