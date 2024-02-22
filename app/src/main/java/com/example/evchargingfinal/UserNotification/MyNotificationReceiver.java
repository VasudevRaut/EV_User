package com.example.evchargingfinal.UserNotification;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyNotificationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String title = intent.getStringExtra("title");
            String message = intent.getStringExtra("message");

            // Handle the notification data here
        }
    }


