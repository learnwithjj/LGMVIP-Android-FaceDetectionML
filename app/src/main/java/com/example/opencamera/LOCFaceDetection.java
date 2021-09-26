package com.example.opencamera;

import android.app.Application;

import com.google.firebase.FirebaseApp;

public class LOCFaceDetection  extends Application {

    public static final String result_text="result_text";
    public static final String result_dialog="result_dialog";

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this) ;}
}
