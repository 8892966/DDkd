package com.example.user.ddkd;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.android.tpush.XGPushManager;

/**
 * Created by Administrator on 2016/4/4.
 */
public class MainActivity_setting extends Activity implements View.OnClickListener {

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_setting);
        ImageView imageView=(ImageView)findViewById(R.id.setExit);
        imageView.setOnClickListener(this);

        TextView exit=(TextView)findViewById(R.id.exit);
        exit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.exit:
                intent=new Intent(this,MainActivity_login.class);
                startActivity(intent);
                XGPushManager.unregisterPush(this);
                Toast.makeText(this,"退出成功",Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.setExit:
                finish();
                break;
        }

    }
}
