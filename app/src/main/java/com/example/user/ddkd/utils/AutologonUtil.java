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
    public AutologonUtil(Context context,Handler handler,Object obj){
        this.context=context;
        this.handler=handler;
        this.obj=obj;
    }
    public void volley_Get_TOKEN() {;
        SharedPreferences sharedPreferences=context.getSharedPreferences("config", Context.MODE_PRIVATE);
        String userid=sharedPreferences.getString("phone","");
        String password=sharedPreferences.getString("password","");
        String url = "http://www.louxiago.com/wc/ddkd/admin.php/User/login/phone/" + userid + "/password/" + password;
        Log.e("volley_Get_TOKEN",url);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
//                Log.e("Get_login", s);
                if (!s.equals("ERROR")){
                    s = s.substring(1, s.length() - 1);
                    //******************当提交成功以后，后台会返回一个参数来说明是否提交/验证成功******************
                    SharedPreferences sharedPreferences = context.getSharedPreferences("config", context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = sharedPreferences.edit();
                    edit.putString("token", s);
//                    Log.e("volley_Get", s);
                    edit.commit();
                    Message message=new Message();
                    message.obj=obj;
                    message.what=MyApplication.GET_TOKEN_SUCCESS;
                    handler.sendMessage(message);
//                    handler.sendEmptyMessage();
                }else{
                    Log.i("Error", "ERROR");
                    handler.sendEmptyMessage(MyApplication.GET_TOKEN_ERROR);
                }
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
//                Toast.makeText(context, "网络连接出错", Toast.LENGTH_SHORT).show();
                Log.e("onErrorResponse", "onErrorResponse");
                handler.sendEmptyMessage(MyApplication.GET_TOKEN_ERROR);
            }
        });
        request.setTag("volley_Get_TOKEN");
        MyApplication.getQueue().add(request);
    }
}
