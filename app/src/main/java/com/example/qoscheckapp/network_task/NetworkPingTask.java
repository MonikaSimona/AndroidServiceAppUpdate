package com.example.qoscheckapp.network_task;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.SharedMemory;
import android.util.Log;

import com.example.qoscheckapp.ActiveService;
import com.example.qoscheckapp.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.BufferOverflowException;

import static android.content.Context.MODE_PRIVATE;

public class NetworkPingTask extends AsyncTask<Void,Void,String>{
    public static String ping;
    public String host;
    public int count;
    public int packetSize;
    public  int jobPeriod;
    public BufferedReader input;
    public String line;
    public  String jobType;
    public String resultOk;
    public NetworkCheck networkCheck;
    public Context mContext;
    private SharedPreferences prefs;
    public NetworkPingTask(Context context){
        mContext=context;
    }


    @Override
    protected String doInBackground(Void... voids) {

        networkCheck =  new NetworkCheck(mContext.getApplicationContext());

            Top.doTop();

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
                jobType = jsonObject.getString("jobType");

                Log.i("MAKEPING","JOB TYPE = " + jobType);

                for(int j=0; j<=(int)(600/jobPeriod);j++) {

//                    if(jobType=="PING") {
                    makePing(host, count, packetSize);
//                    }

                    try {
                        Thread.sleep(jobPeriod * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        } catch (JSONException  e) {
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
                Ping += line;

            }
            input.close();
            Log.i("MAKEPING",Ping);
            prefs = mContext.getSharedPreferences("com.example.qoscheckapp.ActiveServiceRunning", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            if(networkCheck.networkCheck()){
                postJson.postBackendJson(Ping);
                if(prefs.getString("result1",null)!= null){
                    postJson.postBackendJson(prefs.getString("result1",null));
                    editor.putString("result1",null);
                }
                if(prefs.getString("result2",null)!= null){
                    postJson.postBackendJson(prefs.getString("result2",null));
                    editor.putString("result2",null);
                }
                else{
                    if((prefs.getString("result1",null)!=null ) && (prefs.getString("result1",null)!=null)
                    || (prefs.getString("result1",null)==null ) && (prefs.getString("result1",null)==null) ){
                        editor.putString("result1",Ping);
                    }else {
                        editor.putString("result2",Ping);
                    }
                    editor.apply();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return Ping;
    }
}
