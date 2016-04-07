package com.example.user.ddkd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
/**
 * Created by Administrator on 2016/4/4.
 */
public class MainActivity_setting extends Activity implements View.OnClickListener {

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_setting);

        TextView exit=(TextView)findViewById(R.id.exit);
        exit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.exit:
                Intent intent=new Intent(this,MainActivity_login.class);
                startActivity(intent);
                break;
        }

    }
}
