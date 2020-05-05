package com.example.maksimov.missterh;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;


/**
 * Created by maksimov on 01.02.2017.
 */

public class NotificationMessage extends BroadcastReceiver {


    private static final String NOTIFICATION_TAG = "Message";


    @TargetApi(Build.VERSION_CODES.ECLAIR)
    private static void notify(final Context context, final Notification notification) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.notify(NOTIFICATION_TAG, 1, notification);
        } else {
            nm.notify(NOTIFICATION_TAG.hashCode(), notification);
        }
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    public static void cancel(final Context context) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.cancel(NOTIFICATION_TAG, 1);
        } else {
            nm.cancel(NOTIFICATION_TAG.hashCode());
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle("Уведомление")
                .setContentText("Вы забыли измерить давление")
                .setPriority(NotificationCompat.PRIORITY_HIGH)

                   .setTicker("Вы забыли измерить давление")
                .setContentIntent(
                        PendingIntent.getActivity(
                                context,
                                0,
                                new Intent(context,MainActivity.class),
                                PendingIntent.FLAG_UPDATE_CURRENT)
                )
                .setAutoCancel(true);

        notify(context, builder.build());




    }


}
