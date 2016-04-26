package com.example.user.ddkd;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baidu.mobstat.StatService;
import com.example.user.ddkd.text.UserInfo;
import com.example.user.ddkd.utils.AutologonUtil;
import com.example.user.ddkd.utils.Exit;
import com.example.user.ddkd.utils.MyStringRequest;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.Principal;

/**
 * Created by Administrator on 2016/4/5.
 */
public class MainActivity_getmoney extends Activity implements View.OnClickListener {
    private TextView textView;
    private EditText getmoney;
    private TextView yue;
    private EditText counter;
    private TextView textView1;
    private TextView sure;
    private EditText Tname;
    private UserInfo userInfo;
    private EditText beizhu;
    private ProgressDialog progressDialog;
    private String getmoney1;
    private String counter1;
    private String tname1;
    private String beizhu1;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MyApplication.GET_TOKEN_SUCCESS:
                    Object[] objects = (Object[]) msg.obj;
                    String getmoney3 = (String) objects[0];
                    String counter3 = (String) objects[1];
                    String tname3 = (String) objects[2];
                    String username3 = (String) objects[3];
                    String beizhu3 = (String) objects[4];
                    volley_get(getmoney3, counter3, tname3, username3, beizhu3);
                    break;
                case MyApplication.GET_TOKEN_ERROR:

