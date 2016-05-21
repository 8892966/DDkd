package com.example.user.ddkd.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.user.ddkd.MyApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2016-05-17.
 */
public class NewAutologonUtil {
    private static NewAutologonUtil instance=null;
    private static List<OnLogoListent> list=null;
    private NewAutologonUtil(){
    }
    public static void getToken(Context context,OnLogoListent onLogoListent){
        if(instance==null){
            instance=new NewAutologonUtil();
            instance.volley_Get_TOKEN(context);
            list=new ArrayList<>();
        }
        list.add(onLogoListent);
    }

    public void volley_Get_TOKEN(final Context context) {
            MyApplication.getQueue().cancelAll("volley_Get_TOKEN");
            SharedPreferences sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
            String userid = sharedPreferences.getString("phone", "");
            String password = sharedPreferences.getString("password", "");
            String url = "http://www.louxiago.com/wc/ddkd/admin.php/User/login?phone=" + userid + "&password=" + password;
            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    try {
                        if (!s.equals("ERROR")) {
                            s = s.substring(1, s.length() - 1);
                            //******************当提交成功以后，后台会返回一个参数来说明是否提交/验证成功******************
                            SharedPreferences sharedPreferences = context.getSharedPreferences("config", context.MODE_PRIVATE);
                            SharedPreferences.Editor edit = sharedPreferences.edit();
                            edit.putString("token", s);
                            edit.commit();
                            for(OnLogoListent listent:list){
                                listent.Success();
                            }
                        } else {
                            for(OnLogoListent listent:list){
                                listent.Error();
                            }
                        }
                    } catch (Exception e) {
                        Log.e("Exception", e.getMessage()+"");
                        Toast.makeText(context, "信息有误!!!", Toast.LENGTH_SHORT).show();
                    } finally {
                        instance=null;
                        list=null;
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    for(OnLogoListent listent:list){
                        listent.Error();
                    }
                    instance=null;
                    list=null;
                }
            });
            request.setTag("volley_Get_TOKEN");
            MyApplication.getQueue().add(request);
    }
    public interface OnLogoListent{
        void Success();
        void Error();
    }
}
