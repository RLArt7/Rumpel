package com.example.harelavikasis.rumpel.PushNotifications;

import android.util.Log;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NotificationsClient {

    private static final String TAG = NotificationsClient.class.getName();
    private static NotificationsClient instance;
    private NotificationsService mNotificationsService;
    private int mNumMessages;
    private List<String> mNotificationMessages;
    private int counter = 0;

    public static final String FCM_URL = "https://fcm.googleapis.com/fcm/";
    public static final String FCM_KEY = "key=AIzaSyBx4KIpVdY7QxDK5ITpjsG11G1rkZ7eHDY";

    private NotificationsClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(FCM_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mNotificationsService = retrofit.create(NotificationsService.class);

        mNumMessages = 0;
        mNotificationMessages = new ArrayList<>();
    }

    public static NotificationsClient getInstance() {
        if(instance == null){
            instance = new NotificationsClient();
        }
        return instance;
    }

    public void sendMsgPushNotification(PushNotificationObject data) {
        mNotificationsService.sendPushNotification(data).enqueue(new Callback<Object>() {
            @Override public void onResponse(Call<Object> call, Response<Object> response) {
                Log.d(TAG, "sendMsgPushNotification onResponse: counter: " + response.body() !=null ? response.body().toString() : "");
            }

            @Override public void onFailure(Call<Object> call, Throwable t) {
                Log.e(TAG, "sendMsgPushNotification onFailure: counter: ");
            }
        });
    }

    public int getNumMessages() {
        mNumMessages++;
        return mNumMessages;
    }

    public List<String> getNotificationMessages() {
        return mNotificationMessages;
    }

    public void clearMessagesAndNumber(){
        mNumMessages = 0;
        mNotificationMessages.clear();
    }

    public void addMessage(String message){
        mNotificationMessages.add(message);
    }
}
