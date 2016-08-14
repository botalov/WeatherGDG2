package com.example.botalovns.weathergdg.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WebHelper {

    public static String GoToURL(String url, String request)
    {
        String outtext1="";
        url = url.replaceFirst("https", "http");


        HttpURLConnection urlConnection = null;
        try
        {
            urlConnection = (HttpURLConnection) new URL(url).openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
            out.println(request);
            out.close();

            InputStream tmptmp = urlConnection.getInputStream();
            InputStream in = new BufferedInputStream(tmptmp);
            outtext1 = IStoString(in);
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            urlConnection.disconnect();
        }
        return outtext1;
    }

    private static String IStoString(InputStream in)
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    public static boolean isNetworkAvailable(Context context) {

        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        catch (Exception exc){
            return false;
        }
    }
}
