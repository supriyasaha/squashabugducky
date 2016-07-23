package com.squashabug.sdk.models;

import android.os.Parcel;
import android.os.Parcelable;


public class BaseModel implements Parcelable {

	public int id;
	public long timestamp;
	public String sessionKey;

	public BaseModel(){}

	protected BaseModel(Parcel in) {
		id = in.readInt();
		timestamp = in.readLong();
		sessionKey = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeLong(timestamp);
		dest.writeString(sessionKey);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<BaseModel> CREATOR = new Creator<BaseModel>() {
		@Override
		public BaseModel createFromParcel(Parcel in) {
			return new BaseModel(in);
		}

		@Override
		public BaseModel[] newArray(int size) {
			return new BaseModel[size];
		}
	};
}
