package com.example.user.ddkd;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by User on 2016-04-07.
 */
public class MyApplication extends Application {
    public static RequestQueue requestQueue;
    @Override
    public void onCreate() {
        super.onCreate();
        requestQueue = Volley.newRequestQueue(this);
    }
    public static RequestQueue getQueue(){
        return requestQueue;
    }
}
