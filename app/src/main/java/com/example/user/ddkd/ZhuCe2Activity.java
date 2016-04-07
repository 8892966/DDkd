package com.example.user.ddkd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.ddkd.R;

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

        et_password= (EditText) findViewById(R.id.et_password);
        et_password2= (EditText) findViewById(R.id.et_password2);
        TextView tv_head_fanghui= (TextView) findViewById(R.id.tv_head_fanghui);
        tv_head_fanghui.setOnClickListener(this);
    }
    public void next(View v){
        String password1 = et_password.getText().toString();
        String password2 = et_password2.getText().toString();
        if(!TextUtils.isEmpty(password1)){
            if (password1.length()>=6&&password1.length()<=32) {
                if (password1.equals(password2)) {
                    Intent intent = new Intent(ZhuCe2Activity.this, ZhuCe3Activity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(ZhuCe2Activity.this, "确认密码不相同", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(ZhuCe2Activity.this,"密码不能低于六位或高于32位",Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(ZhuCe2Activity.this,"请输入密码",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_head_fanghui:
                Intent intent=new Intent(ZhuCe2Activity.this,ZhuCe1Activity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
