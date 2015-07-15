package com.utilsframework.android.view;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;

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
        notify(context, id, builder.getNotification());
    }
}
