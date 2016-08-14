package com.example.botalovns.weathergdg.helpers;

import android.content.Context;

import com.example.botalovns.weathergdg.R;
import com.example.botalovns.weathergdg.enums.CloudinessEnum;
import com.example.botalovns.weathergdg.enums.DirectionOfTheWindEnum;

public class ConvertHelper {

    public static int hPaTOmmHg(int pressure){
        return (int)(pressure * 0.75006375541921);
    }

    // Конвертирует градусы в направление ветра
    public static DirectionOfTheWindEnum degTOdirectionWind(double deg){
        DirectionOfTheWindEnum result = DirectionOfTheWindEnum.NONE;
        try{
            if(deg>=11.25 && deg<56.25){
                result = DirectionOfTheWindEnum.NORTHEAST;
            }
            else if(deg>=56.25 && deg<101.25){
                result = DirectionOfTheWindEnum.EAST;
            }
            else if(deg>=101.25 && deg<146.25){
                result = DirectionOfTheWindEnum.SOUTHEAST;
            }
            else if(deg>=146.25 && deg<191.25){
                result = DirectionOfTheWindEnum.SOUTH;
            }
            else if(deg>=191.25 && deg<236.25){
                result = DirectionOfTheWindEnum.SOUTHWEST;
            }
            else if(deg>=236.25 && deg<281.25){
                result = DirectionOfTheWindEnum.WEST;
            }
            else if(deg>=281.25 && deg<326.25){
                result = DirectionOfTheWindEnum.NORTHWEST;
            }
            else if((deg>=326.25 && deg<=360) || (deg>=0 && deg<11.25)){
                result = DirectionOfTheWindEnum.NORTH;
            }
            else {
                result = DirectionOfTheWindEnum.NONE;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public static String getNameOfDirectionWind(DirectionOfTheWindEnum directionOfTheWindEnum, Context context){
        String result = context.getString(R.string.wind_NONE);
        try{
            switch (directionOfTheWindEnum){
                case EAST: {
                    result = context.getString(R.string.wind_EAST);
                    break;
                }
                case NORTH: {
                    result = context.getString(R.string.wind_NORTH);
                    break;
                }
                case NORTHEAST: {
                    result = context.getString(R.string.wind_NORTHEAST);
                    break;
                }
                case NORTHWEST: {
                    result = context.getString(R.string.wind_NORTHWEST);
                    break;
                }
                case SOUTH: {
                    result = context.getString(R.string.wind_SOUTH);
                    break;
                }
                case SOUTHEAST: {
                    result = context.getString(R.string.wind_SOUTHEAST);
                    break;
                }
                case SOUTHWEST: {
                    result = context.getString(R.string.wind_SOUTHWEST);
                    break;
                }
                case WEST: {
                    result = context.getString(R.string.wind_WEST);
                    break;
                }
                default:{
                    result = context.getString(R.string.wind_NONE);
                    break;
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }


    public static CloudinessEnum getCloudinessEnum(int cloudiness){
        CloudinessEnum result = CloudinessEnum.NONE;
        try{
            if((int)(cloudiness/100) == 5){
                result = CloudinessEnum.RAIN;
            }
            else if(cloudiness == 800){
                result = CloudinessEnum.SUN;
            }
            else if(cloudiness == 803 || cloudiness == 804){
                result = CloudinessEnum.SUN_CLOUDY;
            }
            else if((int)(cloudiness/100) == 8){
                result = CloudinessEnum.CLODY;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }
}
