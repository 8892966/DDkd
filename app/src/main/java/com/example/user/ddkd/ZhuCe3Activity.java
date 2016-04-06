package com.example.user.ddkd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Created by User on 2016-04-03.
 */
public class ZhuCe3Activity extends Activity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zhuce3_activity);
        TextView tv_head_fanghui= (TextView) findViewById(R.id.tv_head_fanghui);
        tv_head_fanghui.setOnClickListener(this);
    }
    public void next(View v){
        Intent intent=new Intent(ZhuCe3Activity.this,ZhuCe4Activity.class);
        startActivity(intent);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_head_fanghui:
                Intent intent=new Intent(ZhuCe3Activity.this,ZhuCe2Activity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
