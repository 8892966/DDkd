package com.example.user.ddkd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import java.util.LinkedList;

/**
 * Created by Administrator on 2016/4/5.
 */
public class MainActivity_userinfo extends Activity implements View.OnClickListener {
    private TextView textView;
    private TextView userphone;
    private TextView username;
    private TextView schoolname;
    private TextView DJ;
    private TextView userno;
    private TextView detailsnum;
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_userinfo);
        textView=(TextView)findViewById(R.id.tv_head_fanghui);
        textView.setOnClickListener(this);
        username= (TextView) findViewById(R.id.username);
        userno= (TextView) findViewById(R.id.userno);
        userphone= (TextView) findViewById(R.id.userphone);
        DJ= (TextView) findViewById(R.id.DJ);
        detailsnum= (TextView) findViewById(R.id.detailsnum);
        schoolname= (TextView) findViewById(R.id.schoolname);
    }

    public void Voley_Post(){
        String url="";
        StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Type userinfolist=new TypeToken<LinkedList<UserInfo>>(){}.getType();
                Gson gson=new Gson();
                UserInfo list =gson.fromJson(s, userinfolist);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        request.setTag("abcPost");
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
