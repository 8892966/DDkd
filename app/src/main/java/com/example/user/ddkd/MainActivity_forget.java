package com.example.user.ddkd;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.baidu.mobstat.StatService;
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
    private YanZhenMaUtil yanZhenMaUtil;

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
        Intent intent;
        switch (v.getId()){
            case R.id.commitpassword:
                if(yanZhenMaUtil.isYZM(this,et_yanzhengma,et_phone_number)){
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
//                intent=new Intent(this,MainActivity_login.class);
//                startActivity(intent);
                finish();
                break;
            case R.id.tv_button_yanzhengma:
                yanZhenMaUtil.sendYZM(this,et_phone_number,tv_button_yanzhengma);
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
}
