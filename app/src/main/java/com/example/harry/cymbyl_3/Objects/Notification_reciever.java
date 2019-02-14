package com.example.harry.cymbyl_3.Objects;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.example.harry.cymbyl_3.NotificationActivity;

public class Notification_reciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager nm = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);

        Intent rIntent = new Intent(context, NotificationActivity.class);
        rIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pIntent = PendingIntent.getActivity(context, 100, rIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentIntent(pIntent)
                .setSmallIcon(android.R.drawable.arrow_down_float)
                .setContentTitle("You Can Do It!")
                .setContentText("Your mantra puzzle is here ;)")
                .setOngoing(true)
                .setAutoCancel(true);

        nm.notify(100, builder.build());
    }
}
