package com.example.qoscheckapp.network_task;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.BufferOverflowException;

public class NetworkPingTask extends AsyncTask<Void,Void,String>{
    public static String ping;
    public String host;
    public int count;
    public int packetSize;
    public  int jobPeriod;
    public BufferedReader input;
    public String line;


    @Override
    protected String doInBackground(Void... voids) {

        try {
            String jsonData = getJson.getBackendJson();
            JSONArray jsonArray = new JSONArray(jsonData);
            for(int i=0; i<jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                 host = jsonObject.getString("host");
                 count = jsonObject.getInt("count");
                 packetSize = jsonObject.getInt("packetSize");
//                 jobPeriod = jsonObject.getInt("jobPeriod");
//                String jobType = jsonObject.getString("jobtType");

                try{

                    String pingCmd = "ping -s " + packetSize + " -c "+ count + " " + host;
                    Runtime runtime = Runtime.getRuntime();
                    Process process = runtime.exec(pingCmd);
                    input = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    line = " ";
                    while ((line = input.readLine()) != null){
                        Log.i("PINGLINE", "doInBackground: " + line);
                        ping += line;

                    }
                    input.close();
                    Log.i("PING", "doInBackground: " + ping);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ping;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
