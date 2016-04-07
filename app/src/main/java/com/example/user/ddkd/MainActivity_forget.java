package com.example.user.ddkd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/4/6.
 */
public class MainActivity_forget extends Activity implements View.OnClickListener {
    private TextView commit;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password_activity);

        commit=(TextView)findViewById(R.id.commitpassword);
        commit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.commitpassword:
                Toast.makeText(this,"密码修改成功，请重新登录",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(this,MainActivity_login.class);
                startActivity(intent);
                break;
        }
    }
}
