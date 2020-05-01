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
        Log.i("MAKEPING","pocnuva doInBackground");

        try {
            String jsonData = getJson.getBackendJson();
            JSONArray jsonArray = new JSONArray(jsonData);
            for(int i=0; i<jsonArray.length(); i++){
                final JSONObject jsonObject = jsonArray.getJSONObject(i);
                 host = jsonObject.getString("host");
                 count = jsonObject.getInt("count");
                 packetSize = jsonObject.getInt("packetSize");
                jobPeriod = jsonObject.getInt("jobPeriod");
//                String jobType = jsonObject.getString("jobtType");


                for(int j=0; j<=(int)(600/jobPeriod);j++){
                    int s = (int)(600/jobPeriod)+1;
                    Log.i("MAKEPING","vrti " + s + " pati ");
                    ping = makePing(host,count,packetSize);
                    Log.i("MAKEPING",ping);
                    try{
                        Thread.sleep(jobPeriod*1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return  null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
    public String makePing(String Host , int Count, int PacketSize){
        String Ping = "";
        try{

            String pingCmd = "ping -s " + PacketSize + " -c "+ Count + " " + Host;
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec(pingCmd);
            input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            line = " ";
            while ((line = input.readLine()) != null){
//                Log.i("PINGLINE", "makePing " + line);
                Ping += line;

            }
            input.close();
//            Log.i("PING", "makePing: " + Ping);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return Ping;
    }
}
