package com.example.harelavikasis.rumpel.Services;


import android.util.Log;
import android.widget.Toast;

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
    }
}
