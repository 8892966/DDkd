package com.example.user.ddkd.utils;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

/**
 * Created by User on 2016-04-26.
 */
public abstract class MyStringRequest implements Response.Listener {
    @Override
    public void onResponse(Object o) {
        String s = o.toString();
        if (s.endsWith("\"token outtime\"")) {
            Log.e("volley_getOrder_GET", "token过时了");
            tokenouttime();
        }else if(s.equals("yididenglu")){
            Log.e("volley_getOrder_GET", "yide");
            yidiensdfsdf();
        }else{
            Log.e("volley_getOrder_GET", "ok");
            success(s);
        }
    }
    public abstract void  success(Object o);
    public abstract void  tokenouttime();
    public abstract void  yidiensdfsdf();
}
