package com.example.dz.myapplication.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import com.example.dz.myapplication.gson.Weather;
import com.example.dz.myapplication.util.HttpUtil;
import com.example.dz.myapplication.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by captain on 2017/5/16.
 */

public class AutoUpdateService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent,int flags, int startId) {
        updateWeather();
        updatePicture();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 8*6*6*1000;//八小时的毫秒数
        long triggerTime = SystemClock.elapsedRealtime()+anHour;
        Intent i = new Intent(this,AutoUpdateService.class);
        PendingIntent pendIntent = PendingIntent.getService(this,0, i,0);
        manager.cancel(pendIntent);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerTime,pendIntent);
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 更新天气
     */
    private void updateWeather() {
        SharedPreferences prefer = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefer.getString("weather",null);
        if(weatherString!=null){
            Weather weather = Utility.handleWeatherResponse(weatherString);
           String weatherID = weather.basic.weatherId;
            String weatherUrl = "htp://guolin.tech/api/weather?cityid="+weatherID+"&key=cb11e0ae2f6942e8bdb6cc6e7ce06705";
            HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                    Weather weather = Utility.handleWeatherResponse(responseText);
                    if(weather!=null&&"ok".equals(weather.status)){
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                        editor.putString("weather",responseText);
                        editor.apply();
                    }
                }
            });
        }
    }

    /**
     * 更新图片
     */
    private void updatePicture() {
        String requestBasic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBasic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String bingPic = response.body().string();
                SharedPreferences .Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                editor.putString("bing_pic",bingPic);
                editor.apply();
            }
        });
    }
}
