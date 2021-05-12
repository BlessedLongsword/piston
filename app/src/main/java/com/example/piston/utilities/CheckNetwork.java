package com.example.piston.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;

public class CheckNetwork {

    @SuppressWarnings("deprecation") //Use deprecated methods only for old Android versions...
    public static boolean isConnected(Context getApplicationContext) {

        ConnectivityManager cm = (ConnectivityManager) getApplicationContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return cm != null && cm.getActiveNetwork() != null && cm.getNetworkCapabilities(cm.getActiveNetwork()) != null;
        } else {
            return cm != null && cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
        }
    }

}
