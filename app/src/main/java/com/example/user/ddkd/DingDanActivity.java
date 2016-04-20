package com.example.user.ddkd;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.user.ddkd.beam.OrderInfo;
import com.example.user.ddkd.utils.AutologonUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.stat.StatService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DingDanActivity extends Activity implements View.OnClickListener {
    //放订单列表
    private ListView listView;
    private MyBaseAdapter baseAdapter;
    private TextView tv_button_yijie;
    private TextView tv_button_daisong;
    private TextView tv_button_wangchen;
    private TextView tv_button_quxiao;
    private TextView tv_head_fanghui;
    private List<OrderInfo> list;
    private RelativeLayout rl_order_ProgressBar;

    private int xuanzhe;
    private SharedPreferences preferences;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MyApplication.GET_TOKEN_SUCCESS:

                    break;
                case MyApplication.GET_TOKEN_ERROR:

                    break;
            }
        }
    };
    private int i;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_details_activity);
        //初始加载已接单页面
        volley_getOrder_GET("1");

        listView = (ListView) findViewById(R.id.lv_order_details);
        tv_button_yijie = (TextView) findViewById(R.id.tv_button_yijie);//查看已接单的订单详情
        tv_button_daisong = (TextView) findViewById(R.id.tv_button_daisong);//查看待送的订单详情
        tv_button_wangchen = (TextView) findViewById(R.id.tv_button_wangchen);//查看已完成的订单详情
        tv_button_quxiao = (TextView) findViewById(R.id.tv_button_quxiao);//查看以取消的订单详情
        tv_head_fanghui = (TextView) findViewById(R.id.tv_head_fanghui);
        rl_order_ProgressBar = (RelativeLayout) findViewById(R.id.rl_order_ProgressBar);//加载中显示的RelativeLayout
        //初始化选择页面
        xuanzhe = 1;
        //初始化list
        list = new ArrayList<OrderInfo>();

        tv_button_yijie.setOnClickListener(this);
        tv_button_daisong.setOnClickListener(this);
        tv_button_wangchen.setOnClickListener(this);
        tv_button_quxiao.setOnClickListener(this);
        tv_head_fanghui.setOnClickListener(this);
        baseAdapter = new MyBaseAdapter();
        listView.setAdapter(baseAdapter);
        listView.setEmptyView(findViewById(R.id.tv_default));
        //先隐藏listview，等加载数据后再显示出来
        listView.setVisibility(View.GONE);
        //初始化数据
