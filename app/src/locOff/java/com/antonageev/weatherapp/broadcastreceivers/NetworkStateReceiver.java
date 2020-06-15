package com.antonageev.weatherapp.broadcastreceivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.antonageev.weatherapp.R;

public class NetworkStateReceiver extends BroadcastReceiver {
    int messageId = 2000;

    @Override
    public void onReceive(Context context, Intent intent) {
        String text = null;
        if (intent != null){
            text = intent.getStringExtra("ConnectionStatusKey");
        }
        if (text == null){
            text = "Connection state changed";
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "1")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Weather App informer")
                .setContentText(text);

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(messageId++, builder.build());
    }
}
