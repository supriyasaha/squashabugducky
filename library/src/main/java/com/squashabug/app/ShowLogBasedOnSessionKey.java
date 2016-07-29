package com.squashabug.app;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squashabug.app.database.DatabaseHelper;
import com.squashabug.app.database.DebugDatabase;
import com.squashabug.app.models.ApiLogModel;

import java.util.ArrayList;
import java.util.Collections;

public class ShowLogBasedOnSessionKey extends AppCompatActivity {

    private ArrayList<ApiLogModel> apiLogModelList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_list);

        apiLogModelList = new ArrayList<>();
        getDataFromDatabase();
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
                    Log.d("session_key", apiLogModel.sessionKey != null ? apiLogModel.sessionKey : "null");

                    apiLogModelList.add(apiLogModel);
                }
                cursor.close();
            }
            Collections.sort(apiLogModelList);
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
    }

}
