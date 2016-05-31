package com.example.user.ddkd;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.user.ddkd.text.UserInfo;
import com.google.gson.Gson;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Administrator on 2016/4/2.
 */
public class MainActivity_login extends BaseActivity implements View.OnClickListener {
    private TextView button;
    private EditText userid1;
    private EditText password1;
    private TextView insert;
    private TextView forget;
    private ProgressDialog progressDialog;
    private CheckBox rembpwd;

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            ExitApplication.getInstance().exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.layout_login);
            button = (TextView) findViewById(R.id.login);
            userid1 = (EditText) findViewById(R.id.userInfo);
            password1 = (EditText) findViewById(R.id.passwordInfo);
            insert = (TextView) findViewById(R.id.insert);
            forget = (TextView) findViewById(R.id.forget);
            rembpwd = (CheckBox) findViewById(R.id.rembpwd);

            SharedPreferences preferences01 = getSharedPreferences("config", MODE_PRIVATE);
            int i=preferences01.getInt("checkstatic",0);
            if(i==1){
                userid1.setText(preferences01.getString("phone1",""));
                password1.setText(preferences01.getString("password1",""));
            }
            button.setOnClickListener(this);
            insert.setOnClickListener(this);
            forget.setOnClickListener(this);
            ExitApplication.getInstance().addActivity(this);

            //**********点击图标判断当前是否为登录状态**********
            SharedPreferences loginstatic = getSharedPreferences("config", MODE_PRIVATE);
            String nowLoginstatic = loginstatic.getString("loginstatic", "");
            if (nowLoginstatic.equals("1")) {
                Intent intent = new Intent(MainActivity_login.this, JieDangActivity.class);
                startActivity(intent);
                finish();
                MyApplication.state = 1;
            }
        }catch (Exception e){
            Toast.makeText(MainActivity_login.this,"信息有误",Toast.LENGTH_SHORT).show();
        }
    }

    public void volley_Get(final String userid, final String password) {
            String url = "http://www.louxiago.com/wc/ddkdtest/admin.php/User/login";
            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("Get_login", s);
                try {
//                closeProgressDialog();//*****关闭加载提示框*****
                    if ("WAIT PASS".equals(s)) {
                        Toast.makeText(MainActivity_login.this, "正在审核中，请耐心等候...", Toast.LENGTH_SHORT).show();
                    } else if (!"ERROR".equals(s)) {
                        s = s.substring(1, s.length() - 1);
                        //******************当提交成功以后，后台会返回一个参数来说明是否提交/验证成功******************
                        SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
                        SharedPreferences.Editor edit = sharedPreferences.edit();
                        edit.putString("token", s);
                        edit.putString("loginstatic", "1");
                        MyApplication.state = 1;
                        edit.commit();
                        //****************保存登录状态，0为离线状态，1为在线状态************************
                        Intent intent = new Intent(MainActivity_login.this, JieDangActivity.class);
                        startActivity(intent);
                        finish();
                        volley_Get_Image();
                        volley_Get_userInfo();
                    } else {
                        Toast.makeText(MainActivity_login.this, "您的信息有误", Toast.LENGTH_SHORT).show();
                    }
                    closeProgressDialog();
                }catch (Exception e){
                    Toast.makeText(MainActivity_login.this,"信息有误",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                closeProgressDialog();
                Toast.makeText(MainActivity_login.this, "网络连接中断", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map1 = new HashMap<>();
                map1.put("phone",userid);
                map1.put("password",password);
                return map1;
            }
        };
        request.setTag("abcGet_login");
        MyApplication.getQueue().add(request);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.login:
                //*****启动加载提示框*****
                showProgressDialog();
                //***********判断服务器返回的参数，根据参数来判断验证是否通过**********
                String phone = userid1.getText().toString();
                String password = password1.getText().toString();
                SharedPreferences preferences = getSharedPreferences("config", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("phone", phone);
                editor.putString("password", password);
                //******************记录当前的打钩的状态，1为打钩，0为不打钩*******************
                if (rembpwd.isChecked()) {
                    editor.putInt("checkstatic",1);
                    editor.putString("phone1",phone);
                    editor.putString("password1", password);
                }else{
                    editor.putInt("checkstatic",0);
                }
                editor.commit();
                if (!TextUtils.isEmpty(phone)) {
                    if (!TextUtils.isEmpty(password)) {
                        volley_phoExist_GET(phone, password);
                    } else {
                        closeProgressDialog();
                        Toast.makeText(MainActivity_login.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    closeProgressDialog();
                    Toast.makeText(MainActivity_login.this, "账号不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.insert:
                intent = new Intent(this, ZhuCeActivity.class);
                startActivity(intent);
                break;
            case R.id.forget:
                intent = new Intent(this, MainActivity_forget.class);
                startActivity(intent);
                break;
        }
    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(MainActivity_login.this);
            progressDialog.setMessage("正在登陆.......");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    //判断用户是否已注册
    private void volley_phoExist_GET(final String phone, final String password) {
        String url = "http://www.louxiago.com/wc/ddkdtest/admin.php/User/phoExist/phone/" + phone;
//        参数一：方法 参数二：地址 参数三：成功回调 参数四：错误回调 。重写getParams 以post参数
        StringRequest request_post = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
//                Log.e("volley_phoExist_GET", s);
                    if (!"SUCCESS".equals(s)) {
                        volley_Get(phone, password);
                    } else {
                        closeProgressDialog();
                        Toast.makeText(MainActivity_login.this, "该用户还没注册！", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    Log.e("Exception", e.getMessage());
                    Toast.makeText(MainActivity_login.this,"信息有误",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                closeProgressDialog();
                Toast.makeText(MainActivity_login.this, "网络异常", Toast.LENGTH_SHORT).show();
            }
        });
        request_post.setTag("volley_phoExist_GET");
        MyApplication.getQueue().add(request_post);
    }

    //**********获得用户头像的路径***********
    public void volley_Get_Image() {
        try {
            final SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
            String url = MyApplication.url+"User/getLogo/token/" + sharedPreferences.getString("token", "");
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    Log.e("volley_Get_Image",s);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("imageuri", s);
                    editor.commit();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(MainActivity_login.this, "网络连接出错", Toast.LENGTH_SHORT).show();
                }
            });
            stringRequest.setTag("volley_Get_Image_login");
            MyApplication.getQueue().add(stringRequest);
        }catch (Exception e){
            Toast.makeText(MainActivity_login.this,"信息有误",Toast.LENGTH_SHORT).show();
        }
    }
    //**********获得用户的基本信息***********
    public void volley_Get_userInfo() {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
            String token = sharedPreferences.getString("token", null);
            String url = MyApplication.url+"Turnover/center/token/" + token;
            Log.i("Loginurl",url);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String s){
                    if (!"ERROR".equals(s)) {
                        Log.e("volley_Get_userInfo",s);
                        Gson gson = new Gson();
                        UserInfo userInfo = gson.fromJson(s, UserInfo.class);
                        //**********保存用户的个人信息，断网时回显***********
                        DecimalFormat decimalFormat = new DecimalFormat("0.00");
                        SharedPreferences preferences = getSharedPreferences("user", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("username", userInfo.getUsername());
                        editor.putString("collage", userInfo.getCollege());
                        editor.putString("number", userInfo.getNumber() + "");
                        editor.putString("phone", userInfo.getPhone() + "");
                        editor.putString("shortphone", userInfo.getShortphone() + "");
                        editor.putString("level", userInfo.getLevel());
                        editor.putString("yingye", decimalFormat.format(Double.valueOf(userInfo.getYingye())));
                        editor.putString("balance", decimalFormat.format(Double.valueOf(userInfo.getBalance())));
                        editor.commit();
                    } else {
                        Log.i("GetInfoError", "login failed to get information");
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.i("ERROR", "login failed to get information");
                }
            });
            stringRequest.setTag("volley_Get_userInfo");
            MyApplication.getQueue().add(stringRequest);
        }catch (Exception e){
            Toast.makeText(MainActivity_login.this,"信息有误",Toast.LENGTH_SHORT).show();
        }
    }
}
