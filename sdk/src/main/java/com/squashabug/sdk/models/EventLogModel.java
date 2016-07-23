package com.squashabug.sdk.models;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

public class EventLogModel extends BaseModel implements Parcelable {

	public String eventName;
	public HashMap<String, String> eventMessage;

	public EventLogModel(){}

	protected EventLogModel(Parcel in) {
		super(in);
		eventName = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeString(eventName);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<EventLogModel> CREATOR = new Creator<EventLogModel>() {
		@Override
		public EventLogModel createFromParcel(Parcel in) {
			return new EventLogModel(in);
		}

		@Override
		public EventLogModel[] newArray(int size) {
			return new EventLogModel[size];
		}
	};
}
