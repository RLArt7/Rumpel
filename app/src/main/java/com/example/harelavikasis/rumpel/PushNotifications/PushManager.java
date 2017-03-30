package com.example.harelavikasis.rumpel.PushNotifications;


import java.util.ArrayList;


public class PushManager {

    private final String massage;
    private final String token;

    public PushManager(String token, String massage) {
        this.token = token;
        this.massage =massage;
    }

    public void sendPush(){
        NotificationsClient.getInstance().sendMsgPushNotification(
                new PushNotificationObject(token,
                        new PushNotificationObject.AdditionalData(massage)));
    }

}
