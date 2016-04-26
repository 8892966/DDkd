package com.example.user.ddkd;

import android.app.Activity;
import android.content.SharedPreferences;
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

/**
 * Created by Administrator on 2016/4/24.
 */
public class Activity_feedback extends Activity implements View.OnClickListener {
    private TextView Fcommit;
    private EditText messageedit;
    private ImageView exit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_feedback);
        messageedit= (EditText) findViewById(R.id.messageedit);
        Fcommit= (TextView) findViewById(R.id.Fcommit);
        exit= (ImageView) findViewById(R.id.setExit);
        exit.setOnClickListener(this);
        Fcommit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Fcommit:
                String message=messageedit.getText().toString();
                volley_Get(message);

                Toast.makeText(Activity_feedback.this,"commie success",Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.setExit:
                finish();
                break;
        }
    }
    public void volley_Get(String message){
        SharedPreferences sharedPreferences=getSharedPreferences("config", MODE_PRIVATE);
        String token=sharedPreferences.getString("token","");
        String url="";
        StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(Activity_feedback.this,"网络连接中断",Toast.LENGTH_SHORT).show();
            }
        });
        request.setTag("Get_feedback");
        MyApplication.getQueue().add(request);
    }
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getQueue().cancelAll("Get_feedback");
    }

}