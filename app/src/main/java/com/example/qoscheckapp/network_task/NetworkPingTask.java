package com.example.qoscheckapp.network_task;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class NetworkPingTask extends AsyncTask<Void,Void,String>{
    public static String ping;

    @Override
    protected String doInBackground(Void... voids) {

        try {
            String jsonData = getJson.getBackendJson();
            JSONArray jsonArray = new JSONArray(jsonData);
            for(int i=0; i<jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String host = jsonObject.getString("host");
                int count = jsonObject.getInt("count");
                int packetSize = jsonObject.getInt("packetSize");
                int jobPeriod = jsonObject.getInt("jobPeriod");
//                String jobType = jsonObject.getString("jobtType");

                try{
                    String pingCmd = "ping -i " + jobPeriod + " -s " + packetSize + " -c "+ count + " " + host;
                    Runtime runtime = Runtime.getRuntime();
                    Process process = runtime.exec(pingCmd);
                    BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line ;
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
