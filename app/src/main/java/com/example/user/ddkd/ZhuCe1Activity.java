package com.example.user.ddkd;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baidu.mobstat.StatService;
import com.example.user.ddkd.beam.OrderInfo;
import com.example.user.ddkd.beam.SignUpInfo;
import com.example.user.ddkd.utils.AutologonUtil;
import com.example.user.ddkd.utils.SmsUtils;
import com.example.user.ddkd.utils.YanZhenMaUtil;

import org.apache.commons.codec.digest.DigestUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by User on 2016-04-03.
 */
public class ZhuCe1Activity extends Activity implements View.OnClickListener {

    private EditText et_phone_number;
    private EditText et_yanzhengma;
    private CheckBox cb_xieyi;
    private TextView tv_button_yanzhengma;
    private TextView tv_button_yuedu;
    private TextView tv_next;
    private TextView tv_head_fanghui;

    private YanZhenMaUtil yanZhenMaUtil;
    private SmsUtils smsUtils;

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zhuce1_activity);
        yanZhenMaUtil = new YanZhenMaUtil();//初始化验证码工具类

        smsUtils = new SmsUtils();
        smsUtils.startGetSms(this);

        et_phone_number = (EditText) findViewById(R.id.et_phone_number);//手机号
        et_yanzhengma = (EditText) findViewById(R.id.et_yanzhengma);//验证码
        cb_xieyi = (CheckBox) findViewById(R.id.cb_xieyi);//协议
        tv_button_yuedu = (TextView) findViewById(R.id.tv_button_yuedu);//阅读协议
        tv_button_yanzhengma = (TextView) findViewById(R.id.tv_button_yanzhengma);//获取验证码
        tv_next = (TextView) findViewById(R.id.tv_next);//下一步按钮
        tv_head_fanghui = (TextView) findViewById(R.id.tv_head_fanghui);//返回

        tv_button_yuedu.setOnClickListener(this);
        tv_button_yanzhengma.setOnClickListener(this);
        tv_button_yanzhengma.setEnabled(false);//初始化获取验证码按钮状态
        cb_xieyi.setOnClickListener(this);
        tv_next.setOnClickListener(this);
        tv_head_fanghui.setOnClickListener(this);


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
                    tv_button_yanzhengma.setText("检查中...");
                    volley_phoExist_GET(et_phone_number.getText().toString());
                } else {
                    tv_button_yanzhengma.setEnabled(false);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.tv_button_yanzhengma:
                yanZhenMaUtil.sendYZM(this, et_phone_number, tv_button_yanzhengma);
                break;
            case R.id.tv_button_yuedu:
                intent = new Intent(this, WebActivity.class);
                intent.putExtra("title", "DD快递服务协议");
                intent.putExtra("url", "http://www.louxiago.com/wc/ddkd/index.php/Agreement/index.html");
                startActivity(intent);
                break;
            case R.id.cb_xieyi:
                if (cb_xieyi.isChecked()) {
                    tv_next.setEnabled(true);
                } else {
                    tv_next.setEnabled(false);
                }
                break;
            case R.id.tv_next:
//                if (yanZhenMaUtil.isYZM(this, et_yanzhengma, et_phone_number)) {
                    //注册信息
                    SignUpInfo signUpInfo = new SignUpInfo();
                    signUpInfo.setPhone(et_phone_number.getText().toString());
                    Intent intent2 = new Intent(this, ZhuCe2Activity.class);
                    intent2.putExtra("SignUpInfo", signUpInfo);//传递注册信息
                    startActivity(intent2);
                    finish();
//                }
                break;
            case R.id.tv_head_fanghui:
//                intent = new Intent(this, MainActivity_login.class);
//                startActivity(intent);
                finish();
                break;
        }
    }
    //判断用户是否已注册
    private void volley_phoExist_GET(String phone) {
        String url = "http://www.louxiago.com/wc/ddkd/admin.php/User/phoExist/phone/" + phone;
//        参数一：方法 参数二：地址 参数三：成功回调 参数四：错误回调 。重写getParams 以post参数
        StringRequest request_post = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("volley_phoExist_GET", s);
                tv_button_yanzhengma.setText("验证码");
                if ("SUCCESS".equals(s)) {
                    tv_button_yanzhengma.setEnabled(true);
                } else {
                    tv_button_yanzhengma.setEnabled(false);
                    Toast.makeText(ZhuCe1Activity.this, "用户已存在！", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                tv_button_yanzhengma.setText("验证码");
                tv_button_yanzhengma.setEnabled(false);
                Toast.makeText(ZhuCe1Activity.this, "网络异常", Toast.LENGTH_SHORT).show();
            }
        });
        request_post.setTag("volley_phoExist_GET");
        MyApplication.getQueue().add(request_post);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        smsUtils.cloesGetSms(this);
        MyApplication.getQueue().cancelAll("volley_phoExist_GET");
    }
}
