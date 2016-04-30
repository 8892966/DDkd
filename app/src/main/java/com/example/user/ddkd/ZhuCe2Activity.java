package com.example.user.ddkd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.example.user.ddkd.beam.SignUpInfo;
import com.example.user.ddkd.utils.PasswordUtil;

/**
 * Created by User on 2016-04-03.
 */
public class ZhuCe2Activity extends Activity implements View.OnClickListener {
    private EditText et_password;
    private EditText et_password2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zhuce2_activity);
        et_password = (EditText) findViewById(R.id.et_password);
        et_password2 = (EditText) findViewById(R.id.et_password2);
        TextView tv_head_fanghui = (TextView) findViewById(R.id.tv_head_fanghui);
        tv_head_fanghui.setOnClickListener(this);
        SignUpInfo signUpInfo = (SignUpInfo) getIntent().getSerializableExtra("SignUpInfo");
        if (signUpInfo.getPassword() != null) {
            et_password.setText(signUpInfo.getPassword());
        }
    }

    public void next(View v) {
        String password1 = et_password.getText().toString();
        String password2 = et_password2.getText().toString();
        if (PasswordUtil.isSame(ZhuCe2Activity.this, password1, password2)) {
            //注册信息
            SignUpInfo signUpInfo = getSignUpInfo(password1);
            Intent intent = new Intent(ZhuCe2Activity.this, ZhuCe3Activity.class);
            intent.putExtra("SignUpInfo", signUpInfo);//传递注册信息
            if (getIntent().getSerializableExtra("picture") != null) {
                intent.putExtra("picture", getIntent().getSerializableExtra("picture"));
            }
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_head_fanghui:
                Intent intent = new Intent(ZhuCe2Activity.this, ZhuCe1Activity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Intent intent = new Intent(ZhuCe2Activity.this, ZhuCe1Activity.class);
        startActivity(intent);
        finish();
    }

    //获取已有信息并添加新的信息
    private SignUpInfo getSignUpInfo(String password1) {
        SignUpInfo signUpInfo = (SignUpInfo) getIntent().getSerializableExtra("SignUpInfo");
        signUpInfo.setPassword(password1);
        return signUpInfo;
    }

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

}
