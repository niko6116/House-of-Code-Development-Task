package com.niko.houseofcodedevelopmenttask;

import android.app.Application;

import com.firebase.client.Firebase;

public class DevelopmentTask extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }

}
