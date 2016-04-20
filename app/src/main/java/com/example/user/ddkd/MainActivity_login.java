package com.example.user.ddkd;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.tencent.stat.StatService;

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
    private ProgressDialog progressDialog;
    private CheckBox rembpwd;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);

        button = (TextView) findViewById(R.id.login);
        userid1 = (EditText) findViewById(R.id.userInfo);
        password1 = (EditText) findViewById(R.id.passwordInfo);
        insert = (TextView) findViewById(R.id.insert);
        forget = (TextView) findViewById(R.id.forget);
        rembpwd= (CheckBox) findViewById(R.id.rembpwd);


        SharedPreferences preferences01=getSharedPreferences("config",MODE_PRIVATE);
        userid1.setText(preferences01.getString("phone",null));
        password1.setText(preferences01.getString("password",null));

        button.setOnClickListener(this);
        insert.setOnClickListener(this);
        forget.setOnClickListener(this);
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

    public void volley_Get(final String userid, final String password) {

        String url = "http://www.louxiago.com/wc/ddkd/admin.php/User/login/phone/" + userid + "/password/" + password;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
//                Log.e("Get_login", s);
                closeProgressDialog();//*****关闭加载提示框*****
                if (!s.equals("ERROR")){
                    s = s.substring(1, s.length() - 1);
                    //******************当提交成功以后，后台会返回一个参数来说明是否提交/验证成功******************
                    SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
                    SharedPreferences.Editor edit = sharedPreferences.edit();
                    edit.putString("token", s);
//                    Log.e("volley_Get", s);
                    edit.commit();
                    Intent intent = new Intent(MainActivity_login.this, JieDangActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.i("Error", "ERROR");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MainActivity_login.this,"网络连接出错",Toast.LENGTH_SHORT).show();
                Log.e("onErrorResponse", "onErrorResponse");
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
                showProgressDialog();//*****启动加载提示框*****
                //***********判断服务器返回的参数，根据参数来判断验证是否通过**********
                String phone = userid1.getText().toString();
                String password = password1.getText().toString();
                if(rembpwd.isChecked()){
                    SharedPreferences preferences=getSharedPreferences("config",MODE_PRIVATE);
                    SharedPreferences.Editor editor=preferences.edit();
                    editor.putString("phone",phone);
                    editor.putString("password",password);
                    editor.commit();
//                    Log.i("save","保存成功");
                }
                if(!TextUtils.isEmpty(phone)){
                    if(!TextUtils.isEmpty(password)){
                        volley_Get(phone, password);
                    }else{
                        Toast.makeText(MainActivity_login.this,"密码不能为空",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(MainActivity_login.this,"账号不能为空",Toast.LENGTH_SHORT).show();
                }
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
    private void showProgressDialog(){
        if(progressDialog==null){
            progressDialog=new ProgressDialog(MainActivity_login.this);
            progressDialog.setMessage("正在登陆.......");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }
    private void closeProgressDialog(){
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
    }
}
