package com.example.user.ddkd;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.example.user.ddkd.text.UserInfo;

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
    private String tname;
    private String beizhu1;

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
                tname = Tname.getText().toString();
                beizhu1 = beizhu.getText().toString();
//                if (beizhu1.length() <= 10) {
                if (!TextUtils.isEmpty(getmoney1)) {
                    if (!TextUtils.isEmpty(counter1)) {
                        if (!TextUtils.isEmpty(tname)) {
                            if (TextUtils.isEmpty(beizhu1)) {
                                beizhu1 = "无";
                                volley_get(getmoney1, counter1, tname, username, beizhu1);
                            } else {
                                if (beizhu1.length() <= 10) {
                                    volley_get(getmoney1, counter1, tname, username, beizhu1);
                                    finish();
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
                    Toast.makeText(MainActivity_getmoney.this, "提现金额不能为空", Toast.LENGTH_SHORT).show();
                }
//                } else {
//                    closeProgressDialog();
//                    Toast.makeText(MainActivity_getmoney.this, "备注内容超过限制，请重新输入", Toast.LENGTH_SHORT).show();
//                }
        }
    }

    public void volley_get(String getmoney, String counter, String tname, String username, String beizhu2) {
        SharedPreferences sharedPreferences1 = getSharedPreferences("config", MODE_PRIVATE);
        String token = sharedPreferences1.getString("token", null);
        try {
            tname = URLEncoder.encode(tname, "utf-8");
            username = URLEncoder.encode(username, "utf-8");
            beizhu2 = URLEncoder.encode(beizhu2, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = "http://www.louxiago.com/wc/ddkd/admin.php/Turnover/withdrawCash/money/" + getmoney + "/Tname/" + tname + "/counter/" + counter + "/name/" + username + "/extra/" + beizhu2 + "/token/" + token;
//        Log.i("URL", url);
        //将用户的提现信息提交给服务器
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            public void onResponse(String s) {
                Log.i("Get_gemoney", s);
                closeProgressDialog();
                s = s.substring(1, s.length() - 1);
                //**************返回一个参数，说明提交的情况*****************
                if ("SUCCESS".equals(s)) {
                    Toast.makeText(MainActivity_getmoney.this, "提现申请已提交", Toast.LENGTH_LONG).show();
                } else if ("ERROR".equals(s)) {
                    Toast.makeText(MainActivity_getmoney.this, "提交内容有误，请核对您的信息", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        request.setTag("abcGet_getmoney");
        MyApplication.getQueue().add(request);
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
}
