package com.example.user.ddkd;

import android.app.Application;
import android.os.Handler;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by User on 2016-04-07.
 */
public class MyApplication extends Application {
    private static Handler handler;
    public static RequestQueue requestQueue;
    public static final int XG_TEXT_MESSAGE=1;
    @Override
    public void onCreate() {
        super.onCreate();
        requestQueue = Volley.newRequestQueue(this);
    }
    public static RequestQueue getQueue(){
        return requestQueue;
    }

    public static Handler getHandler() {
        return handler;
    }

    public static void setHandler(Handler handler) {
        MyApplication.handler = handler;
    }
}
