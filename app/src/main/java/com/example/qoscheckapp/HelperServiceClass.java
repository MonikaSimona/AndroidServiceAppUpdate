package com.example.qoscheckapp;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class HelperServiceClass {

    public static final String TAG = "HELPER_CLASS";

    public HelperServiceClass(){}

    public static void launchService(Context context) {
        if(context==null){
            return;
        }

        Intent serviceIntent = new Intent(context,ActiveService.class);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            context.startForegroundService(serviceIntent);
        }else{
            context.startService(serviceIntent);
        }
        Log.d(TAG,"From HelperServiceClass: let the service start!");
    }
}
