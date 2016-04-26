package com.example.user.ddkd;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.w3c.dom.Text;

/**
 * Created by Administrator on 2016/4/24.
 */
public class Activity_updpwd extends Activity implements View.OnClickListener {
    private EditText password1;
    private EditText password2;
    private EditText password3;
    private TextView sure;
    private ImageView exit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_updpwd);

        password1= (EditText) findViewById(R.id.password1);
        password2= (EditText) findViewById(R.id.password2);
        password3= (EditText) findViewById(R.id.password3);
        exit= (ImageView) findViewById(R.id.setExit);
        exit.setOnClickListener(this);
        sure= (TextView) findViewById(R.id.sure);
        sure.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sure:
                String pwd1=password1.getText().toString();
                String pwd2=password2.getText().toString();
                String pwd3=password3.getText().toString();

                volley_Get(pwd1,pwd2,pwd3);

                Toast.makeText(Activity_updpwd.this,"修改成功",Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.setExit:
                finish();
                break;
        }
    }

    public void volley_Get(String pwd1,String pwd2,String pwd3){
        String url="";
        StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        request.setTag("Get_updpwd");
        MyApplication.getQueue().add(request);
    }
}
