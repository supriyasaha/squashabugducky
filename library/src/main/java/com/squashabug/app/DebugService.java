package com.squashabug.app;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.squashabug.sdk.DebugAIDL;

public class DebugService implements ServiceConnection {

	Context context;
	public static DebugService mInstance;
	public ServiceConnectedCallBacks serviceConnectedCallBacks;
	private DebugAIDL debugService;
	private boolean unbindBoolean = false;

	public DebugService setUp(Context context) {
		mInstance = new DebugService();
		this.context = context;
		return mInstance;
	}

	public DebugService getServiceConnectedCallback(ServiceConnectedCallBacks serviceConnectedCallBacks) {
		mInstance.serviceConnectedCallBacks = serviceConnectedCallBacks;
		return mInstance;
	}


	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		debugService = DebugAIDL.Stub.asInterface((IBinder) service);

		Toast.makeText(context,
				"Addition Service Connected", Toast.LENGTH_SHORT)
				.show();
		Log.d("IRemote", "Binding is done - Service connected");
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		debugService = null;
		Toast.makeText(context, "Service Disconnected",
				Toast.LENGTH_SHORT).show();
		Log.d("IRemote", "Binding - Service disconnected");
	}

	public interface ServiceConnectedCallBacks {

		public void onServiceConnected();

		public void onServiceDisconnected();

	}

	public DebugService connect() {

		Intent intent = new Intent("com.squashabug.library.DebugDuckyService");
		intent.setPackage("com.example.coupondunia.couponduniadebugducky");

		// binding to remote service
		context.bindService(intent, this, Context.BIND_AUTO_CREATE);
		Log.d("Binding", DebugAIDL.class.getName());

		return mInstance;
	}
}
