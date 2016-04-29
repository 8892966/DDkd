package com.example.user.ddkd;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.baidu.mobstat.StatService;
import com.example.user.ddkd.text.UserInfo;
import com.example.user.ddkd.utils.AutologonUtil;
import com.example.user.ddkd.utils.BitmaoCache;
import com.example.user.ddkd.utils.Exit;
import com.example.user.ddkd.utils.MyStringRequest;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

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
    private ImageView userimage;
    private UserInfo userInfo;
    private BitmaoCache bitmaoCache =new BitmaoCache();
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            UserInfo userInfo= (UserInfo) msg.obj;
            Voley_Get(userInfo);
        }
    };
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_userinfo);
        textView=(TextView)findViewById(R.id.tv_head_fanghui);
        textView.setOnClickListener(this);
        username= (TextView) findViewById(R.id.username);
        collage= (TextView) findViewById(R.id.collage);
        number= (TextView) findViewById(R.id.number);
        phone= (TextView) findViewById(R.id.phone);
        userimage= (ImageView) findViewById(R.id.userimage);
        shortphone= (TextView) findViewById(R.id.shortphone);
        level= (TextView) findViewById(R.id.level);
        Voley_Get(userInfo);
        ExitApplication.getInstance().addActivity(this);

        SharedPreferences sharedPreferences=getSharedPreferences("config",MODE_PRIVATE);
        String url=sharedPreferences.getString("imageuri", "");
        Picasso.with(MainActivity_userinfo.this).load(url).into(userimage);
    }

    public void Voley_Get(final UserInfo userInfo){
        final SharedPreferences sharedPreferences=getSharedPreferences("config", MODE_PRIVATE);
        String token=sharedPreferences.getString("token", null);
//        Log.i("Volley_Get",token);
        String url="http://www.louxiago.com/wc/ddkd/admin.php/Turnover/center/token/"+token;
        StringRequest request=new StringRequest(Request.Method.GET, url, new MyStringRequest() {
            @Override
            public void success(Object o) {
                String s= (String) o;
                if(!s.equals("ERROR")){
                    Gson gson=new Gson();
                    UserInfo userInfo=gson.fromJson(s,UserInfo.class);
                    if(userInfo!=null){
                        boolean network=isNetworkConnected();
                        if (network){
                            //**********当网络连接存在时，从后台获取用户信息***********
                            Log.i("SUCCESS", "SUCCESS");
                            //*****************根据Json中的数据回显用户的信息********************
                            username.setText(userInfo.getUsername());
                            collage.setText(userInfo.getCollege());
                            number.setText(userInfo.getNumber()+"");
                            phone.setText(userInfo.getPhone()+"");
                            shortphone.setText(userInfo.getShortphone()+"");
                            String name=getLevel(userInfo.getLevel());
                            level.setText(name);
                        }else {
                            //当网络连接不存在时，从手机的内存中获取用户信息
                            Log.i("ERROR", "ERROR");
                            SharedPreferences sharedPreferences1 = getSharedPreferences("user", MODE_PRIVATE);
                            username.setText(sharedPreferences1.getString("username", ""));
                            collage.setText(sharedPreferences1.getString("collage", ""));
                            number.setText(sharedPreferences1.getString("number", ""));
                            phone.setText(sharedPreferences1.getString("phone", ""));
                            shortphone.setText(sharedPreferences1.getString("shortphone", ""));
                            String name=getLevel(sharedPreferences1.getString("shortphone", ""));
                            level.setText(name);

                        }
                    }else{
                        Log.i("Error","List is null");
                    }
                }else{
                    Toast.makeText(MainActivity_userinfo.this, "网络连接出错", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void tokenouttime() {
                Log.e("MainActivity_userinfo","token outtime");
                AutologonUtil autologonUtil=new AutologonUtil(MainActivity_userinfo.this,handler,userInfo);
                autologonUtil.volley_Get_TOKEN();
            }

            @Override
            public void yidiensdfsdf() {
                Exit.exit(MainActivity_userinfo.this);
                Toast.makeText(MainActivity_userinfo.this, "您的账户已在异地登录", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MainActivity_userinfo.this, "网络连接中断", Toast.LENGTH_SHORT).show();
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
    /**
     * 检测网络是否可用
     * @return
     */
    public boolean isNetworkConnected(){
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if(ni != null && ni.isConnectedOrConnecting()){
            return true;
        }else{
            return false;
        }
    }
    @Override
    protected void onResume(){
        super.onResume();
        StatService.onResume(this);
    }

    @Override
    protected void onPause(){
        super.onPause();
        StatService.onPause(this);
    }
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getQueue().cancelAll("abcPost_userinfo");
    }
    public String getLevel(String level){
        String dengji=level;
        if (dengji.equals("0")) {
            return dengji="铁牌快递员";
        } else if (dengji.equals("1")) {
            return dengji="铜牌快递员";
        } else if (dengji.equals("2")) {
            return dengji="银牌快递员";
        } else{
            return dengji="金牌快递员";
        }
    }
}
