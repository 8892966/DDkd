package com.example.user.ddkd;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baidu.mobstat.StatService;
import com.example.user.ddkd.text.Payment;
import com.example.user.ddkd.text.UserInfo;
import com.example.user.ddkd.utils.AutologonUtil;
import com.example.user.ddkd.utils.Exit;
import com.example.user.ddkd.utils.MyStringRequest;
import com.example.user.ddkd.utils.TimeUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/5.
 */
public class MainActivity_balance extends Activity implements View.OnClickListener {
    private List<Payment> paymentlist = new ArrayList<Payment>();
    private TextView textView;
    private MyAdapter myAdapter = new MyAdapter();
    ;
    private TextView balance;
    private UserInfo userInfo;
    private TextView tongzhi;
    private ListView viewById;
    private Handler handler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MyApplication.GET_TOKEN_SUCCESS:
                    UserInfo userInfo = (UserInfo) msg.obj;
                    volley_Get_Balance(userInfo);
                    break;
                case MyApplication.GET_TOKEN_ERROR:
                    break;
            }
        }
    };
    private Handler handler2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MyApplication.GET_TOKEN_SUCCESS:
                    List<Payment> paymentList = (List<Payment>) msg.obj;
                    tongzhi.setVisibility(View.GONE);
                    Volley_Get(paymentList);
                    break;
                case MyApplication.GET_TOKEN_ERROR:
                    break;
            }
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_balance);
        balance = (TextView) findViewById(R.id.balance);
        TextView exit = (TextView) findViewById(R.id.tv_head_fanghui);
        exit.setOnClickListener(this);
        tongzhi = (TextView) findViewById(R.id.tongzhi);
        textView = (TextView) findViewById(R.id.getmoney);
        textView.setOnClickListener(this);
        viewById = (ListView) findViewById(R.id.listviewbalance);
        Volley_Get(paymentlist);
        volley_Get_Balance(userInfo);
        ExitApplication.getInstance().addActivity(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.getmoney:
                intent = new Intent(this, MainActivity_getmoney.class);
                startActivity(intent);
                break;
            case R.id.tv_head_fanghui:
                finish();
                Log.i("exit", "exit");
                break;
        }
    }
    public void Volley_Get(final List<Payment> paymentlist2) {
        SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);
        String url = "http://www.louxiago.com/wc/ddkd/admin.php/Turnover/takeoutrecord/token/" + token;
        Log.i("Payment_url", url);
        //**********从后台返回一个参数来说明数据的获取状况**********
        StringRequest request = new StringRequest(Request.Method.GET, url, new MyStringRequest() {
            @Override
            public void success(Object o) {
                String s = o.toString();
                Log.i("Payment", s);
                if (!s.equals("")) {
                    if (!s.equals("ERROR")) {
                        Type listv = new TypeToken<LinkedList<Payment>>() {
                        }.getType();
                        Gson gson = new Gson();
                        paymentlist = gson.fromJson(s, listv);
                        if (paymentlist != null) {
                            for (Payment payments2 : paymentlist) {
                                payments2.setTime(TimeUtil.getStrTime(payments2.getTime()));
                                Log.i("TIME",payments2.getTime());
                            }
                            tongzhi.setVisibility(View.GONE);
                            viewById.setAdapter(myAdapter);
                            myAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(MainActivity_balance.this, "暂时无收支明细", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MainActivity_balance.this, "网络连接出错", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity_balance.this, "暂时无收支明细", Toast.LENGTH_SHORT).show();
                    Log.i("Error_Payment", "Payment is null");
                }
            }

            @Override
            public void tokenouttime() {
                Log.i("token outtime", "token outtime");
                AutologonUtil autologonUtil = new AutologonUtil(MainActivity_balance.this, handler2, paymentlist2);
                autologonUtil.volley_Get_TOKEN();
            }

            @Override
            public void yidiensdfsdf() {
                Exit.exit(MainActivity_balance.this);
                Toast.makeText(MainActivity_balance.this, "您的账户已在异地登录", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MainActivity_balance.this, "网络连接中断", Toast.LENGTH_SHORT).show();
            }
        });
        request.setTag("abcGet_balance");
        MyApplication.getQueue().add(request);
    }

    public void volley_Get_Balance(final UserInfo userInfo1) {
        SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);
        String url = "http://www.louxiago.com/wc/ddkd/admin.php/Turnover/center/token/" + token;
        Log.i("Balance_url", url);
        StringRequest balance_request = new StringRequest(Request.Method.GET, url, new MyStringRequest() {
            @Override
            public void success(Object o) {
                String s = (String) o;
                if (!s.equals("ERROR")) {
                    Gson gson = new Gson();
                    UserInfo userInfo = gson.fromJson(s, UserInfo.class);
                    Log.i("Userinfo", userInfo + "");
                    if (userInfo != null) {
                        DecimalFormat decimalFormat = new DecimalFormat("0.00");
                        balance.setText(decimalFormat.format(Double.valueOf(userInfo.getBalance())));
                    }
                } else {
                    Toast.makeText(MainActivity_balance.this, "网络连接异常", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void tokenouttime() {
                Log.i("TOKEN_balance", "token outtime");
                AutologonUtil autologonUtil = new AutologonUtil(MainActivity_balance.this, handler1, userInfo1);
                autologonUtil.volley_Get_TOKEN();
            }

            @Override
            public void yidiensdfsdf() {
                Exit.exit(MainActivity_balance.this);
                Toast.makeText(MainActivity_balance.this, "您的账户已在异地登录", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MainActivity_balance.this, "网络连接中断", Toast.LENGTH_SHORT).show();
            }
        });
        balance_request.setTag("get_main");
        MyApplication.getQueue().add(balance_request);
    }

    class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return paymentlist.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            if (convertView == null) {//判断当前的缓存对象是否为空；
                LayoutInflater inflater = MainActivity_balance.this.getLayoutInflater();
                view = inflater.inflate(R.layout.balance_listview, null);
            } else {
                view = convertView;
            }
            //提现
            TextView moneyout = (TextView) view.findViewById(R.id.moneyout);
            TextView outStatic = (TextView) view.findViewById(R.id.outStatic);
            TextView counter = (TextView) view.findViewById(R.id.counter);
            TextView time = (TextView) view.findViewById(R.id.time1);
            //收入
            Payment payment = paymentlist.get(position);
            if (payment != null) {
                Log.i("Falg", payment.getFlag());
                if (payment.getFlag().equals("OUT")) {
                    if (payment.getStatus().equals("1")) {
                        outStatic.setText("审核中");
                    } else if (payment.getStatus().equals("2")) {
                        outStatic.setText("已通过");
                    } else {
                        outStatic.setText("操作失败");
                    }
                    moneyout.setText("-" + String.valueOf(payment.getMoney()));
                    counter.setText(payment.getCounter());
                } else {
                    SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
                    counter.setText(sharedPreferences.getString("phone", ""));
                    outStatic.setText("已到账");
                    moneyout.setText("+" + String.valueOf(payment.getMoney()));
                }
                time.setText(payment.getTime());
            } else {
                Log.i("ERROR", "payment的内容为空");
            }
            return view;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);
        Volley_Get(paymentlist);
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }

    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getQueue().cancelAll("get_main");
        MyApplication.getQueue().cancelAll("abcGet_balance");
    }
}
