package com.example.user.ddkd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/4/5.
 */
public class MainActivity_getmoney extends Activity implements View.OnClickListener {
    private TextView textView;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.balance_getmoney);
        TextView textView1=(TextView)findViewById(R.id.exitgetmoney);
        textView1.setOnClickListener(this);

        textView=(TextView)findViewById(R.id.tv_head_fanghui);
        textView.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_head_fanghui:
                finish();
                break;
            case R.id.exitgetmoney:
                finish();
                break;
        }
    }
}
