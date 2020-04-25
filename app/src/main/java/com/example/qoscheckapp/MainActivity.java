package com.example.qoscheckapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import com.example.qoscheckapp.broadcastReceiver_jobService.ServiceIntentBR;

public class MainActivity extends AppCompatActivity {

    Context main_context;
    public Context getCtx() {
        return main_context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        main_context = this;

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            ServiceIntentBR.scheduleJob(getApplicationContext());
        }else{
            HelperServiceClass.launchService(getApplicationContext());
        }
        finish();
    }
}
