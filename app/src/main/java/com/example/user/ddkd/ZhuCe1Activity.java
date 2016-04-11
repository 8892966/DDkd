package com.example.user.ddkd;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.commons.codec.digest.DigestUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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

    private String yanzhengma;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zhuce1_activity);

        et_phone_number = (EditText) findViewById(R.id.et_phone_number);//手机号
        et_yanzhengma = (EditText) findViewById(R.id.et_yanzhengma);//验证码
        cb_xieyi = (CheckBox) findViewById(R.id.cb_xieyi);//协议
        tv_button_yuedu = (TextView) findViewById(R.id.tv_button_yuedu);//阅读协议
        tv_button_yanzhengma = (TextView) findViewById(R.id.tv_button_yanzhengma);//获取验证码
        tv_next = (TextView) findViewById(R.id.tv_next);//下一步按钮
        tv_head_fanghui= (TextView) findViewById(R.id.tv_head_fanghui);//返回

        tv_button_yuedu.setOnClickListener(this);
        tv_button_yanzhengma.setOnClickListener(this);
        cb_xieyi.setOnClickListener(this);
        tv_next.setOnClickListener(this);
        tv_head_fanghui.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.tv_button_yanzhengma:
                final int mobile_code = (int) ((Math.random() * 9 + 1) * 100000);
                yanzhengma = mobile_code+"";
                Log.i("ZhuCe1Activity", mobile_code + "");
                String number = et_phone_number.getText().toString();
                Log.i("ZhuCe1Activity", number);
                if (!TextUtils.isEmpty(number)) {
                    volley_Post(mobile_code, number);
                } else {
                    Toast.makeText(ZhuCe1Activity.this, "请填入您的手机号码", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_button_yuedu:
                intent = new Intent(this, WebActivity.class);
                intent.putExtra("title", "DD快递服务协议");
                intent.putExtra("url", "http://www.baidu.com");
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
                if (yanzhengma == null) {
                    Toast.makeText(this, "请输入您的手机号并验证", Toast.LENGTH_LONG).show();
                } else {
                        if(yanzhengma.equals(et_yanzhengma.getText().toString())){
                            Intent intent2=new Intent(this,ZhuCe2Activity.class);
                            startActivity(intent2);
                        }else{
                            Toast.makeText(this, "验证码不正确", Toast.LENGTH_LONG).show();
                        }
                }
                break;
            case R.id.tv_head_fanghui:
                intent=new Intent(this,MainActivity_login.class);
                startActivity(intent);
                break;
        }
    }
    private void volley_Post(int mobile_code, final String phone) {
        Log.i("my", "user volley Post");
//        参数一：方法 参数二：地址 参数三：成功回调 参数四：错误回调 。重写getParams 以post参数
        final String content = new String("您的验证码是：" + mobile_code + "。请不要把验证码泄露给其他人。");
        StringRequest request_post = new StringRequest(Request.Method.POST, "http://106.ihuyi.cn/webservice/sms.php?method=Submit", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    InputStream is = new ByteArrayInputStream(s.getBytes("utf-8"));
                    XmlPullParser parser = Xml.newPullParser();
                    parser.setInput(is,"UTF-8");
                    int eventType = parser.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        switch (eventType) {
                            case XmlPullParser.START_TAG:
                                if(parser.getName().equals("code")){
                                    eventType=parser.next();
                                    String code = parser.getText();
                                    if ("2".equals(code)){
                                        Log.i("ZhuCe1Activity", "请留意您的短信");
                                        Toast.makeText(ZhuCe1Activity.this, "请留意您的短信", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(ZhuCe1Activity.this, "获取验证码失败", Toast.LENGTH_SHORT).show();
                                        Log.i("ZhuCe1Activity", "获取验证码失败");
                                    }
                                }
                                break;
                        }
                            eventType = parser.next();
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("account", "cf_louxiago");
                map.put("password", DigestUtils.md5Hex("louxiago123"));
                map.put("mobile", phone);
                map.put("content", content);
                return map;
            }
        };
        MyApplication.getQueue().add(request_post);
    }
}
