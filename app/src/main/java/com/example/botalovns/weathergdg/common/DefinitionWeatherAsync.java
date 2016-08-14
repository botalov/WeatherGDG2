package com.example.botalovns.weathergdg.common;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.botalovns.weathergdg.R;
import com.example.botalovns.weathergdg.helpers.WeatherHelper;
import com.example.botalovns.weathergdg.models.CityModel;
import com.example.botalovns.weathergdg.models.WeatherModel;

public class DefinitionWeatherAsync  extends AsyncTask {
    public AsyncResponseWeather delegate = null;
    public void setDelegate(AsyncResponseWeather delegate){
        this.delegate = delegate;
    }

    private ProgressDialog dialogGetWeather;
    private CityModel city;
    private Context context;
    public DefinitionWeatherAsync(CityModel city, Context context){
        this.city=city;
        this.context=context;
    }


    @Override
    protected void onPreExecute() {
        try {
            if(dialogGetWeather==null && context!=null) {
                dialogGetWeather = new ProgressDialog(context);
                dialogGetWeather.setMessage(context.getString(R.string.message_dialog_get_weather));
                dialogGetWeather.setIndeterminate(true);
                dialogGetWeather.setCancelable(false);
                dialogGetWeather.setCanceledOnTouchOutside(false);
                dialogGetWeather.show();
            }
        }
        catch (Exception exc){
            Log.println(1, "", exc.toString());
        }
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        return  WeatherHelper.getWeather(city, context);
    }

    @Override
    protected void onPostExecute(Object result) {
        if (dialogGetWeather != null) {
            dialogGetWeather.dismiss();
        }
        if(result==null){
            Toast.makeText(
                    context,
                    context.getString(R.string.error_get_weather),
                    Toast.LENGTH_LONG
            ).show();
        }
        else{
            try {
                WeatherModel weatherModel = (WeatherModel) result;
                delegate.processFinish(weatherModel);

            }
            catch (Exception exc){
                exc.printStackTrace();
            }

        }
    }

    public interface AsyncResponseWeather {
        void processFinish(WeatherModel output);
    }
}