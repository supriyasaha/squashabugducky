package com.squashabug.app;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.squashabug.app.database.DatabaseHelper;
import com.squashabug.app.database.DebugDatabase;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

	public static final String API = "API";
	Intent intent;
	public EditText apiUrl;
	String apiText;
	boolean debugSwitch, urlToastSwitch;
	TextView runningApp;
	Button clearButton;
	public static List<String> appList;
	public static String packageName;
	private Spinner spinner;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		((Button) findViewById(R.id.startButton)).setOnClickListener(this);
		((Button) findViewById(R.id.show_url_toasts)).setOnClickListener(this);
		((Button) findViewById(R.id.show_track_toasts)).setOnClickListener(this);
		clearButton = (Button) findViewById(R.id.switch1);
		clearButton.setOnClickListener(this);
		runningApp = (TextView) findViewById(R.id.runningApp);
		apiUrl = (EditText) findViewById(R.id.apiText);

		apiText = PreferenceManager.getDefaultSharedPreferences(DebugApplication.getAppContext()).getString(API, null);
		if (!TextUtils.isEmpty(apiText)) {
			apiUrl.setText(apiText);
		}


	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.show_track_toasts:
				startActivity(new Intent(this, EventListActivity.class));
				break;

			case R.id.startButton:
				storeApToSharedPreference();
//				intent = new Intent(this, DebugDuckyService.class);
//				startService(intent);
				break;
			case R.id.show_url_toasts:
				startActivity(new Intent(this, ApiLofListActivity.class));
				break;

			case R.id.switch1:
				SQLiteDatabase db = DatabaseHelper.getInstance().getDatabseManipulater();
				db.delete(DebugDatabase.ApiLogTable.TABLE_NAME, null, null);
				db.delete(DebugDatabase.EventsEntryTable.TABLE_NAME, null, null);
				break;
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (intent != null) {
			stopService(intent);
		}
	}

	public void storeApToSharedPreference() {
		apiText = TextUtils.isEmpty(apiUrl.getText()) ? null : apiUrl.getText().toString().trim();
		PreferenceManager.getDefaultSharedPreferences(this).edit().putString(API, apiText).commit();
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (spinner != null) {
			getSpinnerDetails();
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu, menu);

		MenuItem item = menu.findItem(R.id.spinner);
		spinner = (Spinner) MenuItemCompat.getActionView(item);
		getSpinnerDetails();
		return true;
	}

	public void getPackages() {

		SQLiteDatabase db = DatabaseHelper.getInstance().getDatabseManipulater();

		Cursor c = db.query(true, DebugDatabase.ApiLogTable.TABLE_NAME, new String[]{DebugDatabase.ApiLogTable.PACKAGE_NAME},
				null, null, null, null, null, null);
		if (c != null)
			if (c.getCount() > 0) {
				for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
					String value = c.getString(c.getColumnIndexOrThrow(DebugDatabase.ApiLogTable.PACKAGE_NAME));
					if (appList != null && appList.size() > 0) {
						if (!appList.contains(value)) {
							appList.add(value);
						}
					}
					else {
						appList = new ArrayList<>();
						appList.add(value);
					}
					Log.d("package", value);
				}
				c.close();
			}
		if (appList != null && appList.size() > 0) {
			packageName = appList.get(0);
			runningApp.setText(packageName);
		}
	}

	public static String getPackage() {
		return packageName != null ? packageName : "";
	}

	public void getSpinnerDetails() {
		getPackages();

		ArrayAdapter<String> adapter = null;
		if (appList != null && appList.size() > 0) {
			adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1,
					appList);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(adapter);
			spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					packageName = (String) parent.getItemAtPosition(position);
					runningApp.setText(packageName);
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {

				}

			});
		}
	}
}
