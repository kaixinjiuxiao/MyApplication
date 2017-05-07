package com.example.dz.myapplication.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by captain on 2017/5/7.
 */

public class Now {
    @SerializedName("tmp")
    public String temperture;
    @SerializedName("cond")
    public More more;
    public class More{
        @SerializedName("txt")
        public String info;
    }
}
