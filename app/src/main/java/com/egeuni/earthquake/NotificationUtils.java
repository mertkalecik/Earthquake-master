package com.egeuni.earthquake;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.common.internal.Objects;

public class NotificationUtils {

    private static final int EARTHQUAKE_NOTIFICATION_ID = 1138;


    private static final int EARTHQUAKE_PENDING_INTENT_ID = 3417;

    private static final String EARTHQUAKE_NOTIFICATION_CHANNEL_ID = "earthquake_notification_channel";


    public static void remindUserBecauseUpdate(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    EARTHQUAKE_NOTIFICATION_CHANNEL_ID,
                    context.getString(R.string.main_notification_channel_name),
                    NotificationManager.IMPORTANCE_HIGH);

            notificationManager.createNotificationChannel(mChannel);
        }


        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(context, EARTHQUAKE_NOTIFICATION_CHANNEL_ID)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.drawable.icon_map)
                .setLargeIcon(largeIcon(context))
                .setContentTitle(context.getString(R.string.earthquake_reminder_notification_title))
                .setContentText(context.getString(R.string.earthquake_reminder_notification_body))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(
                        context.getString(R.string.earthquake_reminder_notification_body)))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent(context))
                .setAutoCancel(true);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            nBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }

        notificationManager.notify(EARTHQUAKE_NOTIFICATION_ID, nBuilder.build());
    }

    public static void remindUserBecauseEarthquake(Context context, String name, String place) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    EARTHQUAKE_NOTIFICATION_CHANNEL_ID,
                    context.getString(R.string.main_notification_channel_name),
                    NotificationManager.IMPORTANCE_HIGH);

            notificationManager.createNotificationChannel(mChannel);
        }

        String text = context.getString(R.string.earthquake_event_reminder_notification_body_first) + name +
                context.getString(R.string.earthquake_event_reminder_notification_body_second) + place;
        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(context, EARTHQUAKE_NOTIFICATION_CHANNEL_ID)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.drawable.icon_map)
                .setLargeIcon(largeIcon(context))
                .setContentTitle(context.getString(R.string.earthqauke_event_reminder_notification_title))
                .setContentText(text)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(text))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent(context))
                .setAutoCancel(true);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            nBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }

        notificationManager.notify(EARTHQUAKE_NOTIFICATION_ID, nBuilder.build());
    }

    public static void clearAllNotifications(Context context) {
        Toast.makeText(context, "Temizlendi", Toast.LENGTH_LONG).show();

    }

    private static PendingIntent contentIntent(Context context) {
        Intent startActivityIntent = new Intent(context, MainActivity.class);

        return PendingIntent.getActivity(
                context,
                EARTHQUAKE_PENDING_INTENT_ID,
                startActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }


    private static Bitmap largeIcon(Context context) {

        Resources res = context.getResources();
        Bitmap largeIcon = BitmapFactory.decodeResource(res, R.drawable.icon_map);
        return largeIcon;
    }
}
