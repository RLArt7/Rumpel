package com.example.harelavikasis.rumpel;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.google.firebase.FirebaseApp;

/**
 * Created by harelavikasis on 14/03/2017.
 */

public class RumpelMainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.setApplicationId(getString(R.string.facebook_app_id));
        FacebookSdk.sdkInitialize(getApplicationContext());
        FirebaseApp.initializeApp(this);
    }
}
