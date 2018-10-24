package com.example.manhvd.noteapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtils {
    public static boolean hasConnection(Context context) {
        if(context == null) {
            return false;
        }
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiNetwork = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if(wifiNetwork != null && wifiNetwork.isConnected()) {
            return true;
        }

        NetworkInfo mobileNetwork = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if(mobileNetwork != null && mobileNetwork.isConnected()) {
            return true;
        }

        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
        if(activeNetwork != null && activeNetwork.isConnected()) {
            return true;
        }

        return false;
    }
}
