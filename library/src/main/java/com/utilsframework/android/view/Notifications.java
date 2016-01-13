package com.utilsframework.android.view;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * Created by CM on 7/15/2015.
 */
public final class Notifications {
    public static void notify(Context context, int id, Notification notification) {
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(id, notification);
    }

    @SuppressWarnings("deprecation")
    public static void notify(Context context, int id, Notification.Builder builder) {
        Notification notification = builder.getNotification();
        notification.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;
        notify(context, id, notification);
    }

    public static void setActivity(Context context, Notification.Builder builder, Class<Activity> activity) {
        Intent resultIntent = new Intent(context, activity);
        setActivityIntent(context, builder, resultIntent);
    }

    public static void setActivityIntent(Context context, Notification.Builder builder, Intent resultIntent) {
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        builder.setContentIntent(pendingIntent);
    }
}
