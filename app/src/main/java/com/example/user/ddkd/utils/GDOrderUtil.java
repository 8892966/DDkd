package com.example.user.ddkd.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.user.ddkd.MyApplication;
import com.example.user.ddkd.beam.QOrderInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by User on 2016-05-06.
 */
public abstract class GDOrderUtil {

    public void volley_GDMSG_GET_UTILS(final Context context){
        SharedPreferences preferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        String token = preferences.getString("token", "");
        String url = "http://www.louxiago.com/wc/ddkd/admin.php/Order/CountOrder/token/" + token;
//        Log.e("volley_OrderState_GET", url);
        StringRequest request_post = new StringRequest(Request.Method.GET, url, new MyStringRequest() {
            @Override
            public void success(Object o) {
                try {
                    String ss = (String) o;
                    Log.e("volley_MSG_GET", ss);
                    Gson gson=new Gson();
                    List<QOrderInfo> list=gson.fromJson(ss, new TypeToken<List<QOrderInfo>>() {}.getType());
                    getOrder(list);
                }catch (Exception e){
                    Log.e("Exception", e.getMessage());
                    Toast.makeText(context, "信息有误!!!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void tokenouttime() {
            }
            @Override
            public void yidiensdfsdf() {
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(context, "网络连接中断", Toast.LENGTH_SHORT).show();
            }
        });
        request_post.setTag("volley_GDMSG_GET");
        MyApplication.getQueue().add(request_post);
    }
    public abstract void getOrder(Object o);
}
