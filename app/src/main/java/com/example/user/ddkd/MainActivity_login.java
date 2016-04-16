package com.example.user.ddkd;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Administrator on 2016/4/2.
 */
public class MainActivity_login extends Activity implements View.OnClickListener {
    private TextView button;
    private EditText userid1;
    private EditText password1;
    private TextView insert;
    private TextView forget;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);

        button = (TextView) findViewById(R.id.login);
        userid1 = (EditText) findViewById(R.id.userInfo);
        password1 = (EditText) findViewById(R.id.passwordInfo);
        insert = (TextView) findViewById(R.id.insert);
        forget = (TextView) findViewById(R.id.forget);
        button.setOnClickListener(this);
        insert.setOnClickListener(this);
        forget.setOnClickListener(this);
    }

    public void volley_Get(String userid, String password) {
        String url = "http://www.louxiago.com/wc/ddkd/admin.php/User/login/phone/18813972184/password/123456";
        //"?"+"phone="+userid+"&password="+password;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("volley_Get", s);
                s = s.substring(1, s.length()-1);
                //******************当提交成功以后，后台会返回一个参数来说明是否提交/验证成功******************
                SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.putString("token", s);
                Log.e("volley_Get", s);
                edit.commit();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        });
        request.setTag("abcGet_login");
        MyApplication.getQueue().add(request);
    }
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.login:
                //***********判断服务器返回的参数，根据参数来判断验证是否通过**********
                String phone = userid1.getText().toString();
                String password = password1.getText().toString();
                volley_Get(phone, password);
                intent = new Intent(this, JieDangActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.insert:
                intent = new Intent(this, ZhuCe1Activity.class);
                startActivity(intent);
                break;
            case R.id.forget:
                intent = new Intent(this, MainActivity_forget.class);
                startActivity(intent);
                break;
        }
    }
}
