package com.example.user.ddkd.utils;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.TextView;
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
    private String yanzhengma;
    private String number;
    private TextView tv_button_yanzhengma;

    /**
     * 判断是否验证吗
     * @param context 上下文
     * @param et_yanzhengma 验证码
     * @return 是就返回true，否则返回false
     */
    public boolean isYZM(Context context,EditText et_yanzhengma,EditText et_phone_number){
        if (yanzhengma == null) {
            Toast.makeText(context, "请输入您的手机号并验证", Toast.LENGTH_LONG).show();
        } else {
            if(yanzhengma.equals(et_yanzhengma.getText().toString())&&number.equals(et_phone_number.getText().toString())){
                return true;
            }else{
                Toast.makeText(context, "验证码不正确", Toast.LENGTH_LONG).show();
            }
        }
        return false;
    }

    /**
     * 获取验证码
     * @param context 上下文
     * @param et_phone_number 电话号码
     */
    public void sendYZM(Context context,EditText et_phone_number,TextView tv_button_yanzhengma){
        this.tv_button_yanzhengma=tv_button_yanzhengma;
        final int mobile_code = (int) ((Math.random() * 9 + 1) * 100000);
        Log.i("ZhuCe1Activity", mobile_code + "");
        number = et_phone_number.getText().toString();
        Log.i("ZhuCe1Activity", number);
        if (!TextUtils.isEmpty(number)) {
            yanzhengma = mobile_code+"";
            countDown();
            volley_Post(context, mobile_code, number);
        } else {
            Toast.makeText(context, "请填入您的手机号码", Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * 获取验证码
     * @param context 上下文
     * @param mobile_code 验证码
     * @param phone 电话号码
     */
    private void volley_Post(final Context context,int mobile_code, final String phone) {
        Log.i("my", "user volley Post");
//        参数一：方法 参数二：地址 参数三：成功回调 参数四：错误回调 。重写getParams 以post参数
        final String content = new String("您的验证码是：" + mobile_code + "。请不要把验证码泄露给其他人。");
        StringRequest request_post = new StringRequest(Request.Method.POST, "http://106.ihuyi.cn/webservice/sms.php?method=Submit", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("YanZhenMaUtil",s);
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
                                    if ("2".equals(code)) {
                                        Log.i("ZhuCe1Activity", "请留意您的短信");
                                        Toast.makeText(context, "请留意您的短信", Toast.LENGTH_SHORT).show();
                                    } else if("4085".equals(code)){
                                        Log.i("ZhuCe1Activity", "同一手机号验证码短信发送超出5条");
                                        Toast.makeText(context, "同一手机号验证码短信发送超出5条", Toast.LENGTH_SHORT).show();
                                    }else
                                    {
                                        Toast.makeText(context, "获取验证码失败", Toast.LENGTH_SHORT).show();
                                        Log.i("ZhuCe1Activity", "获取验证码失败");
                                    }
                                }
                                break;
                        }
                        eventType = parser.next();
                    }
                }catch (Exception e){
                    Log.e("Exception", e.getMessage());
                    Toast.makeText(context,"信息有误!!!",Toast.LENGTH_SHORT).show();
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
}
