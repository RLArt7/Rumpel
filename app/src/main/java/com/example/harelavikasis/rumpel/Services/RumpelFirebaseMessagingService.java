package com.example.harelavikasis.rumpel.Services;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.harelavikasis.rumpel.Login.MainLoginActivity;
import com.example.harelavikasis.rumpel.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Created by harelavikasis on 29/03/2017.
 */

public class RumpelFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO: Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated.
        String from = remoteMessage.getFrom();
        Map data = remoteMessage.getData();
//        Toast.makeText(getActivity(), (String)data.result,
//                Toast.LENGTH_LONG).show();
//        Log.d(TAG, "From: " + remoteMessage.getFrom());
//        if (remoteMessage.getNotification().getBody() != null) {
//            Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
//        }
        RumpelFirebaseMessagingService.showNotification(getApplicationContext(),data.get("title").toString(),data.get("body").toString());

    }
    public static void showNotification(Context context , String title, String content) {

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            Notification notification = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setVibrate(new long[]{300, 300, 300, 300})
                    .setLights(Color.BLUE, 1, 1)
                    .setSound(defaultSoundUri)
                    .setContentIntent(createPendingIntent(context))
                    .setGroupSummary(true)
                    .build();

            NotificationManager manager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(0, notification);
    }

    private static PendingIntent createPendingIntent(Context context) {
        Intent intent = new Intent(context, MainLoginActivity.class);
        intent.putExtra("fromPush",true);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        return PendingIntent.getActivity(context, 0, intent, 0);
    }
}
