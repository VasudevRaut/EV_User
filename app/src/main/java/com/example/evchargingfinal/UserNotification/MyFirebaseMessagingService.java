package com.example.evchargingfinal.UserNotification;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.evchargingfinal.Dashboard;
import com.example.evchargingfinal.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

//    final String sharedPreferencesFileTitle = "getnotification";
    final String sharedPreferencesFileTitle = "CapcunCRM";
    private final String CHANNEL_ID = "my_channel_id";
    private final int NOTIFICATION_ID = 101;

    String titile, body;


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

//        Toast.makeText(this, "Msg Recieved", Toast.LENGTH_SHORT).show();

        titile = remoteMessage.getNotification().getTitle();
        body = remoteMessage.getNotification().getBody();



        sendNotification(remoteMessage.getData(),remoteMessage.getNotification().getBody());

//        getFirebaseMessage(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());

    }

    private void sendNotification(Map<String,String> from, String body) {
        new Handler(Looper.getMainLooper()).post(new Runnable(){

            @Override
            public void run()
            {
//                Toast.makeText(MyFirebaseMessagingService.this, ""+body, Toast.LENGTH_SHORT).show();
                if(body.equals("SplashMSG"))
                {
                    SharedPreferences sharedPreferences = getSharedPreferences(sharedPreferencesFileTitle, MODE_PRIVATE);

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("source", "Notification");
                    editor.apply();


                }
                createNotificationChannel();
                sendNotification();

            }


        });
    }

    private void createNotificationChannel() {

        Uri soundUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.custom_sound);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "My Channel";
            String description = "Channel for my app";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            channel.setSound(soundUri, new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION).build());
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void sendNotification() {
//        Toast.makeText(this, "Come  here", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, Dashboard.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);






        // Get the resource ID of the custom sound
        Context context = getApplicationContext();

// Get the resource identifier of your custom sound file
        int soundResourceId = context.getResources().getIdentifier("custom_sound", "raw", context.getPackageName());

// Create a Uri object for your custom sound
        Uri soundUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + soundResourceId);



        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(titile)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setSound(soundUri)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }



}



//public class MyFirebaseMessagingService extends FirebaseMessagingService {
//
//    @Override
//    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
//        super.onMessageReceived(remoteMessage);
//
//        String title = remoteMessage.getNotification().getTitle();
//        String msg = remoteMessage.getNotification().getBody();
//
//        if (isAppInForeground()) {
//            // Handle the message when the app is open
//            showInAppNotification(title, msg);
//        } else {
//            // Handle the message when the app is in the background or closed
//            showSystemNotification(title, msg);
//        }
//    }
//
//    private boolean isAppInForeground() {
//        // Implement your own logic to determine if the app is in the foreground
//        // You can use lifecycle callbacks or libraries like ProcessLifecycleOwner
//        // Return true if the app is in the foreground, false otherwise
//        // For simplicity, we'll assume the app is always in the foreground
//        return true;
//    }
//
//    private void showInAppNotification(String title, String msg) {
////        Toast.makeText(this, "Vasudev come here", Toast.LENGTH_SHORT).show();
//        // Handle the FCM message when the app is open
//        // You can implement custom logic here to display an in-app notification
//        // to a particular employee
//
////        Intent intent = new Intent(this, Meeting_done.class);
////        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////        PendingIntent pendingIntent = PendingIntent.getActivity(this, uniqueRequestCode(), intent, PendingIntent.FLAG_CANCEL_CURRENT);
////
////        // Create a notification
////        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "myFirebaseChannel")
////                .setSmallIcon(R.drawable.eye)
////                .setContentTitle(title)
////                .setContentText(msg)
////                .setAutoCancel(true)
////                .setContentIntent(pendingIntent);
////
////        // Show the notification
////        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
////        manager.notify(101, builder.build());
////        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "myFirebaseChannel")
////                .setSmallIcon(R.drawable.eye)
////                .setContentTitle("title")
////                .setContentText("msg")
////                .setAutoCancel(true);
////
////        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
////        manager.notify(101, builder.build());
//
//        Intent intent = new Intent("com.example.crm_project.NOTIFICATION_RECEIVED");
//        intent.putExtra("title", title);
//        intent.putExtra("message", msg);
////        sendBroadcast(intent);
//        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
//
//    }
//    private static int requestCode = 0;
//
//    private int uniqueRequestCode() {
//        // Generate a unique request code for each PendingIntent
//        return requestCode++;
//    }
//
//    private void showSystemNotification(String title, String msg) {
//        // Handle the FCM message when the app is in the background or closed
//        // You can display a system notification to the user
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "myFirebaseChannel")
//                .setSmallIcon(R.drawable.eye)
//                .setContentTitle(title)
//                .setContentText(msg)
//                .setAutoCancel(true);
//
//        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
//        manager.notify(101, builder.build());
//    }
//}

