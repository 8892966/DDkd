package com.example.user.ddkd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/4/5.
 */
public class MainActivity_userinfo extends Activity implements View.OnClickListener {
    TextView textView;
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_userinfo);
        textView=(TextView)findViewById(R.id.tv_head_fanghui);
        textView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.tv_head_fanghui:
                finish();
                break;
        }
    }
}
