package com.squashabug.sdk;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.squashabug.sdk.models.ApiLogModel;
import com.squashabug.sdk.models.EventLogModel;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.UUID;

public class SquashABug implements ServiceConnection {

	public static DebugAIDL debugService = null;
	private static SquashABug serviceInstance;
	private WeakReference<Context> appContext;
	private ServiceConnectedCallBacks serviceConnectedCallBacks;
	private String session_id;

	public static SquashABug getInstance(Context context) {
		if (serviceInstance == null) {
			serviceInstance = new SquashABug();
		}
		serviceInstance.setWeakReference(context);
		return serviceInstance;
	}

	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {

		debugService = DebugAIDL.Stub.asInterface((IBinder) service);

		Log.d("IRemote", "Binding is done - Service connected");
		if (serviceConnectedCallBacks != null) {
			session_id = UUID.randomUUID().toString();
			serviceConnectedCallBacks.onServiceConnected(serviceInstance);
		}
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {

		if (serviceConnectedCallBacks != null) {
			serviceConnectedCallBacks.onServiceDisconnected(serviceInstance);
		}
	}


	public interface ServiceConnectedCallBacks {

		void onServiceConnected(SquashABug serviceConnection);

		void onServiceDisconnected(SquashABug serviceConnection);

		void onErrorCallBacks(String message);
	}

	public void setWeakReference(Context context) {
		appContext = new WeakReference<>(context);
	}

	public SquashABug registerDebugCallBacks(ServiceConnectedCallBacks serviceConnectedCallBacks) {
		this.serviceConnectedCallBacks = serviceConnectedCallBacks;
		return serviceInstance;
	}

	public void connect() {
		if (serviceInstance != null && appContext != null && appContext.get() != null) {
			Intent intent = new Intent("com.squashabug.app.DebugDuckyService.DEBUG_SERVICE");
			intent.setClassName("com.squashabug.app", "com.squashabug.app.DebugDuckyService");
			// binding to remote service
			serviceInstance.appContext.get().bindService(intent, serviceInstance, Context.BIND_AUTO_CREATE);
			Log.d("Binding", DebugAIDL.class.getName());
		}
	}

	public void logAPiCalls(ApiLogModel model) {

		try {
			if (debugService != null && appContext != null && appContext.get() != null) {
				HashMap<String, String> hashMap = new HashMap<>();
				hashMap.put("sessionKey", session_id);
				hashMap.put("timeStamp", String.valueOf(System.currentTimeMillis()));
				hashMap.put("url", model.url);
				hashMap.put("method", model.method);
				hashMap.put("requestTime", model.requestTime);
				hashMap.put("responseTime", model.responseTime);
				hashMap.put("requestBody", model.requestBody);
				hashMap.put("requestHeaders", model.requestHeaders);
				hashMap.put("responseHeaders", model.responseHeaders);
				hashMap.put("responseBody", model.responseBody);

				String apiLog = new JSONObject(hashMap).toString();
				debugService.getApiLog(apiLog, appContext.get().getPackageName());

			}
		}
		catch (Throwable e) {
			e.printStackTrace();
			serviceConnectedCallBacks.onErrorCallBacks(e.getMessage());
		}
	}

	public void getEventLog(EventLogModel event) {
		try {
			if (debugService != null && appContext != null && appContext.get() != null) {
				debugService.getEventLog(event.eventName, new JSONObject(event.eventMessage).toString(), appContext.get().getPackageName());
			}
		}
		catch (Throwable e) {
			e.printStackTrace();
			serviceConnectedCallBacks.onErrorCallBacks(e.getMessage());
		}
	}

	public String debugUrl() {

		if (debugService != null && appContext != null && appContext.get() != null) {
			try {
				return debugService.getApiUrl();
			}
			catch (RemoteException e) {
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}
}
