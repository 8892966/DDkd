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
    public static final int GET_TOKEN_SUCCESS=2;
    public static final int GET_TOKEN_ERROR=3;
    public static final int XG_TEXT_USERCANCEL=4;
    public static int state = 0;//0为非登录状态，1为登录状态
    public static final String url="http://www.louxiago.com/wc/ddkdtest/admin.php/";

    @Override
    public void onCreate() {
        super.onCreate();
        //腾讯统计分析
//        StatService.registerActivityLifecycleCallbacks(this);
        requestQueue = Volley.newRequestQueue(this);
    }
    public static RequestQueue getQueue(){
        return requestQueue;
    }

    public static Handler getHandler(){
        return handler;
    }

    public static void setHandler(Handler handler) {
        MyApplication.handler = handler;
    }
}
