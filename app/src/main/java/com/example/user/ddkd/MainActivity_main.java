package com.example.user.ddkd;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.user.ddkd.text.UserInfo;
import com.example.user.ddkd.utils.Progress;
import com.google.gson.Gson;

/**
 * Created by Administrator on 2016/4/2.
 */
public class MainActivity_main extends Activity implements View.OnClickListener {
    private ImageView announce;
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_userinfo);
        ImageView imageView=(ImageView)findViewById(R.id.exituserinfo);
        imageView.setOnClickListener(this);
        announce= (ImageView) findViewById(R.id.announce);
        announce.setOnClickListener(this);

        RelativeLayout moneysum=(RelativeLayout)findViewById(R.id.moneysum);
        moneysum.setOnClickListener(this);
        LinearLayout detauils=(LinearLayout)findViewById(R.id.details);
        detauils.setOnClickListener(this);
        LinearLayout userinfo=(LinearLayout)findViewById(R.id.userInfo);
        userinfo.setOnClickListener(this);
        LinearLayout setting=(LinearLayout)findViewById(R.id.setting);
        setting.setOnClickListener(this);
        volley_Get();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.moneysum:
                intent=new Intent(this,MainActivity_balance.class);
                startActivity(intent);
                break;
            case R.id.details:
                intent=new Intent(this,details.class);
                startActivity(intent);
                break;
            case R.id.userInfo:
                intent=new Intent(this,MainActivity_userinfo.class);
                startActivity(intent);
                break;
            case R.id.setting:
                intent=new Intent(this,MainActivity_setting.class);
                startActivity(intent);
                break;
            case R.id.exituserinfo:
                finish();
                break;
            case R.id.announce:
                Progress.showProgressDialog();
                intent=new Intent(MainActivity_main.this,Announce.class);
                startActivity(intent);
                break;
        }
    }
    public void volley_Get(){
        SharedPreferences sharedPreferences=getSharedPreferences("config",MODE_PRIVATE);
        String token=sharedPreferences.getString("token",null);
        String url="http://www.louxiago.com/wc/ddkd/admin.php/Turnover/center/token/"+token;
        StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.i("Main",s);
                Gson gson=new Gson();
                UserInfo userInfo=gson.fromJson(s, UserInfo.class);
                if(userInfo!=null) {
                    SharedPreferences preferences = getSharedPreferences("user", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("username", userInfo.getUsername());
                    editor.putString("collage", userInfo.getCollege());
                    editor.putString("number", userInfo.getNumber() + "");
                    editor.putString("phone", userInfo.getPhone() + "");
                    editor.putString("shortphone", userInfo.getShortphone() + "");
                    editor.putString("level", userInfo.getLevel());
                    editor.commit();
                }else{
                    Log.i("Error","your infomation save Error");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        request.setTag("Get_main");
        MyApplication.getQueue().add(request);
    }
}
