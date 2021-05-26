package com.example.piston.main.notifications;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.Window;
import android.view.WindowManager;

public class NotificationMultiSelection {

    @SuppressLint("NewApi")
    public static void toggleStatusBarColor(Activity activity, int color) {
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS); // Deprecated
        window.setStatusBarColor(color);
    }
}
