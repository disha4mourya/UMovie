package com.example.popularmovies.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ServiceManager {

    private Context context;

    public ServiceManager(Context base) {
        context = base;
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (cm != null) {
            networkInfo = cm.getActiveNetworkInfo();
        }
        return networkInfo != null && networkInfo.isConnected();
    }
}