//        volley_getOrder_GET("", "1", "http://www.louxiago.com/wc/ddkd/admin.php/Order/getOrder/state/0/uid/704");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_button_yijie://查看以接单
                xuanzhe = 1;
                volley_getOrder_GET("1");
                listView.getEmptyView().setVisibility(View.GONE);//跟新后显示
                listView.setVisibility(View.GONE);//更新后再显示
                rl_order_ProgressBar.setVisibility(View.VISIBLE);//显示加载页面
                break;
            case R.id.tv_button_daisong://查看待送单
                xuanzhe = 2;
                volley_getOrder_GET("2");
                listView.getEmptyView().setVisibility(View.GONE);//跟新后显示
                listView.setVisibility(View.GONE);//更新后再显示
                rl_order_ProgressBar.setVisibility(View.VISIBLE);//显示加载页面
                break;
            case R.id.tv_button_wangchen://查看完成订单
                xuanzhe = 3;
                volley_getOrder_GET("3");
                listView.getEmptyView().setVisibility(View.GONE);//跟新后显示
                listView.setVisibility(View.GONE);//更新后再显示
                rl_order_ProgressBar.setVisibility(View.VISIBLE);//显示加载页面
                break;
            case R.id.tv_button_quxiao://查看取消的订单
                xuanzhe = 4;
                volley_getOrder_GET("5");
                listView.getEmptyView().setVisibility(View.GONE);//跟新后显示
                listView.setVisibility(View.GONE);//更新后再显示
                rl_order_ProgressBar.setVisibility(View.VISIBLE);//显示加载页面
                break;
            case R.id.tv_head_fanghui:
                finish();
                break;
        }
    }

    class MyBaseAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
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
            ZhuanTai zhuanTai;
            if (convertView != null) {
                view = convertView;
                zhuanTai = (ZhuanTai) view.getTag();
            } else {
                zhuanTai = new ZhuanTai();
                view = View.inflate(DingDanActivity.this, R.layout.dingdan_item, null);
                //已拿件完成的按钮
                zhuanTai.textbutton = (TextView) view.findViewById(R.id.tv_dingdang_yina);
                //退单的按钮
                zhuanTai.button = (TextView) view.findViewById(R.id.tv_dingdang_tuidang);
                //订单的id
                zhuanTai.tv_dingdang_id = (TextView) view.findViewById(R.id.tv_dingdang_id);
                //可以赚到的钱
                zhuanTai.tv_money = (TextView) view.findViewById(R.id.tv_money);
                //快递的地址和所属的快递公司
                zhuanTai.tv_dingdang_kuaidi_dizhi = (TextView) view.findViewById(R.id.tv_dingdang_kuaidi_dizhi);
                //客户名字和电话号码
                zhuanTai.tv_dingdang_kehu = (TextView) view.findViewById(R.id.tv_dingdang_kehu);
                //目的地的地址
                zhuanTai.tv_dingdang_nudi_dizhi = (TextView) view.findViewById(R.id.tv_dingdang_nudi_dizhi);
                //客户的留言
                zhuanTai.tv_dingdang_liuyan = (TextView) view.findViewById(R.id.tv_dingdang_liuyan);
                //下单的时间
                zhuanTai.tv_dingdang_shijain = (TextView) view.findViewById(R.id.tv_dingdang_shijain);
                //打客户的电话
                zhuanTai.iv_call_phone = (ImageView) view.findViewById(R.id.iv_call_phone);
//                //退单的理由
//                zhuanTai.tv_dingdang_liyou = (TextView) view.findViewById(R.id.tv_dingdang_liyou);
                view.setTag(zhuanTai);
            }
            OrderInfo info = list.get(position);
            Log.e("MyBaseAdapter", info.toString());
            if (xuanzhe == 1) {
//                zhuanTai.tv_dingdang_liyou.setVisibility(View.GONE);
                zhuanTai.button.setText("退出");
                zhuanTai.textbutton.setVisibility(View.VISIBLE);
                zhuanTai.button.setVisibility(View.VISIBLE);
            } else if (xuanzhe == 2) {
//                zhuanTai.tv_dingdang_liyou.setVisibility(View.GONE);
                zhuanTai.button.setText("完成");
                zhuanTai.textbutton.setVisibility(View.GONE);
                zhuanTai.button.setVisibility(View.VISIBLE);
            } else if (xuanzhe == 3) {
//                zhuanTai.tv_dingdang_liyou.setVisibility(View.GONE);
                zhuanTai.textbutton.setVisibility(View.GONE);
                zhuanTai.button.setVisibility(View.GONE);
            } else if (xuanzhe == 4) {
//                zhuanTai.tv_dingdang_liyou.setVisibility(View.VISIBLE);
                zhuanTai.textbutton.setVisibility(View.GONE);
                zhuanTai.button.setVisibility(View.GONE);
//                zhuanTai.tv_dingdang_liyou.setText();
            }
            zhuanTai.tv_dingdang_id.setText("订单：" + info.getId());
            zhuanTai.tv_money.setText(info.getPrice() + "元");
            zhuanTai.tv_dingdang_kehu.setText("   " + info.getUsername() + "   " + info.getPhone());
            zhuanTai.tv_dingdang_kuaidi_dizhi.setText("   " + info.getAddressee() + "   " + info.getExpressCompany() + "快递");
            zhuanTai.tv_dingdang_liuyan.setText("留言：" + info.getEvaluate());
            zhuanTai.tv_dingdang_shijain.setText(info.getTime() + "");
            zhuanTai.tv_dingdang_nudi_dizhi.setText("   " + info.getReceivePlace());
            zhuanTai.iv_call_phone.setOnClickListener(new MyOnClickListener(info));
            zhuanTai.textbutton.setOnClickListener(new MyOnClickListener(info));
            zhuanTai.button.setOnClickListener(new MyOnClickListener(info));
            return view;
        }

        //按钮的监听
        class MyOnClickListener implements View.OnClickListener {
            OrderInfo info;

            /**
             * 输入信息
             *
             * @param info
             */
            public MyOnClickListener(OrderInfo info) {
                this.info = info;
            }
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.iv_call_phone://打电话
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + info.getPhone()));
                        //noinspection ResourceType
                        startActivity(intent);
                        break;
                    case R.id.tv_dingdang_yina://已拿件按钮事件
                        volley_OrderState_GET(info, "2");
                        break;
                    case R.id.tv_dingdang_tuidang:
                        if (xuanzhe == 2) {
                            volley_OrderState_GET(info, "3");
                        }
                        if (xuanzhe == 1) {
                            volley_OrderState_GET(info, "4");
                        }
                        break;
                }
            }
        }

        class ZhuanTai {
            TextView textbutton;
            TextView button;
            TextView tv_dingdang_id;
            TextView tv_money;
            TextView tv_dingdang_kuaidi_dizhi;
            TextView tv_dingdang_kehu;
            TextView tv_dingdang_nudi_dizhi;
            TextView tv_dingdang_liuyan;
            TextView tv_dingdang_shijain;
            //            TextView tv_dingdang_liyou;
            ImageView iv_call_phone;
        }
    }

    //网络申请得到相应状态的订单列表
    private void volley_getOrder_GET(final String State) {
        MyApplication.getQueue().cancelAll("volley_getOrder_GET");
        preferences = getSharedPreferences("config", MODE_PRIVATE);
        String token = preferences.getString("token", "");
        Log.e("volley_getOrder_GET", token);
        String url = "http://www.louxiago.com/wc/ddkd/admin.php/Order/getOrder/state/" + State + "/token/" + token;
//        参数一：方法 参数二：地址 参数三：成功回调 参数四：错误回调 。重写getParams 以post参数
        StringRequest request_post = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("volley_getOrder_GET", s);
                if (!s.endsWith("\"token outtime\"")) {
                    if (!s.equals("\"ERROR\"")) {
                        Gson gson = new Gson();
                        list = gson.fromJson(s, new TypeToken<List<OrderInfo>>() {
                        }.getType());
                        //转化时间戳
                        SimpleDateFormat format = new SimpleDateFormat("MM月dd日 HH:mm");
                        for (OrderInfo info : list) {
                            info.setTime(format.format(Long.valueOf(info.getTime())));
                        }

                    } else {
                        list.clear();
                    }
                    //更新日期
                    baseAdapter.notifyDataSetChanged();
                    listView.setVisibility(View.VISIBLE);//显示数据
                    rl_order_ProgressBar.setVisibility(View.GONE);//隐藏加载页面
                } else {
                    Log.e("volley_getOrder_GET", "token过时了");
                    AutologonUtil autologonUtil = new AutologonUtil(DingDanActivity.this, handler);
                    autologonUtil.volley_Get_TOKEN();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                baseAdapter.notifyDataSetChanged();
                listView.setVisibility(View.VISIBLE);//显示数据
                rl_order_ProgressBar.setVisibility(View.GONE);//隐藏加载页面
            }
        });
        request_post.setTag("volley_getOrder_GET");
        MyApplication.getQueue().add(request_post);
    }
    //网络申请修改相应状态的订单列表
    private void volley_OrderState_GET(final OrderInfo info, final String State) {
        preferences = getSharedPreferences("config", MODE_PRIVATE);
        String token = preferences.getString("token", "");
        String url = "http://www.louxiago.com/wc/ddkd/admin.php/Order/setOrderState/id/" + info.getId() + "/state/" + State + "/token/" + token;
//        参数一：方法 参数二：地址 参数三：成功回调 参数四：错误回调 。重写getParams 以post参数
        StringRequest request_post = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("volley_OrderState_GET", s);
                if (!s.endsWith("\"token outtime\"")) {
                    if ("SUCCESS".equals(s)) {
                        list.remove(info);
                        baseAdapter.notifyDataSetChanged();
                    } else {

                    }
                } else {

                }
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                TextView textView = (TextView) listView.getEmptyView();
                textView.setText("网络连接中断...");
                list.clear();
                baseAdapter.notifyDataSetChanged();
            }
        });
        request_post.setTag("volley_OrderState_GET");
        MyApplication.getQueue().add(request_post);
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
