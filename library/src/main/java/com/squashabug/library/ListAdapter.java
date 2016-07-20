package com.squashabug.library;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.squashabug.library.models.ApiLogModel;
import com.squashabug.library.models.BaseModel;
import com.squashabug.library.models.EventLogModel;

import java.text.SimpleDateFormat;
import java.util.List;


public class ListAdapter<T extends BaseModel> extends BaseAdapter {

	Context context;
	List<T> listLog;
	public static String dateFormat = "dd MMM HH:mm";
	public SimpleDateFormat simpleDateFormat;

	public ListAdapter(Context context) {
		this.context = context;
		simpleDateFormat = new SimpleDateFormat(dateFormat);
	}

	public void setData(List<T> listLog) {
		this.listLog = listLog;
	}

	@Override
	public int getCount() {
		return listLog.size();
	}

	@Override
	public T getItem(int position) {
		return listLog.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.event_list_item, parent, false);
			holder = new ViewHolder();
			holder.eventName = (TextView) convertView.findViewById(R.id.eventName);
			holder.timestamp = (TextView) convertView.findViewById(R.id.timeStamp);

			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}

		final T model = getItem(position);
		if (model instanceof EventLogModel) {
			holder.eventName.setText(((EventLogModel) model).eventName);
			holder.timestamp.setText(simpleDateFormat.format(model.timestamp));

		}
		else {
			holder.eventName.setText(((ApiLogModel) model).url);
			holder.timestamp.setText(simpleDateFormat.format(model.timestamp));
		}

		convertView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				Intent intent;
				if (model instanceof EventLogModel) {
					bundle.putParcelable("eventObject", ((EventLogModel) model));
					intent = new Intent(context, DetailedEventActivity.class);
				}
				else {
					bundle.putParcelable("eventObject", ((ApiLogModel) model));
					intent = new Intent(context, DetailedApiActivity.class);
				}
				intent.putExtras(bundle);
				context.startActivity(intent);
			}
		});
		return convertView;
	}


	public static class ViewHolder {

		TextView eventName, timestamp;
	}
}
