package com.example.user.ddkd;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/4/2.
 */
public class MainActivity_login extends Activity implements View.OnClickListener {
    private TextView button;
    private EditText userid;
    private EditText password;
    private TextView insert;
    private TextView forget;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);

        button=(TextView)findViewById(R.id.login);
        userid=(EditText)findViewById(R.id.userInfo);
        password=(EditText)findViewById(R.id.passwordInfo);
        insert=(TextView)findViewById(R.id.insert);
        forget=(TextView)findViewById(R.id.forget);

        button.setOnClickListener(this);
        insert.setOnClickListener(this);
        forget.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==button){
            Toast.makeText(this,"您选择了登录",Toast.LENGTH_SHORT).show();
        }
        if(v==insert){
            Toast.makeText(this,"您选择了注册",Toast.LENGTH_SHORT).show();
        }
        if(v==forget){
            Toast.makeText(this,"您选择了修改",Toast.LENGTH_SHORT).show();
        }

    }
}
