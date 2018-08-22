package com.great3.smartactivatedcollegewebbook;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by Glenn on 1/21/2018.
 */
public class SmartActivatedCollegeWebBook extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
/*
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
    */
}