                    break;
            }

        }
    };
    private Handler handler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MyApplication.GET_TOKEN_SUCCESS:
                    Volley_Get();
                    break;
                case MyApplication.GET_TOKEN_ERROR:

                    break;
            }
        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.balance_getmoney);
        textView1 = (TextView) findViewById(R.id.exitgetmoney);
        textView1.setOnClickListener(this);
        textView = (TextView) findViewById(R.id.tv_head_fanghui);
        textView.setOnClickListener(this);
        sure = (TextView) findViewById(R.id.sure);
        sure.setOnClickListener(this);

        yue = (TextView) findViewById(R.id.yue);
        getmoney = (EditText) findViewById(R.id.getmoney);
        counter = (EditText) findViewById(R.id.counter);
        Tname = (EditText) findViewById(R.id.Tname);
        beizhu = (EditText) findViewById(R.id.beizhu);
        Volley_Get();
        ExitApplication.getInstance().addActivity(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_head_fanghui:
                finish();
                break;
            case R.id.exitgetmoney:
                finish();
                break;
            case R.id.sure:
                //***********点击确定按钮的时候，提交数据*************
                showProgressDialog();
                SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
                String username = sharedPreferences.getString("username", null);
                Log.i("MainActivity_getmoney", username);
                //panduan1
                getmoney1 = getmoney.getText().toString();
                counter1 = counter.getText().toString();
                tname1 = Tname.getText().toString();
                beizhu1 = beizhu.getText().toString();
//                if (beizhu1.length() <= 10) {
                Log.i("Money2", userInfo.getBalance());
                if (!TextUtils.isEmpty(getmoney1)&&Double.valueOf(getmoney1)>100) {
                    if (Double.valueOf(userInfo.getBalance()) > 100) {
                        if (Double.valueOf(getmoney1) < Double.valueOf(userInfo.getBalance())) {
                            if (!TextUtils.isEmpty(counter1)) {
                                if (!TextUtils.isEmpty(tname1)) {
                                    if (TextUtils.isEmpty(beizhu1)) {
                                        beizhu1 = "无";
                                        volley_get(getmoney1, counter1, tname1, username, beizhu1);
                                    } else {
                                        if (beizhu1.length() <= 10) {
                                            volley_get(getmoney1, counter1, tname1, username, beizhu1);
                                            break;
                                        } else {
                                            closeProgressDialog();
                                            Toast.makeText(MainActivity_getmoney.this, "备注内容超过限制，请重新输入", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                } else {
                                    closeProgressDialog();
                                    Toast.makeText(MainActivity_getmoney.this, "账户人不能为空", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                closeProgressDialog();
                                Toast.makeText(MainActivity_getmoney.this, "提现账户不能为空", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            closeProgressDialog();
                            Toast.makeText(MainActivity_getmoney.this, "提现金额输入有误，请重新输入", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        closeProgressDialog();
                        Toast.makeText(MainActivity_getmoney.this, "您的余额不足100元", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    closeProgressDialog();
                    Toast.makeText(MainActivity_getmoney.this, "提现金额不能为空或提现金额小于100", Toast.LENGTH_SHORT).show();
                }
        }
    }

    public void volley_get(final String getmoney, final String counter, final String tname,
                           final String username, final String beizhu) {
        SharedPreferences sharedPreferences1 = getSharedPreferences("config", MODE_PRIVATE);
        String token = sharedPreferences1.getString("token", null);
        String tname2 = null;
        String username2 = null;
        String beizhu2 = null;

        try {
            tname2 = URLEncoder.encode(tname, "utf-8");
            username2 = URLEncoder.encode(username, "utf-8");
            beizhu2 = URLEncoder.encode(beizhu, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = "http://www.louxiago.com/wc/ddkd/admin.php/Turnover/withdrawCash/money/" + getmoney + "/Tname/" + tname2 + "/counter/" + counter + "/name/" + username2 + "/extra/" + beizhu2 + "/token/" + token;
        Log.i("URL", url);
        //******************将用户的提现信息提交给服务器
        StringRequest request=new StringRequest(Request.Method.GET, url, new MyStringRequest() {
            @Override
            public void success(Object o) {
                String s= (String) o;
                closeProgressDialog();
                if (!s.equals("ERROR")) {
                    s = s.substring(1, s.length() - 1);
                    //**************返回一个参数，说明提交的情况*****************
                    finish();
                    Toast.makeText(MainActivity_getmoney.this, "提现申请已提交", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(MainActivity_getmoney.this, "提交内容有误，请核对您的信息", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void tokenouttime() {
                Log.e("MainActivity_getmoney", "token outtime");
                Object[] obj = {getmoney, counter, tname, username, beizhu};
                AutologonUtil autologonUtil = new AutologonUtil(MainActivity_getmoney.this, handler, obj);
                autologonUtil.volley_Get_TOKEN();
            }

            @Override
            public void yidiensdfsdf() {
                Exit.exit(MainActivity_getmoney.this);
                Toast.makeText(MainActivity_getmoney.this, "您的账户已在异地登录", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MainActivity_getmoney.this, "网络连接中断", Toast.LENGTH_SHORT).show();
            }
        });
        request.setTag("abcGet_getmoney");
        MyApplication.getQueue().add(request);
    }

    public void Volley_Get() {
        SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);
        String url = "http://www.louxiago.com/wc/ddkd/admin.php/Turnover/center/token/" + token;
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new MyStringRequest() {
            @Override
            public void success(Object o) {
                String s= (String) o;
                if (!s.equals("ERROR")) {
                    Gson gson = new Gson();
                    userInfo = gson.fromJson(s, UserInfo.class);
//                        Log.i("Money", String.valueOf(userInfo.getBalance()));
                    yue.setText(userInfo.getBalance());
                } else {
                    Toast.makeText(MainActivity_getmoney.this, "网络连接出错", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void tokenouttime() {
                Log.e("Main_balance", "token outtime");
                AutologonUtil autologonUtil = new AutologonUtil(MainActivity_getmoney.this, handler1, null);
                autologonUtil.volley_Get_TOKEN();
            }

            @Override
            public void yidiensdfsdf() {
                Exit.exit(MainActivity_getmoney.this);
                Toast.makeText(MainActivity_getmoney.this, "您的账户已在异地登录", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        stringRequest.setTag("Get_getmoney_userinfo");
        MyApplication.getQueue().add(stringRequest);
    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(MainActivity_getmoney.this);
            progressDialog.setMessage("正在提交........");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
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
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getQueue().cancelAll("abcGet_getmoney");
        MyApplication.getQueue().cancelAll("Get_getmoney_userinfo");
    }

}
