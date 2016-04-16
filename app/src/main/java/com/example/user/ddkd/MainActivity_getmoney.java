package com.example.user.ddkd;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.user.ddkd.text.UserInfo;

import org.w3c.dom.Text;

import java.security.Principal;

/**
 * Created by Administrator on 2016/4/5.
 */
public class MainActivity_getmoney extends Activity implements View.OnClickListener {
    private TextView textView;
    private EditText getmoney;
    private TextView yue;
    private EditText counter;
    private TextView textView1;
    private TextView sure;
    private EditText Tname;
    private UserInfo userInfo;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.balance_getmoney);
        textView1=(TextView)findViewById(R.id.exitgetmoney);
        textView1.setOnClickListener(this);
        textView=(TextView)findViewById(R.id.tv_head_fanghui);
        textView.setOnClickListener(this);
        sure=(TextView)findViewById(R.id.sure);
        sure.setOnClickListener(this);

        yue= (TextView) findViewById(R.id.yue);
        getmoney= (EditText) findViewById(R.id.getmoney);
        counter= (EditText) findViewById(R.id.counter);
        Tname= (EditText) findViewById(R.id.Tname);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_head_fanghui:
                finish();
                break;
            case R.id.exitgetmoney:
                finish();
                break;
            case R.id.sure:
                //***********点击确定按钮的时候，提交数据*************
                double getmoney1=Double.valueOf(getmoney.getText().toString());
                String counter1=counter.getText().toString();
                String tname=Tname.getText().toString();
                SharedPreferences sharedPreferences=getSharedPreferences("User", MODE_PRIVATE);
                String username=sharedPreferences.getString("username",null);
                volley_get(getmoney1, counter1,tname,username);
                finish();
                break;
        }
    }
    public void volley_get(double getmoney,String counter,String tname,String username){
        SharedPreferences sharedPreferences1=getSharedPreferences("config",MODE_PRIVATE);
        String token=sharedPreferences1.getString("token",null);
        String url="http://www.louxiago.com/wc/ddkd/admin.php/Turnover/withdrawCash/money/"+getmoney+"/Tname/"+tname+"/counter/"+counter+"/name/"+username+"/extra/ /token/"+token;
        //将用户的提现信息提交给服务器
        StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //**************返回一个参数，说明提交的情况*****************


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        request.setTag("abcGet");
        MyApplication.getQueue().add(request);
    }
}
