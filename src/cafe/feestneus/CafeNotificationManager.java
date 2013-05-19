package cafe.feestneus;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class CafeNotificationManager extends Application
{
	private int notificationInt = 0;

    //Common functions
    public void pushNotification(CharSequence title, CharSequence text)
    {
    	String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);

		int icon = R.drawable.ic_launcher;
		long when = System.currentTimeMillis();

		Notification notification = new Notification(icon, text, when);
		
		Context context = getApplicationContext();
		Intent notificationIntent = new Intent(this, CafedeFeestneusActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

		notification.setLatestEventInfo(context, title, text, contentIntent);
		mNotificationManager.notify(notificationInt, notification);

		notificationInt++;
    }
}