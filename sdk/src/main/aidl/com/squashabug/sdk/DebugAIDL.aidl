package com.squashabug.sdk;


interface DebugAIDL {

	void getEventLog(String eventName, String message, String packageName);

	void getApiLog(String apiInfoString,  String packageName);

	 String getApiUrl();

	void getAppProfileInfo(int processId, String packageName);
}
