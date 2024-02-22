package com.example.evchargingfinal.UserNotification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            // Re-register the FCM token on device boot if needed
            // This ensures the device receives notifications after a reboot
        }
    }
}
