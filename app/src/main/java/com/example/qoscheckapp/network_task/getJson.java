package com.example.qoscheckapp.network_task;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class getJson {

    public static final String URL ="http://10.0.2.2:5000/getjobs";

    public static String getBackendJson(){
        String json = null;
        HttpURLConnection httpURLConnection = null;
        BufferedReader reader = null;
        try {
            Uri buildUri = Uri.parse(URL).buildUpon().build();
            URL requestURL = new URL(buildUri.toString());

            httpURLConnection = (HttpURLConnection) requestURL.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            InputStream inputStream = httpURLConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            if(inputStream == null){
                return  null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine())!= null){
                buffer.append(line +"\n");
            }
            if(buffer.length() == 0){
                return null;
            }json = buffer.toString();

        }catch (IOException e){
            e.printStackTrace();
            return null;

        } finally {
            if (httpURLConnection!=null){
                httpURLConnection.disconnect();
            }
            if (reader!= null){
                try {
                    reader.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        Log.i("GET_JSON", "getBackendJson: " + json);
            return json;

    }
}
