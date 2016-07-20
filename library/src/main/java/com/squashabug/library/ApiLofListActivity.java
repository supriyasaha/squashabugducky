package com.squashabug.library;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squashabug.library.database.DatabaseHelper;
import com.squashabug.library.database.DebugDatabase;
import com.squashabug.library.models.ApiLogModel;

import java.util.ArrayList;
import java.util.List;

public class ApiLofListActivity extends AppCompatActivity {


	ListView list;
	ListAdapter adapter;
	List<ApiLogModel> apiLogModelList;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		setContentView(R.layout.log_list);
		list = (ListView) findViewById(R.id.list);
		adapter = new ListAdapter<ApiLogModel>(ApiLofListActivity.this);
		apiLogModelList = new ArrayList<>();

		if (!TextUtils.isEmpty(MainActivity.getPackage())) {
			getDataFromDatabase();
			if (apiLogModelList != null) {
				adapter.setData(apiLogModelList);
				list.setAdapter(adapter);
				adapter.notifyDataSetChanged();
			}
		}

	}

	public void getDataFromDatabase() {

		SQLiteDatabase db = DatabaseHelper.getInstance().getDatabseManipulater();
		Gson gson = new Gson();

		try {
			Cursor cursor = db.rawQuery("Select * from " + DebugDatabase.ApiLogTable.TABLE_NAME + " WHERE " + DebugDatabase.ApiLogTable.PACKAGE_NAME + "='" + MainActivity.getPackage() + "'", null);
			if (cursor != null && cursor.getCount() > 0) {
				for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
					ApiLogModel apiLogModel;
					apiLogModel = gson.fromJson(cursor.getString(cursor.getColumnIndexOrThrow(DebugDatabase.ApiLogTable.API_LOG)),
							new TypeToken<ApiLogModel>() {
							}.getType());

					apiLogModel.id = cursor.getInt(cursor.getColumnIndexOrThrow(DebugDatabase.EventsEntryTable.ENTRY_ID));
					apiLogModel.timestamp = cursor.getLong(cursor.getColumnIndexOrThrow(DebugDatabase.EventsEntryTable.TIMESTAMP));

					apiLogModelList.add(apiLogModel);
				}
				cursor.close();
			}
		}
		catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
