package com.example.user.ddkd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by Administrator on 2016/4/2.
 */
public class MainActivity_main extends Activity implements View.OnClickListener {

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_userinfo);

        RelativeLayout moneysum=(RelativeLayout)findViewById(R.id.moneysum);
        moneysum.setOnClickListener(this);
        LinearLayout detauils=(LinearLayout)findViewById(R.id.details);
        detauils.setOnClickListener(this);
        LinearLayout userinfo=(LinearLayout)findViewById(R.id.userInfo);
        userinfo.setOnClickListener(this);
        LinearLayout setting=(LinearLayout)findViewById(R.id.setting);
        setting.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.moneysum:
                Intent intent3=new Intent(this,MainActivity_balance.class);
                startActivity(intent3);
                break;
            case R.id.details:
                Intent intent=new Intent(this,details.class);
                startActivity(intent);
                break;
            case R.id.userInfo:
                Intent intent1=new Intent(this,MainActivity_userinfo.class);
                startActivity(intent1);
                break;
            case R.id.setting:
                Intent intent2=new Intent(this,MainActivity_setting.class);
                startActivity(intent2);

                break;
        }
    }
}
