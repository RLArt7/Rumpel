package com.example.harelavikasis.rumpel.PushNotifications;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface NotificationsService {

    @Headers({"Content-Type: application/json", "Authorization: "+ NotificationsClient.FCM_KEY})
    @POST("send")
    Call<Object> sendPushNotification(@Body PushNotificationObject data);
}
