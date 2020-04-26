package com.example.qoscheckapp.network_task;

import android.os.AsyncTask;
import android.util.Log;

public class NetworkPingTask extends AsyncTask<Void,Void,String>{

    @Override
    protected String doInBackground(Void... voids) {

        return getJson.getBackendJson();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
