package com.example.user.ddkd.utils;

import android.app.Activity;
import android.app.AlarmManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
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

    public void volley_QDGD_GET(final String id, final Activity activity, final Handler handler1){
        SharedPreferences preferences = activity.getSharedPreferences("config", Context.MODE_PRIVATE);
        String token = preferences.getString("token", "");
        String url = "http://www.louxiago.com/wc/ddkd/admin.php/Order/RobBespeakOrder/orderId/"+id+"/token/" + token;
//        Log.e("volley_OrderState_GET", url);
        final StringRequest request_post = new StringRequest(Request.Method.GET, url, new MyStringRequest() {
            @Override
            public void success(Object o) {
                try {
                    String ss = (String) o;
                    if("ROB SUCCESS".equals(ss)){
                        Toast.makeText(activity,"抢单成功",Toast.LENGTH_SHORT).show();
                    }else if("ROB FAIL".equals(ss)){
                        Toast.makeText(activity,"抢单不成功",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(activity,"操作失败",Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    Log.e("Exception", e.getMessage());
                    Toast.makeText(activity, "信息有误!!!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void tokenouttime() {
                AutologonUtil autologonUtil = new AutologonUtil(activity, handler1, id);
                autologonUtil.volley_Get_TOKEN();
            }
            @Override
            public void yidiensdfsdf() {
                Toast.makeText(activity, "您的账号在其他地方被登陆，请在此登陆", Toast.LENGTH_SHORT).show();
                Exit.exit(activity);
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(activity, "网络连接中断", Toast.LENGTH_SHORT).show();
            }
        });
        request_post.setTag("volley_QDGD_GET");
        MyApplication.getQueue().add(request_post);
    }

    public void volley_GDMSG_GET_UTILS( final Activity activity,final Handler handler1){
        SharedPreferences preferences = activity.getSharedPreferences("config", Context.MODE_PRIVATE);
        String token = preferences.getString("token", "");
        String url = "http://www.louxiago.com/wc/ddkd/admin.php/Order/getBespeakOrder/token/" + token;
        final StringRequest request_post = new StringRequest(Request.Method.GET, url, new MyStringRequest() {
            @Override
            public void success(Object o) {
                try {
                    String ss = (String) o;
                    if(!"NODATA".equals(ss)) {
                        Log.e("volley_MSG_GET", ss);
                        Gson gson = new Gson();
                        List<QOrderInfo> list = gson.fromJson(ss, new TypeToken<List<QOrderInfo>>() {
                        }.getType());
                        getOrder(list);
                    }else {

                    }
                }catch (Exception e){
                    Log.e("Exception", e.getMessage());
                    Toast.makeText(activity, "信息有误!!!3333", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void tokenouttime() {
                AutologonUtil autologonUtil = new AutologonUtil(activity, handler1, null);
                autologonUtil.volley_Get_TOKEN();
            }
            @Override
            public void yidiensdfsdf() {
                Toast.makeText(activity, "您的账号在其他地方被登陆，请在此登陆", Toast.LENGTH_SHORT).show();
                Exit.exit(activity);
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError){
                Toast.makeText(activity, "网络连接中断", Toast.LENGTH_SHORT).show();
            }
        });
        request_post.setTag("volley_GDMSG_GET_UTILS");
        MyApplication.getQueue().add(request_post);
    }
    public abstract void getOrder(Object o);
}
