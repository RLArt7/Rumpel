package com.example.harelavikasis.rumpel.PushNotifications;

import java.util.ArrayList;

public class PushNotificationObject {

    private String to;
    private ArrayList<String> registration_ids;
    private AdditionalData data;

    public PushNotificationObject(ArrayList<String> registration_ids, AdditionalData data) {
        this.registration_ids = registration_ids;
        this.data = data;
    }

    public PushNotificationObject(String to, AdditionalData data) {
        this.to = to;
        this.data = data;
    }

    public String getTo() {
        return to;
    }

    public AdditionalData getData() {
        return data;
    }

    public ArrayList<String> getRegistrationIds() {
        return registration_ids;
    }

    public static class AdditionalData {
        private String title;
        private String body;

        public AdditionalData(String massage) {
            title = "Rumpel App";
            this.body = massage;

        }

        public String getMassage() {
            return body;
        }

    }
}
