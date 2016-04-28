package com.example.user.ddkd;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baidu.mobstat.StatService;
import com.example.user.ddkd.utils.AutologonUtil;
import com.example.user.ddkd.utils.Exit;
import com.example.user.ddkd.utils.MyStringRequest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Administrator on 2016/4/24.
 */
public class Activity_feedback extends Activity implements View.OnClickListener {
    private TextView Fcommit;
    private EditText messageedit;
    private ImageView exit;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MyApplication.GET_TOKEN_SUCCESS:
                    Object[] objects= (Object[]) msg.obj;
                    String ms= (String) objects[0];
                    volley_Get(ms);
                    break;
                case MyApplication.GET_TOKEN_ERROR:

                    break;
            }
        }
    };
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
                finish();
                break;
            case R.id.setExit:
                finish();
                break;
        }
    }
    public void volley_Get(final String message){
        SharedPreferences sharedPreferences=getSharedPreferences("config", MODE_PRIVATE);
        String token=sharedPreferences.getString("token", "");
        String ms = null;
        try {
            ms =URLEncoder.encode(message, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url="http://www.louxiago.com/wc/ddkd/admin.php/YiJian/index/YiJian/"+ms+"/did/1/token/"+token;
        Log.i("Feedback",url);
        StringRequest request=new StringRequest(Request.Method.GET, url, new MyStringRequest() {
            @Override
            public void success(Object o) {
                Toast.makeText(Activity_feedback.this,"您的建议已提交",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void tokenouttime() {
                Log.i("Feedback", "token outtime");
                Object[] obj={message};
                AutologonUtil autologonUtil=new AutologonUtil(Activity_feedback.this,handler,obj);
                autologonUtil.volley_Get_TOKEN();
            }
            @Override
            public void yidiensdfsdf() {
                Exit.exit(Activity_feedback.this);
                Toast.makeText(Activity_feedback.this, "您的账户已在异地登录", Toast.LENGTH_SHORT).show();
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
    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }
}
