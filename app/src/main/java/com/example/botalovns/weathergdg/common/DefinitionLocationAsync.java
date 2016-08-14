package com.example.botalovns.weathergdg.common;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.botalovns.weathergdg.R;
import com.example.botalovns.weathergdg.helpers.LocationHelper;
import com.example.botalovns.weathergdg.models.CityModel;
import com.example.botalovns.weathergdg.models.WeatherModel;

public class DefinitionLocationAsync extends AsyncTask {

    public AsyncResponseLocation delegate;
    public void setDelegate(AsyncResponseLocation delegate){
        this.delegate = delegate;
    }

    private ProgressDialog dialogGetLocation;
    private Context context;
    public DefinitionLocationAsync(Context context){
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        try {
            if(dialogGetLocation==null && context!=null) {
                dialogGetLocation = new ProgressDialog(context);
                dialogGetLocation.setMessage(context.getString(R.string.message_dialog_get_location));
                dialogGetLocation.setIndeterminate(true);
                dialogGetLocation.setCancelable(false);
                dialogGetLocation.setCanceledOnTouchOutside(false);
                dialogGetLocation.show();
            }
        }
        catch (Exception exc){
            Log.println(1, "", exc.toString());
        }
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        return LocationHelper.getCurrentLocation(context);
    }

    @Override
    protected void onPostExecute(final Object result) {
        if(dialogGetLocation!=null){
            dialogGetLocation.dismiss();
        }

        if(result==null){
            Toast.makeText(
                    context,
                    context.getString(R.string.error_get_location),
                    Toast.LENGTH_LONG
            ).show();
        }else{
            final CityModel city = (CityModel)result;

            DefinitionWeatherAsync definitionWeatherAsync = new DefinitionWeatherAsync(city, context);
            definitionWeatherAsync.setDelegate(new DefinitionWeatherAsync.AsyncResponseWeather() {
                @Override
                public void processFinish(WeatherModel output) {
                    city.setWeather(output);
                    delegate.processFinish(city);
                }
            });
            definitionWeatherAsync.execute();
        }
    }

    public interface AsyncResponseLocation {
        void processFinish(CityModel output);
    }
}