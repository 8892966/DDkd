package com.example.user.ddkd.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.user.ddkd.JieDangActivity;
import com.example.user.ddkd.MainActivity_login;
import com.example.user.ddkd.MyApplication;

/**
 * Created by User on 2016-04-19.
 */
public class AutologonUtil {
    private Context context;
    private Handler handler;
    private Object obj;
    private static boolean isRunning=false;
    private static int Static = 0;
    private static int number = 0;
    public AutologonUtil(Context context,Handler handler,Object obj){
        this.context=context;
        this.handler=handler;
        this.obj=obj;
    }
    public void volley_Get_TOKEN() {
        number++;
        if (!isRunning&&Static==0) {
            isRunning=true;
            MyApplication.getQueue().cancelAll("volley_Get_TOKEN");
//            Toast.makeText(context,"token过时",Toast.LENGTH_SHORT).show();
            SharedPreferences sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
            String userid = sharedPreferences.getString("phone", "");
            String password = sharedPreferences.getString("password", "");
            String url = "http://www.louxiago.com/wc/ddkd/admin.php/User/login?phone=" + userid + "&password=" + password;
            Log.e("volley_Get_TOKEN", url);
            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
//                Log.e("Get_login", s);
                    try {
                        if (!s.equals("ERROR")) {
                            s = s.substring(1, s.length() - 1);
                            //******************当提交成功以后，后台会返回一个参数来说明是否提交/验证成功******************
                            SharedPreferences sharedPreferences = context.getSharedPreferences("config", context.MODE_PRIVATE);
                            SharedPreferences.Editor edit = sharedPreferences.edit();
                            edit.putString("token", s);
//                    Log.e("volley_Get", s);
                            edit.commit();
                            Static = 1;
//                        Message message = new Message();
//                        message.obj = obj;
//                        message.what = MyApplication.GET_TOKEN_SUCCESS;
//                        handler.sendMessage(message);
//                    handler.sendEmptyMessage();
                        } else {
                            Log.i("Error", "ERROR");
                            Static = 2;
//                        handler.sendEmptyMessage(MyApplication.GET_TOKEN_ERROR);
                        }
                    } catch (Exception e) {
                        Log.e("Exception", e.getMessage());
                        Toast.makeText(context, "信息有误!!!", Toast.LENGTH_SHORT).show();
                    } finally {
                        isRunning = false;
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
//                Toast.makeText(context, "网络连接出错", Toast.LENGTH_SHORT).show();
                    Log.e("onErrorResponse", "onErrorResponse");
                    Static = 3;
                    isRunning = false;
//                handler.sendEmptyMessage(MyApplication.GET_TOKEN_ERROR);
                }
            });
            request.setTag("volley_Get_TOKEN");
            MyApplication.getQueue().add(request);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e("volley_Get_TOKEN",isRunning+"");
                while (isRunning==true){
                }
                Log.e("volley_Get_TOKEN",isRunning+"");
                if(Static==1){
                    Log.e("volley_Get_TOKEN","1");
                    Message message = new Message();
                    message.obj = obj;
                    message.what = MyApplication.GET_TOKEN_SUCCESS;
                    handler.sendMessage(message);
                }else if(Static==2){
                    Log.e("volley_Get_TOKEN", "2");
                    handler.sendEmptyMessage(MyApplication.GET_TOKEN_ERROR);
                }else if(Static==3){
                    Log.e("volley_Get_TOKEN", "3");
                    handler.sendEmptyMessage(MyApplication.GET_TOKEN_ERROR);
                }
                if((number=number-1)==0){
                    Static=0;
                }
                Log.e("volley_Get_TOKEN", Static+"");
            }
        }).start();
    }

}
