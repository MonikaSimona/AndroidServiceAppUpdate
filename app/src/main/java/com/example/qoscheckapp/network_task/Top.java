package com.example.qoscheckapp.network_task;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Top {
    public static final String URL ="http://10.0.2.2:5000/postresults";

    public static void doTop(){
        String returnString = null;
        String statResult = "";
        try {
            Process pstat = Runtime.getRuntime().exec("top -n 1");
            BufferedReader in = new BufferedReader(new InputStreamReader(pstat.getInputStream()));
            String inputLine;

            while (returnString == null || returnString.contentEquals("")) {
                returnString = in.readLine();
            }
            statResult += returnString +",";
            while ((inputLine = in.readLine()) != null) {
                inputLine += ";";
                statResult += inputLine;
            }
            in.close();
            if (pstat != null) {
                pstat.getOutputStream().close();
                pstat.getInputStream().close();
                pstat.getErrorStream().close();
            }
            Log.i("MAKEPING","statResult = "+statResult);
            postJson.postBackendJson(statResult);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
