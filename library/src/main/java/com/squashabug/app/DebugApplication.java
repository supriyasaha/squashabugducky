package com.squashabug.app;


import android.app.Application;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

public class DebugApplication extends Application {

	private static final String YES_ACTION = "com.example.coupondunia.couponduniadebugducky.yes";
	public static Context context;

	@Override
	public void onCreate() {
		super.onCreate();

		context = getApplicationContext();
	}

	public static Context getAppContext() {
		return context;
	}
	public static void shoeDebugNotification() {
		int NOTIFICATION_ID = 0;
		NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
				.setAutoCancel(true)
				.setDefaults(NotificationCompat.DEFAULT_ALL)
				.setSmallIcon(android.R.drawable.ic_dialog_alert)
				.setContentTitle("DebugDucky")
				.setContentText("debugDuckyIsRunning");


		Bundle bundle = new Bundle();
		bundle.putInt("Notification_id", NOTIFICATION_ID);

		Intent yesReceive = new Intent(context, TriggerActionReceiver.class);
		yesReceive.setAction(YES_ACTION);
		PendingIntent pendingIntentYes = PendingIntent.getBroadcast(context, 12345, yesReceive, PendingIntent.FLAG_UPDATE_CURRENT);
		builder.addAction(android.R.drawable.ic_input_add, "Yes", pendingIntentYes);

		builder.setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0));
		NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(NOTIFICATION_ID, builder.build());
	}

}
