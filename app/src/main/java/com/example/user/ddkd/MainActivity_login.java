package com.example.user.ddkd;

import android.app.Activity;
import android.content.Intent;
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
        Intent intent;
       switch (v.getId()){
           case R.id.login:
               intent=new Intent(this,JieDangActivity.class);
               startActivity(intent);
               break;
           case R.id.insert:
               intent=new Intent(this,ZhuCe1Activity.class);
               startActivity(intent);
               break;
           case R.id.forget:
               intent=new Intent(this,MainActivity_forget.class);
               startActivity(intent);
               break;
       }
    }
}
