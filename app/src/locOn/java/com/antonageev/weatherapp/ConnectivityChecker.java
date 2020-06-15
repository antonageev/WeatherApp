package com.antonageev.weatherapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;

import androidx.annotation.NonNull;

public class ConnectivityChecker {

    private static boolean wasLost = false;

    public ConnectivityChecker(Activity activity){
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(activity.CONNECTIVITY_SERVICE);
        cm.registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback(){
            @Override
            public void onAvailable(@NonNull Network network) {
                super.onAvailable(network);
                if (hasConnection(activity) && wasLost){
                    Intent intent = new Intent("com.antonageev.weatherapp.NetworkStateChange");
                    intent.putExtra("ConnectionStatusKey", "On-line");
                    wasLost = false;
                    activity.sendBroadcast(intent);
                }
            }

            @Override
            public void onLost(@NonNull Network network) {
                super.onLost(network);
                if (!hasConnection(activity) && !wasLost){
                    Intent intent = new Intent("com.antonageev.weatherapp.NetworkStateChange");
                    intent.putExtra("ConnectionStatusKey", "Off-line");
                    activity.sendBroadcast(intent);
                    wasLost = true;
                }
            }
        });
    }

    public static boolean hasConnection(final Context context)
    {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        wifiInfo = cm.getActiveNetworkInfo();
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        return false;
    }
}
