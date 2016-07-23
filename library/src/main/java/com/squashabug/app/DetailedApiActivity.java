package com.squashabug.app;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squashabug.app.models.ApiLogModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class DetailedApiActivity extends AppCompatActivity {


	TextView url, requestHeaders, requestBody, requestMethod, responseBody, responseHeaders, responseBodyPrettify, requestTime, responseTime;
	HorizontalScrollView pretifyScrollView;
	String prettifyResponse;
	ApiLogModel apiLog;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.detailed_api_log);

		url = (TextView) findViewById(R.id.apiUrl);
		requestMethod = (TextView) findViewById(R.id.requestMethod);
		requestHeaders = (TextView) findViewById(R.id.requestHeaders);
		requestBody = (TextView) findViewById(R.id.requestBody);
		requestTime = (TextView) findViewById(R.id.requestTime);

		responseBody = (TextView) findViewById(R.id.responseBody);
		responseTime = (TextView) findViewById(R.id.responseTime);
		responseHeaders = (TextView) findViewById(R.id.responseHeaders);
		responseBodyPrettify = (TextView) findViewById(R.id.responseBodyPrettify);
		pretifyScrollView = (HorizontalScrollView) findViewById(R.id.scrollViewPrettify);


		Bundle args = getIntent().getExtras();
		if (args != null) {
			apiLog = (ApiLogModel) args.getParcelable("eventObject");
		}

		url.setText(apiLog.url);
		requestHeaders.setText(!TextUtils.isEmpty(apiLog.requestHeaders) ? apiLog.requestHeaders : "");
		requestMethod.setText(!TextUtils.isEmpty(apiLog.method) ? apiLog.method : "");
		requestBody.setText(!TextUtils.isEmpty(apiLog.requestBody) ? apiLog.requestBody : "");
		requestTime.setText(apiLog.getRequestTime());


		responseHeaders.setText(!TextUtils.isEmpty(apiLog.responseHeaders) ? apiLog.responseHeaders : "");
		responseTime.setText("" + apiLog.responseTime + "ms");
		responseBody.setText(apiLog.responseBody);
		pretifyScrollView.setVisibility(View.GONE);


		String text = apiLog.responseBody;
		Object json = null;
		try {
			json = new JSONTokener(text).nextValue();

			if (json instanceof JSONObject) {
				prettifyResponse = new JSONObject(text).toString(4);
			}
			else if (json instanceof JSONArray) {
				prettifyResponse = new JSONArray(text).toString(4);
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.api_activity_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.prettify:
				if (apiLog != null) {
					try {
						responseBody.setVisibility(View.GONE);
						pretifyScrollView.setVisibility(View.VISIBLE);
						responseBodyPrettify.setText(TextUtils.isEmpty(prettifyResponse) ? "" : prettifyResponse);

					}
					catch (Throwable e) {
						e.printStackTrace();
					}
				}
				break;
			case R.id.sendOption:
				getTextForSharing();
				break;
		}
		return true;
	}

	public void getTextForSharing() {

		SpannableStringBuilder shareString = new SpannableStringBuilder();
		LinearLayout layout = (LinearLayout) findViewById(R.id.mainLayout);
		for (int i = 0; i < layout.getChildCount(); i++) {

			if (!(layout.getChildAt(i) instanceof HorizontalScrollView)) {
				shareString.append(((TextView) layout.getChildAt(i)).getText() + "\n\n");
			}
		}
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/html");
		intent.putExtra(Intent.EXTRA_SUBJECT, "Debug Ducky says hi");
		intent.putExtra(Intent.EXTRA_TEXT, shareString);

		startActivity(Intent.createChooser(intent, "Send Email"));
	}
}
