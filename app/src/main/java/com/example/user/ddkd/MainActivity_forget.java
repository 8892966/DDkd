package com.example.user.ddkd;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baidu.mobstat.StatService;
import com.example.user.ddkd.beam.OrderInfo;
import com.example.user.ddkd.utils.AutologonUtil;
import com.example.user.ddkd.utils.PasswordUtil;
import com.example.user.ddkd.utils.YanZhenMaUtil;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/6.
 */
public class MainActivity_forget extends Activity implements View.OnClickListener {
    private TextView commit;
    private TextView tv_head_fanghui;
    private TextView tv_button_yanzhengma;
    private EditText et_phone_number;
    private EditText et_yanzhengma;
    private EditText et_new_password;
    private EditText et_new_password2;
    private SharedPreferences preferences;
    private YanZhenMaUtil yanZhenMaUtil;
    private ProgressDialog progressDialog;//修改等候页面
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password_activity);
        yanZhenMaUtil = new YanZhenMaUtil();//初始化验证码工具类

        commit=(TextView)findViewById(R.id.commitpassword);
        tv_head_fanghui= (TextView) findViewById(R.id.tv_head_fanghui);
        tv_button_yanzhengma= (TextView) findViewById(R.id.tv_button_yanzhengma);//获取验证码
        et_phone_number = (EditText) findViewById(R.id.et_phone_number);//手机号
        et_yanzhengma = (EditText) findViewById(R.id.et_yanzhengma);//验证码
        et_new_password= (EditText) findViewById(R.id.et_new_password);//密码
        et_new_password2= (EditText) findViewById(R.id.et_new_password2);//确认密码

        commit.setOnClickListener(this);
        tv_button_yanzhengma.setOnClickListener(this);
        tv_head_fanghui.setOnClickListener(this);

        tv_button_yanzhengma.setEnabled(false);//验证码初始化为空

        //只有输入手机号码时才能点击获取验证码
        et_phone_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (et_phone_number.length() == 11) {
                    tv_button_yanzhengma.setEnabled(true);
                } else {
                    tv_button_yanzhengma.setEnabled(false);
                }
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.commitpassword:
                    String password1 = et_new_password.getText().toString();
                    String password2 = et_new_password2.getText().toString();
                    if(PasswordUtil.isSame(this, password1, password2)) {
                        showProgressDialog();
                        volley_XGMM_GET(et_phone_number.getText().toString(),et_new_password.getText().toString(),et_yanzhengma.getText().toString());
                    }
                break;
            case R.id.tv_head_fanghui:
                finish();
                break;
            case R.id.tv_button_yanzhengma:
                volley_getYZM_GET(et_phone_number.getText().toString());
                countDown();
                break;
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
    private void countDown(){
//      tv_bt_verify你要设置动画的view
        ValueAnimator valueAnimator=ValueAnimator.ofInt(0,60);//从0到30计时
        valueAnimator.setDuration(60000);//持续时间为60s
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                tv_button_yanzhengma.setText("剩余"+String.valueOf(60-value)+"s");
                if (value==60){
                    //tv_bt_verify.setBackgroundResource(R.drawable.ret_orange);//30s后的背景
                    tv_button_yanzhengma.setEnabled(true);//30s后设置可以点击
                    tv_button_yanzhengma.setText("获取验证码");
                }else {
                    tv_button_yanzhengma.setEnabled(false);//30s内设置不可以点击
                    //time.setBackgroundResource(R.drawable.ret);//30s内的背景
                }
            }
        });
        valueAnimator.setInterpolator(new LinearInterpolator());//设置变化值为线性变化
        valueAnimator.start();//动画开始
    }
//修改密码
    private void volley_XGMM_GET(String phone,String password,String verify) {
        String url = "http://www.louxiago.com/wc/ddkd/admin.php/User/UpdatePsw/phone/" + phone+"/newpsw/"+password+"/verify/"+verify;
        StringRequest request_post = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("onResponse",s);
                    if(!s.equals("ERROR")){
                        closeProgressDialog();
                        Intent intent;
                        Toast.makeText(MainActivity_forget.this, "密码修改成功，请重新登录", Toast.LENGTH_SHORT).show();
                        intent = new Intent(MainActivity_forget.this, MainActivity_login.class);
                        startActivity(intent);
                        finish();
                    }else{
                        closeProgressDialog();
                        Toast.makeText(MainActivity_forget.this,"网络异常",Toast.LENGTH_SHORT).show();
                    }
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                closeProgressDialog();
                Toast.makeText(MainActivity_forget.this,"网络连接中断",Toast.LENGTH_SHORT).show();
            }
        });
        request_post.setTag("volley_XGMM_GET");
        MyApplication.getQueue().add(request_post);
    }
    //***************得到验证码*********************
    private void volley_getYZM_GET(String phone) {
        String url = "http://www.louxiago.com/wc/ddkd/admin.php/User/modifyPsw/phone/"+phone;
        StringRequest request_post = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                    Log.e("volley_getYZM_GET",s);
                    if(!s.equals("ERROR")){
                        Toast.makeText(MainActivity_forget.this, "请留意您的短信", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(MainActivity_forget.this,"网络异常", Toast.LENGTH_SHORT).show();
                    }
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MainActivity_forget.this,"网络连接中断",Toast.LENGTH_SHORT).show();
            }
        });
        request_post.setTag("volley_getYZM_GET");
        MyApplication.getQueue().add(request_post);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getQueue().cancelAll("volley_getYZM_GET");
        MyApplication.getQueue().cancelAll("volley_XGMM_GET");
    }
    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(MainActivity_forget.this);
            progressDialog.setMessage("正在修改.......");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
