package com.example.user.ddkd;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.user.ddkd.utils.PasswordUtil;
import com.example.user.ddkd.utils.YanZhenMaUtil;

import org.w3c.dom.Text;

/**
 * Created by Administrator on 2016/4/24.
 */
public class Activity_updpwd extends Activity implements View.OnClickListener {
    private EditText phone;
    private EditText yanzhenma;
    private EditText password2;
    private EditText password3;
    private TextView tv_button_yanzhengma;
    private TextView sure;
    private ImageView exit;
    private YanZhenMaUtil yanZhenMaUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_updpwd);
        phone = (EditText) findViewById(R.id.phone);
        yanzhenma = (EditText) findViewById(R.id.yanzhengma);
        tv_button_yanzhengma = (TextView) findViewById(R.id.tv_button_yanzhengma);
        password2 = (EditText) findViewById(R.id.password2);
        password3 = (EditText) findViewById(R.id.password3);
        exit = (ImageView) findViewById(R.id.setExit);
        exit.setOnClickListener(this);
        sure = (TextView) findViewById(R.id.sure);
        sure.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sure:
                String phone1 = phone.getText().toString();
                String yzm = yanzhenma.getText().toString();
                String pwd2 = password2.getText().toString();
                String pwd3 = password3.getText().toString();
                if (TextUtils.isEmpty(pwd2)) {
                    if (TextUtils.isEmpty(pwd3)) {
                        if (PasswordUtil.isSame(this, pwd2, pwd3)) {
                            volley_Get(phone1, pwd3, yzm);
                            finish();
                            break;
                        } else {
                            Toast.makeText(Activity_updpwd.this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Activity_updpwd.this, "确认密码不能为空", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Activity_updpwd.this, "新密码不能为空", Toast.LENGTH_SHORT).show();
                }
            case R.id.setExit:
                finish();
                break;
            case R.id.tv_button_yanzhengma:
                yanZhenMaUtil.sendYZM(this, phone, yanzhenma);
                countDown();
                break;
        }
    }

    public void volley_Get(String phone, String newpwd, String verift) {
        String url = "http://www.louxiago.com/wc/ddkd/admin.php/User/UpdatePsw/phone/" + phone + "/newpsw/" + newpwd + "/verify/" + verift;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (!s.equals("\"token outtime\"")) {
                    if (!s.equals("\"ERROR\"")) {
                        Toast.makeText(Activity_updpwd.this, "修改成功", Toast.LENGTH_SHORT).show();
                    }else{

                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(Activity_updpwd.this,"网络连接中断",Toast.LENGTH_SHORT).show();
            }
        });
        request.setTag("Get_updpwd");
        MyApplication.getQueue().add(request);
    }

    private void countDown() {
//      tv_bt_verify你要设置动画的view
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 60);//从0到30计时
        valueAnimator.setDuration(60000);//持续时间为60s
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                tv_button_yanzhengma.setText("剩余" + String.valueOf(60 - value) + "s");
                if (value == 60) {
                    //tv_bt_verify.setBackgroundResource(R.drawable.ret_orange);//30s后的背景
                    tv_button_yanzhengma.setEnabled(true);//30s后设置可以点击
                    tv_button_yanzhengma.setText("获取验证码");
                } else {
                    tv_button_yanzhengma.setEnabled(false);//30s内设置不可以点击
                    //time.setBackgroundResource(R.drawable.ret);//30s内的背景
                }
            }
        });
        valueAnimator.setInterpolator(new LinearInterpolator());//设置变化值为线性变化
        valueAnimator.start();//动画开始
    }
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getQueue().cancelAll("Get_updpwd");
    }
}
