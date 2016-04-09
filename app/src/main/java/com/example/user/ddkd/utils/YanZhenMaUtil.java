package com.example.user.ddkd.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.user.ddkd.MyApplication;
import com.example.user.ddkd.ZhuCe2Activity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by User on 2016-04-09.
 */
public class YanZhenMaUtil {
    public static String yanzhengma;
    public static boolean isYZM(Context context,EditText et_yanzhengma){
        if (yanzhengma == null) {
            Toast.makeText(context, "请输入您的手机号并验证", Toast.LENGTH_LONG).show();
        } else {
            if(yanzhengma.equals(et_yanzhengma.getText().toString())){
                return true;
            }else{
                Toast.makeText(context, "验证码不正确", Toast.LENGTH_LONG).show();
            }
        }
        return false;
    }
    public static void sendYZM(Context context,EditText et_phone_number){
        final int mobile_code = (int) ((Math.random() * 9 + 1) * 100000);
        yanzhengma = mobile_code+"";
        Log.i("ZhuCe1Activity", mobile_code + "");
        String number = et_phone_number.getText().toString();
        Log.i("ZhuCe1Activity", number);
        if (!TextUtils.isEmpty(number)) {
            volley_Post(context,mobile_code, number);
        } else {
            Toast.makeText(context, "请填入您的手机号码", Toast.LENGTH_SHORT).show();
        }
    }
    private static void volley_Post(final Context context,int mobile_code, final String phone) {
        Log.i("my", "user volley Post");
//        参数一：方法 参数二：地址 参数三：成功回调 参数四：错误回调 。重写getParams 以post参数
        final String content = new String("您的验证码是：" + mobile_code + "。请不要把验证码泄露给其他人。");
        StringRequest request_post = new StringRequest(Request.Method.POST, "http://106.ihuyi.cn/webservice/sms.php?method=Submit", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    InputStream is = new ByteArrayInputStream(s.getBytes("utf-8"));
                    XmlPullParser parser = Xml.newPullParser();
                    parser.setInput(is, "UTF-8");
                    int eventType = parser.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        switch (eventType) {
                            case XmlPullParser.START_TAG:
                                if (parser.getName().equals("code")) {
                                    eventType=parser.next();
                                    String code = parser.getText();
                                    Toast.makeText(context,code,Toast.LENGTH_SHORT).show();
                                    if ("2".equals(code)) {
                                        Log.i("ZhuCe1Activity", "请留意您的短信");
                                        Toast.makeText(context, "请留意您的短信", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(context, "获取验证码失败", Toast.LENGTH_SHORT).show();
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
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("account", "cf_louxiago");
                map.put("password", "louxiago123");
                map.put("mobile", phone);
                map.put("content", content);
                return map;
            }
        };
        MyApplication.getQueue().add(request_post);
    }
}
