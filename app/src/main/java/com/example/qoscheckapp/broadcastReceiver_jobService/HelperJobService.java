package com.example.qoscheckapp.broadcastReceiver_jobService;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.qoscheckapp.HelperServiceClass;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class HelperJobService extends JobService {

    private static String TAG = "JOB_SERVICE";
    public static ServiceIntentBR serviceIntentBR;
    private static JobService jobService;
    private static JobParameters jobParameters;

    @Override
    public boolean onStartJob(JobParameters params) {
        HelperServiceClass.launchService(this);
        registerRestaterterReceiver();
        jobService = this;
        HelperJobService.jobParameters = jobParameters;

        return false;
    }

    private void registerRestaterterReceiver() {
        if (serviceIntentBR == null)
            serviceIntentBR = new ServiceIntentBR();
        else try{
            unregisterReceiver(serviceIntentBR);
        } catch (Exception e){
            // not registered
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                IntentFilter filter = new IntentFilter();
                filter.addAction("com.example.qoscheckapp");
                try {
                    registerReceiver(serviceIntentBR, filter);
                } catch (Exception e) {
                    try {
                        getApplicationContext().registerReceiver(serviceIntentBR, filter);
                    } catch (Exception ex) {

                    }
                }
            }
        }, 1000);
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.i("MAKEPING","Stopping job");
        Intent broadcastIntent = new Intent("com.example.qoscheckapp");
        sendBroadcast(broadcastIntent);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                unregisterReceiver(serviceIntentBR);
            }
        }, 1000);
        return false;
    }
}
