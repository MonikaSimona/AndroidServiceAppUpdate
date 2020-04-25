package com.example.qoscheckapp.broadcastReceiver_jobService;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.qoscheckapp.HelperServiceClass;

public class ServiceIntentBR extends BroadcastReceiver {

    public static final String TAG = "BROADCAST_RECEIVER";
    private static JobScheduler jobScheduler;
    private ServiceIntentBR serviceIntentBR;
    public static final int JOB_ID = 1;

    @Override
    public void onReceive(final Context context, Intent intent) {

        Log.d(TAG,"the timer will start "+ context.toString());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
           scheduleJob(context);

        }else{
            registerRestarterReceiver(context);
            HelperServiceClass.launchService(context);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void scheduleJob(Context context) {
        if(jobScheduler == null){
            jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        }
        ComponentName componentName = new ComponentName(context,HelperJobService.class);
        JobInfo jobInfo = new JobInfo.Builder(JOB_ID,componentName)
                .setOverrideDeadline(0)
                .setPersisted(true)
                .build();
        jobScheduler.schedule(jobInfo);
    }

    private void registerRestarterReceiver(final Context context) {

        if (serviceIntentBR == null)
            serviceIntentBR = new ServiceIntentBR();
        else try{
            context.unregisterReceiver(serviceIntentBR);
        } catch (Exception e){
            // not registered
        }
        // give the time to run
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                IntentFilter filter = new IntentFilter();
                filter.addAction("com.example.qoscheckapp");
                try {
                    context.registerReceiver(serviceIntentBR, filter);
                } catch (Exception e) {
                    try {
                        context.getApplicationContext().registerReceiver(serviceIntentBR, filter);
                    } catch (Exception ex) {

                    }
                }
            }
        }, 1000);

    }
}
