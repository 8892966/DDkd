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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.baidu.mobstat.StatService;
import com.example.user.ddkd.text.UserInfo;
import com.example.user.ddkd.utils.AutologonUtil;
import com.example.user.ddkd.utils.BitmaoCache;
import com.example.user.ddkd.utils.Exit;
import com.example.user.ddkd.utils.MyStringRequest;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

/**
 * Created by Administrator on 2016/4/2.
 */
public class MainActivity_main extends Activity implements View.OnClickListener {
    private ImageView announce;
    private RelativeLayout title;
    private ImageView userimage;
    private TextView username;
    private BitmaoCache bitmaoCache = new BitmaoCache();
    private TextView turnover;
    private TextView moneysum;
    private TextView userphone;
    private UserInfo userInfo;
    private RelativeLayout balance;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            UserInfo userInfo = (UserInfo) msg.obj;
            volley_Get(userInfo);
        }
    };
    private Handler handler_image = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MyApplication.GET_TOKEN_SUCCESS:
                    String imageurl = (String) msg.obj;
                    Picasso.with(MainActivity_main.this).load(imageurl).into(userimage);
                    break;
                case MyApplication.GET_TOKEN_ERROR:

                    break;
            }
        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_userinfo);

        //*******个人中心的信息回显*********
        userimage = (ImageView) findViewById(R.id.userimage);
        username = (TextView) findViewById(R.id.username);
        userphone = (TextView) findViewById(R.id.userphone);
        turnover = (TextView) findViewById(R.id.turnover);
        moneysum = (TextView) findViewById(R.id.moneysum);
        //*******实现点击页面的跳转*******
        ImageView exituserinfo = (ImageView) findViewById(R.id.exituserinfo);
        exituserinfo.setOnClickListener(this);
        announce = (ImageView) findViewById(R.id.announce);
        announce.setOnClickListener(this);
        title = (RelativeLayout) findViewById(R.id.title);
        title.setOnClickListener(this);
        balance= (RelativeLayout) findViewById(R.id.balance);
        balance.setOnClickListener(this);
        RelativeLayout detauils = (RelativeLayout) findViewById(R.id.details);
        detauils.setOnClickListener(this);
        RelativeLayout setting = (RelativeLayout) findViewById(R.id.setting);
        setting.setOnClickListener(this);
        ExitApplication.getInstance().addActivity(this);

        SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        String imageuri = sharedPreferences.getString("imageuri", "");
        Log.i("URL", imageuri);
        if (!TextUtils.isEmpty(imageuri)) {
            Picasso.with(this).load(imageuri).into(userimage);
        } else {
            volley_Get_Image();
        }
        SharedPreferences ShareuserInfo = getSharedPreferences("user", MODE_PRIVATE);
        if (ShareuserInfo != null) {
            Log.i("turnover", ShareuserInfo.getString("yingye", ""));
            Log.i("moneysum", ShareuserInfo.getString("balance", ""));
            turnover.setText(ShareuserInfo.getString("yingye", ""));
            moneysum.setText(ShareuserInfo.getString("balance", ""));
            username.setText(ShareuserInfo.getString("username", ""));
            userphone.setText(ShareuserInfo.getString("phone", ""));
        } else {
            volley_Get(userInfo);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.title:
                intent = new Intent(this, MainActivity_userinfo.class);
                startActivity(intent);
                break;
            case R.id.details:
                //直接跳转到订单页面;
//                intent = new Intent(MainActivity_main.this, details.class);
                intent = new Intent(MainActivity_main.this, DingDanNewActivity.class);
                intent.putExtra("Static",3);
                startActivity(intent);
                break;
            case R.id.setting:
                intent = new Intent(this, MainActivity_setting.class);
                startActivity(intent);
                break;
            case R.id.exituserinfo:
                finish();
                break;
            case R.id.announce:
                intent = new Intent(MainActivity_main.this, Announce.class);
                startActivity(intent);
                break;
            case R.id.balance:
                intent = new Intent(this, MainActivity_balance.class);
                startActivity(intent);
                break;
        }
    }

    //**********缓存并加载网络图片***********
    public void volley_Get_Image() {
        final SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        String url = "http://www.louxiago.com/wc/ddkd/admin.php/User/getLogo/token/" + sharedPreferences.getString("token", "");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new MyStringRequest() {
            @Override
            public void success(Object o) {
                try{
                    String s = (String) o;
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("imageuri", s);
                    editor.commit();
                    String imageurl = s;
                    Message ms = new Message();
//                ms.obj=imageurl;
                    ms.obj = s;
                    ms.what=MyApplication.GET_TOKEN_SUCCESS;
                    handler_image.sendMessage(ms);
                }catch (Exception e){
                    Log.e("Exception", e.getMessage());
                    Toast.makeText(MainActivity_main.this, "信息有误", Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void tokenouttime() {
                Log.i("MainActivity", "token outtime");
                String s=sharedPreferences.getString("imageuri","");
                AutologonUtil autologonUtil=new AutologonUtil(MainActivity_main.this,handler_image,s);
                autologonUtil.volley_Get_TOKEN();
            }
            @Override
            public void yidiensdfsdf() {
                Exit.exit(MainActivity_main.this);
                Toast.makeText(MainActivity_main.this, "您的账户已在异地登录", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("ERROR", "====>");
            }
        });
        stringRequest.setTag("volley_Get_Image");
        MyApplication.getQueue().add(stringRequest);
    }

    public void volley_Get(final UserInfo userInfo) {
        SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);
        String url = "http://www.louxiago.com/wc/ddkd/admin.php/Turnover/center/token/" + token;
        StringRequest request = new StringRequest(Request.Method.GET, url, new MyStringRequest() {
            @Override
            public void success(Object o) {
                try{
                    String s = (String) o;
                    Log.i("Main", s);
                    if (!"ERROR".equals(s)) {
                        Gson gson = new Gson();
                        UserInfo userInfo = gson.fromJson(s, UserInfo.class);
                        if (userInfo != null) {
                            if (userInfo.getYingye() == null) {
                                turnover.setText("0");
                            } else {
                                //***********将数值类型定义为高精度*************
                                DecimalFormat g = new DecimalFormat("0.00");//精确到两位小数
                                g.format(Double.valueOf(userInfo.getYingye()));
                                turnover.setText(g.format(Double.valueOf(userInfo.getYingye())));
                            }
                            DecimalFormat decimalFormat = new DecimalFormat("0.00");
                            moneysum.setText(decimalFormat.format(Double.valueOf(userInfo.getBalance())));
                            username.setText(userInfo.getUsername());
                            userphone.setText(String.valueOf(userInfo.getPhone()));

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
                }catch (Exception e){
                    Log.e("Exception", e.getMessage());
                    Toast.makeText(MainActivity_main.this, "信息有误", Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void tokenouttime() {
                Log.e("MainActivity_main", "token过期");
                AutologonUtil autologonUtil = new AutologonUtil(MainActivity_main.this, handler, userInfo);
                autologonUtil.volley_Get_TOKEN();
            }

            @Override
            public void yidiensdfsdf() {
                Exit.exit(MainActivity_main.this);
                Toast.makeText(MainActivity_main.this, "您的账户已在异地登录", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MainActivity_main.this, "网络连接中断", Toast.LENGTH_SHORT).show();
            }
        });
        request.setTag("Get_main");
        MyApplication.getQueue().add(request);
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);
        volley_Get_Image();
        volley_Get(userInfo);
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }

    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getQueue().cancelAll("Get_main");
        MyApplication.getQueue().cancelAll("volley_Get_Image");
    }
}
