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

/**
 * Created by Administrator on 2016/4/24.
 */
public class Activity_feedback extends Activity implements View.OnClickListener {
    private EditText userid;
    private TextView Fcommit;
    private EditText messageedit;
    private ImageView exit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_feedback);

        userid= (EditText) findViewById(R.id.userid);
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
                String phone=userid.getText().toString();
                String message=messageedit.getText().toString();
                volley_Get(phone,message);

                Toast.makeText(Activity_feedback.this,"commie success",Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.setExit:
                finish();
                break;
        }
    }
    public void volley_Get(String phone,String message){
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
        request.setTag("Get_feedback");
        MyApplication.getQueue().add(request);
    }
}
