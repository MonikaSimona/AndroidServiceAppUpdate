package com.example.qoscheckapp;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.example.qoscheckapp.notification.ActiveServiceNotification;

import java.util.Timer;
import java.util.TimerTask;

public class ActiveService extends Service {

    protected static final int NOTIF_ID = 100;
    private int timeCounter;
    private static Service mCurrentService;
    private static Timer timer;
    private TimerTask timerTask;
    public int oldTime;
    public ActiveService() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            restartForeground();
        }
        mCurrentService = this;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
//        timeCounter = 0;

        SharedPreferences prefs= getSharedPreferences("com.example.qoscheckapp.ActiveServiceRunning", MODE_PRIVATE);

        if(prefs.getInt("timeCounter",0)!=0){
            timeCounter = prefs.getInt("counter",0);
        }

        if(intent == null){
            HelperServiceClass.launchService(this);
        }
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            restartForeground();
        }
        startTimer();

        return START_STICKY;
    }

    private void startTimer() {
        Log.i("SERVICE", "Starting timer");
        //stop the timer
        if(timer != null){
            timer.cancel();
            timer = null;
        }

        timer = new Timer();

        initializeTimerTask();

        Log.i("SERVICE", "Scheduling...");
        timer.schedule(timerTask, 1000, 1000);
    }

    private void initializeTimerTask() {

        Log.i("SERVICE", "initialising TimerTask");
        timerTask = new TimerTask() {
            public void run() {
                Log.i("in timer", "in timer ++++  " + (timeCounter++));
            }
        };
    }

    private void restartForeground() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            try {
                ActiveServiceNotification notification = new ActiveServiceNotification();
                startForeground(NOTIF_ID,notification.setNotification(this, "Service notification", "This is the service's notification", R.drawable.ic_android));
                Log.i("SERVICE","restartng foreground successful");
                startTimer();
            }catch (Exception e){
                Log.e("SERVICE", "Error in notification " + e.getMessage());
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            SharedPreferences prefs= getSharedPreferences("uk.ac.shef.oak.ServiceRunning", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("timeCounter", timeCounter);
            editor.apply();

        } catch (NullPointerException e) {
            Log.e("SERVER", "Error " +e.getMessage());

        }

        Intent broadcastIntent = new Intent("com.example.qoscheckapp");
        sendBroadcast(broadcastIntent);
        //stop the timer
        if(timer != null){
            timer.cancel();
            timer = null;
        }
        try {

            //Long.i("MoveMore", "Saving readings to preferences");
        } catch (NullPointerException e) {
            Log.e("SERVER", "error saving: are you testing?" +e.getMessage());

        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.i("SERVICE", "onTaskRemoved called");

        Intent broadcastIntent = new Intent("com.example.qoscheckapp");
        sendBroadcast(broadcastIntent);
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
