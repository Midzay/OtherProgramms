package com.example.maksimov.missterh;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;



public class NotificationMessageMedicament extends BroadcastReceiver {
    private static String TAG="myLogs";
    MainActivity ma;
    Intent intent;


    private static final String NOTIFICATION_TAG = "Message1";


    @TargetApi(Build.VERSION_CODES.ECLAIR)
    private static void notify(final Context context, final Notification notification) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.notify(NOTIFICATION_TAG, 0, notification);
        } else {
            nm.notify(NOTIFICATION_TAG.hashCode(), notification);
        }


    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    public static void cancel(final Context context) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.cancel(NOTIFICATION_TAG, 0);
        } else {
            nm.cancel(NOTIFICATION_TAG.hashCode());
        }

    }

    @Override
    public void onReceive(Context context, Intent intent) {

        final Resources res = context.getResources();
        String name_m = intent.getStringExtra("name_med");
        String time_m = intent.getStringExtra("time_med");

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle("Принять лекарство")
                .setContentText(name_m+" Время: "+time_m)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                //  .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.ic_launcher))
                .setTicker("Необходимо принять "+name_m+"Время: "+time_m)
                .setContentIntent(
                        PendingIntent.getActivity(
                                context,
                                0,
                                new Intent(context,Notice.class).putExtra("time_med",time_m),
                                PendingIntent.FLAG_UPDATE_CURRENT)
                )
                .setAutoCancel(true);

        notify(context, builder.build());




    }



}
