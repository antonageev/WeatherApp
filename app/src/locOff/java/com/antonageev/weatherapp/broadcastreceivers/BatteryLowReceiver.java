package com.antonageev.weatherapp.broadcastreceivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.antonageev.weatherapp.R;

public class BatteryLowReceiver extends BroadcastReceiver {
    private int messageId = 100;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.wtf("BatteryLowReceiver", "onReceive: launched");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "1")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Weather App informer")
                .setContentText("Low battery");

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(messageId++, builder.build());
    }
}
