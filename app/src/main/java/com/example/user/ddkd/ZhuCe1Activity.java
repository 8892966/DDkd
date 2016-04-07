package com.example.user.ddkd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Random;

/**
 * Created by User on 2016-04-03.
 */
public class ZhuCe1Activity extends Activity implements View.OnClickListener {
    private EditText et_phone_number;
    private EditText et_yanzhengma;
    private CheckBox cb_xieyi;
    private TextView tv_button_yanzhengma;
    private StringBuilder msg;
    private TextView tv_button_yuedu;
    private String yanzhengma;
    private TextView tv_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zhuce1_activity);

        msg = new StringBuilder();
        msg.append("验证码为：");

        et_phone_number = (EditText) findViewById(R.id.et_phone_number);//手机号
        et_yanzhengma = (EditText) findViewById(R.id.et_yanzhengma);//验证码
        cb_xieyi = (CheckBox) findViewById(R.id.cb_xieyi);//协议
        tv_button_yuedu = (TextView) findViewById(R.id.tv_button_yuedu);//阅读协议
        tv_button_yanzhengma = (TextView) findViewById(R.id.tv_button_yanzhengma);//获取验证码
        tv_next = (TextView) findViewById(R.id.tv_next);//下一步按钮

        tv_button_yuedu.setOnClickListener(this);
        tv_button_yanzhengma.setOnClickListener(this);
        cb_xieyi.setOnClickListener(this);
        tv_next.setOnClickListener(this);
    }

    public void next(View v) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_button_yanzhengma:
                Random random = new Random();
                for (int i = 0; i < 6; i++) {
                    msg.append(random.nextInt(10));
                }
                Log.i("ZhuCe1Activity", msg.toString());
                String number = et_phone_number.getText().toString();
                if (!TextUtils.isEmpty(number)) {
                    yanzhengma = msg.toString();
                    sendSMS(number, yanzhengma);
                } else {
                    Toast.makeText(ZhuCe1Activity.this, "请填入您的手机号码", Toast.LENGTH_SHORT).show();
                }
                msg.delete(5, msg.length());
                break;
            case R.id.tv_button_yuedu:
                ///
                Intent intent = new Intent(this, WebActivity.class);
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
                if (!TextUtils.isEmpty(yanzhengma)) {
                    String s = yanzhengma.substring(5, yanzhengma.length());
                    if (s.equalsIgnoreCase(et_yanzhengma.getText().toString())) {
                        Intent intent3 = new Intent(ZhuCe1Activity.this, ZhuCe2Activity.class);
                        startActivity(intent3);
                    } else {
                        Toast.makeText(this, "验证码不正确", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "请填入手机号码", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void sendSMS(String phoneNumber, String message) {
        // 获取短信管理器
        android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
        // 拆分短信内容（手机短信长度限制）
        List<String> divideContents = smsManager.divideMessage(message);
        for (String text : divideContents) {
            smsManager.sendTextMessage(phoneNumber, null, text, null, null);
        }
    }
}
