package com.example.user.ddkd;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

/**
 * Created by Administrator on 2016/4/24.
 */
public class Activity_updpwd extends Activity implements View.OnClickListener {
    private EditText password1;
    private EditText password2;
    private EditText password3;
    private TextView sure;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_updpwd);

        password1= (EditText) findViewById(R.id.password1);
        password2= (EditText) findViewById(R.id.password2);
        password3= (EditText) findViewById(R.id.password3);
        sure= (TextView) findViewById(R.id.sure);
        sure.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sure:
                Toast.makeText(Activity_updpwd.this,"修改成功",Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }
}
