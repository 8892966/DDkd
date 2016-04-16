package com.example.user.ddkd;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.user.ddkd.text.UserInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/5.
 */
public class MainActivity_userinfo extends Activity implements View.OnClickListener {
    private TextView textView;
    private TextView username;
    private TextView collage;
    private TextView number;
    private TextView phone;
    private TextView shortphone;
    private TextView level;
    private UserInfo userInfo;
    private List<UserInfo> list;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_userinfo);
        textView=(TextView)findViewById(R.id.tv_head_fanghui);
        textView.setOnClickListener(this);
        username= (TextView) findViewById(R.id.username);
        collage= (TextView) findViewById(R.id.collage);
        number= (TextView) findViewById(R.id.number);
        phone= (TextView) findViewById(R.id.phone);
        shortphone= (TextView) findViewById(R.id.shortphone);
        level= (TextView) findViewById(R.id.level);
        Voley_Get();
    }

    public void Voley_Get(){
        SharedPreferences sharedPreferences=getSharedPreferences("config", MODE_PRIVATE);
        String token=sharedPreferences.getString("token", null);
        Log.i("Volley_Get",token);
        String url="http://www.louxiago.com/wc/ddkd/admin.php/Turnover/center/token/"+token;
        StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
//                Log.i("token",s);
                Type listv=new TypeToken<LinkedList<UserInfo>>(){}.getType();
                Gson gson=new Gson();
                list=gson.fromJson(s,listv);

                if(list!=null){
                    Log.i("token",userInfo.toString());
                    UserInfo userInfo1=list.get(0);
                    if(userInfo1!=null){
                        Log.i("DDS","content not is null");
                    }else{
                        Log.i("DDS","content is null");
                    }
                    //*****************根据Json中的数据回显用户的信息********************
                    username.setText(userInfo1.getUsername());
                    collage.setText(userInfo1.getCollege());
                    number.setText(userInfo1.getNumber()+"");
                    phone.setText(userInfo1.getPhone()+"");
                    shortphone.setText(userInfo1.getShortphone()+"");
                    level.setText(userInfo1.getLevel());
                }else{
                    Log.i("Error","List is null");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        request.setTag("abcPost_userinfo");
        MyApplication.getQueue().add(request);
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_head_fanghui:
                finish();
                break;
        }
    }
}
