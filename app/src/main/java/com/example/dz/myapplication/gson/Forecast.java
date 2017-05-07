package com.example.dz.myapplication.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by captain on 2017/5/7.
 */

public class Forecast {
    public String date;
    @SerializedName("tmp")
    public Temperature temperture;

    @SerializedName("cond")
    public More more;
    public class Temperature{
        public String max;
        public String min;
    }
    public class More{
        @SerializedName("txt_d")
        public String info;
    }
}
