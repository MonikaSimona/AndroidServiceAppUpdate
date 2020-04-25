package com.example.qoscheckapp.network_task;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import static androidx.core.content.ContextCompat.getSystemService;

public class NetworkCheck {
    public boolean connection;
    public Context mContext;

    public  NetworkCheck(Context context){
        mContext=context;

    }

    public boolean networkCheck(){

        ConnectivityManager connMgr = (ConnectivityManager) mContext.getSystemService(mContext.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null) {
            connection = true;
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                connection = true;
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                connection = true;
            }
        } else {
            connection = false;
        }

        return connection;
    }
}
