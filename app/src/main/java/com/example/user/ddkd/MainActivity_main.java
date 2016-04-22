package com.example.user.ddkd;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.user.ddkd.text.UserInfo;
import com.example.user.ddkd.utils.AutologonUtil;
import com.google.gson.Gson;

import org.w3c.dom.Text;

/**
 * Created by Administrator on 2016/4/2.
 */
public class MainActivity_main extends Activity implements View.OnClickListener {
    private ImageView announce;
    private RelativeLayout title;
    private ImageView userimage;
    private TextView username;
    private TextView turnover;
    private TextView moneysum;
    private UserInfo userInfo;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            UserInfo userInfo= (UserInfo) msg.obj;
            volley_Get(userInfo);
        }
    };
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_userinfo);

        //*******个人中心的信息回显*********
        userimage= (ImageView) findViewById(R.id.userimage);
        username= (TextView) findViewById(R.id.username);
        turnover= (TextView) findViewById(R.id.turnover);
        moneysum= (TextView) findViewById(R.id.moneysum);

        //*******实现点击页面的跳转*******
        ImageView exituserinfo=(ImageView)findViewById(R.id.exituserinfo);
        exituserinfo.setOnClickListener(this);
        announce= (ImageView) findViewById(R.id.announce);
        announce.setOnClickListener(this);
        title=(RelativeLayout)findViewById(R.id.title);
        title.setOnClickListener(this);
        LinearLayout detauils=(LinearLayout)findViewById(R.id.details);
        detauils.setOnClickListener(this);
        LinearLayout userinfo=(LinearLayout)findViewById(R.id.userInfo);
        userinfo.setOnClickListener(this);
        LinearLayout setting=(LinearLayout)findViewById(R.id.setting);
        setting.setOnClickListener(this);
        volley_Get(userInfo);
        ExitApplication.getInstance().addActivity(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.title:
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
                intent=new Intent(MainActivity_main.this,Announce.class);
                startActivity(intent);
                break;
        }
    }
    public void volley_Get(final UserInfo userInfo){
        SharedPreferences sharedPreferences=getSharedPreferences("config",MODE_PRIVATE);
        String token=sharedPreferences.getString("token",null);
        String url="http://www.louxiago.com/wc/ddkd/admin.php/Turnover/center/token/"+token;
        StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
//                Log.e("Get_main", s);
                if (!s.equals("\"token outtime\"")) {
                    if (!s.equals("\"ERROR\"")) {
                        Gson gson = new Gson();
                        UserInfo userInfo = gson.fromJson(s, UserInfo.class);
                        if (userInfo != null) {
                            if ("" + userInfo.getYingye() == null) {
                                turnover.setText("0");
                            } else {
                                turnover.setText(userInfo.getYingye());
                            }
                            username.setText(userInfo.getUsername());
                            moneysum.setText(String.valueOf(userInfo.getBalance()));

                            //**********保存用户的个人信息，断网时回显***********
                            SharedPreferences preferences = getSharedPreferences("user", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("username", userInfo.getUsername());
                            editor.putString("collage", userInfo.getCollege());
                            editor.putString("number", userInfo.getNumber() + "");
                            editor.putString("phone", userInfo.getPhone() + "");
                            editor.putString("shortphone", userInfo.getShortphone() + "");
                            editor.putString("level", userInfo.getLevel());
                            editor.commit();
                        }
                    } else {
                        Toast.makeText(MainActivity_main.this, "网络连接出错", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("MainActivity_main", "token过期");
                    AutologonUtil autologonUtil=new AutologonUtil(MainActivity_main.this,handler,userInfo);
                    autologonUtil.volley_Get_TOKEN();
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
