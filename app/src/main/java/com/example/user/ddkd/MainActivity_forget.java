package com.example.user.ddkd;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.user.ddkd.beam.OrderInfo;
import com.example.user.ddkd.utils.PasswordUtil;
import com.example.user.ddkd.utils.YanZhenMaUtil;

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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password_activity);

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
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.commitpassword:
                if(YanZhenMaUtil.isYZM(this,et_yanzhengma)){
                    String password1 = et_new_password.getText().toString();
                    String password2 = et_new_password2.getText().toString();
                    if(PasswordUtil.isSame(this, password1, password2)) {
//                        volley_ForgetPassword_GET(password1,);
                        Toast.makeText(this, "密码修改成功，请重新登录", Toast.LENGTH_SHORT).show();
                        intent = new Intent(this, MainActivity_login.class);
                        startActivity(intent);
                        finish();
                    }
                }
                break;
            case R.id.tv_head_fanghui:
                intent=new Intent(this,MainActivity_login.class);
                startActivity(intent);
                finish();
                break;
            case R.id.tv_button_yanzhengma:
                countDown();
                YanZhenMaUtil.sendYZM(this,et_phone_number);
                break;
        }
    }
    private void countDown() {
//        tv_bt_verify你要设置动画的view
        ValueAnimator valueAnimator=ValueAnimator.ofInt(0,60);//从0到30计时
        valueAnimator.setDuration(60000);//持续时间为60s
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                tv_button_yanzhengma.setText("剩余（"+String.valueOf(60-value)+"s）");
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

    //忘记密码，申请修改密码
    private void volley_ForgetPassword_GET(final OrderInfo info, final String State) {
        preferences = getSharedPreferences("config", MODE_PRIVATE);
        String token = preferences.getString("token", "");
        String url = "" +"/token/" + token;
//        参数一：方法 参数二：地址 参数三：成功回调 参数四：错误回调 。重写getParams 以post参数
        StringRequest request_post = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("volley_ForgetPassword_GET", s);
                if ("SUCCESS".equals(s)) {

                }else{

                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        request_post.setTag("volley_ForgetPassword_GET");
        MyApplication.getQueue().add(request_post);
    }

}
