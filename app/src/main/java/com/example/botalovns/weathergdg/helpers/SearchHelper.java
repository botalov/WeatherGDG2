package com.example.botalovns.weathergdg.helpers;

import android.content.Context;
import android.os.AsyncTask;

import com.example.botalovns.weathergdg.common.AsyncResponse;
import com.example.botalovns.weathergdg.models.CityModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchHelper extends AsyncTask {
    public AsyncResponse delegate = null;

    private Context context;
    private String url;

    public SearchHelper(Context context, String url){
        this.context = context;
        this.url = url;
    }
    public SearchHelper(){
    }

    public void setDelegate(AsyncResponse delegate){
        this.delegate = delegate;
    }
    public void setContext(Context context){
        this.context = context;
    }
    public void setUrl(String url){
        this.url = url;
    }

    @Override
    protected void onPreExecute() {

    }
    @Override
    protected ArrayList<CityModel> doInBackground(Object[] objects) {
        //return null;
        ArrayList<CityModel> result = new ArrayList<>();

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(WebHelper.GoToURL(url, ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray gNames = new JSONArray();
        try {
            if (jsonObject!=null)
                gNames = jsonObject.getJSONArray("geonames");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (gNames.length() > 0)
        {
            result.clear();
        }
        for (int i = 0; i < gNames.length(); i++)
        {
            JSONObject c = new JSONObject();
            String cName = "";
            long population  = 0L;
            double lat = 0.0;
            double lng = 0.0;
            String tzStr = "GMT+00:00";

            try {
                c = gNames.getJSONObject(i);
                population = c.getLong("population");

                if (population > 0L)
                {
                    cName = c.getString("name");
                    lat = c.getDouble("lat");
                    lng = c.getDouble("lng");

                    JSONArray names = null;
                    try{
                        names = c.getJSONArray("alternateNames");
                    }
                    catch (JSONException e){
                        e.printStackTrace();
                    }
                    if(names!=null && names.length()>0){
                        for(int j=0; j<names.length(); j++){
                            JSONObject nameObject = names.getJSONObject(j);
                            if(nameObject!=null){
                                try{
                                    if(nameObject.getString("lang").equals("ru")){
                                        cName = nameObject.getString("name");
                                    }
                                }
                                catch (JSONException exc){
                                    exc.printStackTrace();
                                }
                            }
                        }
                    }

                    CityModel city = new CityModel();
                    city.setName(cName);
                    city.setCoordinates(lat, lng);
                    result.add(city);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    @Override
    protected void onPostExecute(Object result) {
        try {
            delegate.processFinish((ArrayList<CityModel>) result);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
