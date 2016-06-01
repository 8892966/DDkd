package com.example.user.ddkd;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.user.ddkd.utils.AutologonUtil;
import com.example.user.ddkd.utils.Exit;
import com.example.user.ddkd.utils.MyStringRequest;
import com.example.user.ddkd.utils.WiteUtils;

/**
 * Created by Administrator on 2016/6/1.
 */
public class Activity_ChangeMax extends BaseActivity implements View.OnClickListener {
    private TextView exit;
    private EditText maxnum;
    private TextView sure;
    private WiteUtils witeUtils;
    private Handler handle_Max=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MyApplication.GET_TOKEN_SUCCESS:
                    int numb= (int) msg.obj;
                    Log.i("numb_Max",String.valueOf(numb));
                    Volley_CM_GET(numb);
                    break;
                case MyApplication.GET_TOKEN_ERROR:

                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_changemax);

        exit= (TextView) findViewById(R.id.tv_head_fanghui);
        exit.setOnClickListener(this);
        maxnum= (EditText) findViewById(R.id.maxnum);
        sure= (TextView) findViewById(R.id.sure);
        sure.setOnClickListener(this);
        witeUtils=new WiteUtils(Activity_ChangeMax.this);

        /**
         * 获取最大接单量的接口还没有实现
         */
    }

    @Override
    protected boolean addStack() {
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.exit:
                finish();
                break;
            case R.id.sure:
                int number=Integer.valueOf(maxnum.getText().toString());
                if (String.valueOf(number).equals("")&&number>0){
                    Volley_CM_GET(number);
                    witeUtils.showProgressDialog();
                }
                break;
            default:
                break;
        }
    }

    private void Volley_CM_GET(final int amount){
        SharedPreferences sharedPreferences=getSharedPreferences("config",MODE_PRIVATE);
        String token=sharedPreferences.getString("token", "");
        String url="http://www.louxiago.com/wc/ddkdtest/admin.php/Order/setOrderAmount/amount/"+amount+"/token/"+token;
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new MyStringRequest() {
            @Override
            public void success(Object o) {
                String s= (String) o;
                if ("SUCCESS".equals(s)){
                    witeUtils.closeProgressDialog();
                    new AlertDialog.Builder(Activity_ChangeMax.this).setTitle("提交成功").setMessage("你已成为指定镖师").show();
                    finish();
                }else{
                    witeUtils.closeProgressDialog();
                    Toast.makeText(Activity_ChangeMax.this, "提交失败，请重试", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void tokenouttime() {
                Log.i("token outtime", "Volley_Max_Get");
                AutologonUtil autologonUtil=new AutologonUtil(Activity_ChangeMax.this,handle_Max,amount);
                autologonUtil.volley_Get_TOKEN();
            }

            @Override
            public void yidiensdfsdf() {
                Exit.exit(Activity_ChangeMax.this);
                Toast.makeText(Activity_ChangeMax.this, "您的账户已在异地登录", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(Activity_ChangeMax.this, "网络连接中断", Toast.LENGTH_SHORT).show();
            }
        });
        stringRequest.setTag("Volley_CM_GET");
        MyApplication.getQueue().add(stringRequest);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getQueue().cancelAll("Volley_CM_GET");
    }
}
