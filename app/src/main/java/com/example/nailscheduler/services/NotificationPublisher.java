package com.example.nailscheduler.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.nailscheduler.ProfileClient;
import com.example.nailscheduler.R;

public class NotificationPublisher extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        String currentBO = intent.getExtras().getString("boName");
        String startTimne = intent.getExtras().getString("startTime");
        String content= "מחר נקבע תור בשעה : "+"\n"+
                startTimne+ " עבור בית העסק : "+ currentBO;

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, ProfileClient.NOTIFICATION_CHANNEL_ID)
                .setContentTitle("תזכורת !")
                .setContentText(content)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManagerCompat notificationCon=NotificationManagerCompat.from((context));
        notificationCon.notify(1,builder.build());
    }
}
