package com.squashabug.app.models;


import android.os.Parcel;
import android.os.Parcelable;

public class EventLogModel extends BaseModel implements Parcelable {

	public String eventName;
	public String eventMessage;

	public EventLogModel(){}

	protected EventLogModel(Parcel in) {
		super(in);
		eventName = in.readString();
		eventMessage = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeString(eventName);
		dest.writeString(eventMessage);
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
