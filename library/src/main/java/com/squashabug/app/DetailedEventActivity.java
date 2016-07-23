package com.squashabug.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.squashabug.app.models.EventLogModel;

public class DetailedEventActivity extends AppCompatActivity {


	TextView eventName, eventProperties;
	EventLogModel event;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.detailed_event_log);
		eventName = (TextView) findViewById(R.id.eventName);
		eventProperties = (TextView) findViewById(R.id.eventProp);

		Bundle args = getIntent().getExtras();
		if (args != null) {
			event = (EventLogModel) args.getParcelable("eventObject");
		}

		eventName.setText(event.eventName);

		StringBuilder builder = new StringBuilder();
		String[] array = event.eventMessage.split(",");
		for (int i = 0; i < array.length; i++) {
			builder.append(array[i] + "\n");
		}
		eventProperties.setText(builder.toString());
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
}
