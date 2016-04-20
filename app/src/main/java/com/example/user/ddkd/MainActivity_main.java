package com.example.user.ddkd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by Administrator on 2016/4/2.
 */
public class MainActivity_main extends Activity implements View.OnClickListener {
    private ImageView announce;
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_userinfo);
        ImageView imageView=(ImageView)findViewById(R.id.exituserinfo);
        imageView.setOnClickListener(this);
        announce= (ImageView) findViewById(R.id.announce);
        announce.setOnClickListener(this);

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
        Intent intent;
        switch (v.getId()){
            case R.id.moneysum:
                intent=new Intent(this,MainActivity_balance.class);
                startActivity(intent);
                break;
            case R.id.details:
                intent=new Intent(this,details.class);
                startActivity(intent);
                break;
            case R.id.userInfo:
                intent=new Intent(this,MainActivity_userinfo.class);
                startActivity(intent);
                break;
            case R.id.setting:
                intent=new Intent(this,MainActivity_setting.class);
                startActivity(intent);
                break;
            case R.id.exituserinfo:
                finish();
                break;
            case R.id.announce:
                intent=new Intent(MainActivity_main.this,Announce.class);
                startActivity(intent);
                break;
        }
    }
}
