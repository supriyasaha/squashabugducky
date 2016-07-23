package com.squashabug.sdk.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;


public class ApiLogModel extends BaseModel implements Parcelable {

	SimpleDateFormat simpleDateFormat;
	String dateFormat = "dd MMM yyyy HH:mm:ss";
	public String url;
	public String method;
	public String requestHeaders;
	public String requestBody;
	public String requestTime;
	public String responseTime;
	public String responseHeaders;
	public String responseBody;

	public ApiLogModel() {
	}

	protected ApiLogModel(Parcel in) {
		super(in);
		url = in.readString();
		method = in.readString();
		requestHeaders = in.readString();
		requestBody = in.readString();
		requestTime = in.readString();
		responseTime = in.readString();
		responseHeaders = in.readString();
		responseBody = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeString(url);
		dest.writeString(method);
		dest.writeString(requestHeaders);
		dest.writeString(requestBody);
		dest.writeString(requestTime);
		dest.writeString(responseTime);
		dest.writeString(responseHeaders);
		dest.writeString(responseBody);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<ApiLogModel> CREATOR = new Creator<ApiLogModel>() {
		@Override
		public ApiLogModel createFromParcel(Parcel in) {
			return new ApiLogModel(in);
		}

		@Override
		public ApiLogModel[] newArray(int size) {
			return new ApiLogModel[size];
		}
	};


	public String getRequestTime() {
		simpleDateFormat = new SimpleDateFormat(dateFormat);
		String time = simpleDateFormat.format(new Date(Long.parseLong(requestTime)));
		return "date:" + time;
	}

	public String getResponseHeaders() {
		return " \n \n" + responseHeaders + "\n" + responseTime;
	}
}
