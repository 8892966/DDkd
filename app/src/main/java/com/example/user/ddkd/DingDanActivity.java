package com.example.user.ddkd;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baidu.mobstat.StatService;
import com.example.user.ddkd.beam.OrderInfo;
import com.example.user.ddkd.utils.AutologonUtil;
import com.example.user.ddkd.utils.Exit;
import com.example.user.ddkd.utils.MyStringRequest;
import com.example.user.ddkd.utils.TimeUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DingDanActivity extends BaseActivity implements View.OnClickListener {
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

    private Handler handler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MyApplication.GET_TOKEN_SUCCESS:
                    String State = (String) msg.obj;
                    volley_getOrder_GET(State);
                    break;
                case MyApplication.GET_TOKEN_ERROR:
                    Toast.makeText(DingDanActivity.this, "网络连接出错", Toast.LENGTH_SHORT).show();
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
                    Object[] obj = (Object[]) msg.obj;
                    OrderInfo info = (OrderInfo) obj[0];
                    String State = (String) obj[1];
                    TextView button = (TextView) obj[2];
                    ProgressBar pb_button = (ProgressBar) obj[2];
                    volley_OrderState_GET(info, State, pb_button, button);
                    break;
                case MyApplication.GET_TOKEN_ERROR:

                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_details_activity);
        listView = (ListView) findViewById(R.id.lv_order_details);
        tv_button_yijie = (TextView) findViewById(R.id.tv_button_yijie);//查看已接单的订单详情
        tv_button_daisong = (TextView) findViewById(R.id.tv_button_daisong);//查看待送的订单详情
        tv_button_wangchen = (TextView) findViewById(R.id.tv_button_wangchen);//查看已完成的订单详情
        tv_button_quxiao = (TextView) findViewById(R.id.tv_button_quxiao);//查看以取消的订单详情
        tv_head_fanghui = (TextView) findViewById(R.id.tv_head_fanghui);
        rl_order_ProgressBar = (RelativeLayout) findViewById(R.id.rl_order_ProgressBar);//加载中显示的RelativeLayout
        //初始化选择页面
        int i = getIntent().getIntExtra("Static", 1);
        if (i == 3) {
            xuanzhe = 3;
            setEnableds(xuanzhe);
            //初始加载已接单页面
            volley_getOrder_GET("3");
        } else {
            xuanzhe = 1;
            setEnableds(xuanzhe);
            //初始加载已接单页面
            volley_getOrder_GET("1");
        }
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
        listView.getEmptyView().setVisibility(View.GONE);
        //先隐藏listview，等加载数据后再显示出来
        listView.setVisibility(View.GONE);
        ExitApplication.getInstance().addActivity(this);
        //初始化数据
//        volley_getOrder_GET("", "1", "http://www.louxiago.com/wc/ddkd/admin.php/Order/getOrder/state/0/uid/704");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_button_yijie://查看以接单
                xuanzhe = 1;
                setEnableds(xuanzhe);
                volley_getOrder_GET("1");
                listView.getEmptyView().setVisibility(View.GONE);//跟新后显示
                listView.setVisibility(View.GONE);//更新后再显示
                rl_order_ProgressBar.setVisibility(View.VISIBLE);//显示加载页面
                break;
            case R.id.tv_button_daisong://查看待送单
                xuanzhe = 2;
                setEnableds(xuanzhe);
                volley_getOrder_GET("2");
                listView.getEmptyView().setVisibility(View.GONE);//跟新后显示
                listView.setVisibility(View.GONE);//更新后再显示
                rl_order_ProgressBar.setVisibility(View.VISIBLE);//显示加载页面
                break;
            case R.id.tv_button_wangchen://查看完成订单
                xuanzhe = 3;
                setEnableds(xuanzhe);
                volley_getOrder_GET("3");
                listView.getEmptyView().setVisibility(View.GONE);//跟新后显示
                listView.setVisibility(View.GONE);//更新后再显示
                rl_order_ProgressBar.setVisibility(View.VISIBLE);//显示加载页面
                break;
            case R.id.tv_button_quxiao://查看取消的订单
                xuanzhe = 4;
                setEnableds(xuanzhe);
                volley_getOrder_GET("4");
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
            try {
                View view;
                ZhuanTai zhuanTai;
                if (convertView != null) {
                    view = convertView;
                    zhuanTai = (ZhuanTai) view.getTag();
                } else {
                    zhuanTai = new ZhuanTai();
                    view = View.inflate(DingDanActivity.this, R.layout.dingdan_item, null);

                    zhuanTai.button = (TextView) view.findViewById(R.id.tv_dingdang_tuidang);
                    //订单的id
                    zhuanTai.tv_dingdang_id = (TextView) view.findViewById(R.id.tv_dingdang_id);
                    //可以赚到的钱
                    zhuanTai.tv_money = (TextView) view.findViewById(R.id.tv_money);
                    //拿快递地址
                    zhuanTai.tv_kuaidi_dizhi = (TextView) view.findViewById(R.id.tv_kuaidi_dizhi);
                    //快递公司
                    zhuanTai.tv_dingdang_kuaidi = (TextView) view.findViewById(R.id.tv_dingdang_kuaidi);
                    //接收人和电话号码
                    zhuanTai.lxr = (TextView) view.findViewById(R.id.lxr);
                    //快递联系人和电话
                    zhuanTai.dh = (TextView) view.findViewById(R.id.dh);
                    //留言
                    zhuanTai.ly = (TextView) view.findViewById(R.id.ly);
                    //下单的时间
                    zhuanTai.tv_dingdang_shijain = (TextView) view.findViewById(R.id.tv_dingdang_shijain);
                    //打客户的电话
                    zhuanTai.iv_call_phone = (ImageView) view.findViewById(R.id.iv_call_phone);
                    //ProgressBar,点击改变状态时出现
                    zhuanTai.pb_button = (ProgressBar) view.findViewById(R.id.pb_button);
                    //退单理由
                    zhuanTai.tuidan = (TextView) view.findViewById(R.id.tuidan);
                    //付款状态
                    zhuanTai.zhuangtai = (TextView) view.findViewById(R.id.zhuangtai);

                    zhuanTai.fahuodizhi = (TextView) view.findViewById(R.id.fahuodizhi);

                    zhuanTai.ll9 = (LinearLayout) view.findViewById(R.id.ll9);

                    zhuanTai.ll8 = (LinearLayout) view.findViewById(R.id.ll8);
                    zhuanTai.ll_xinxin=(LinearLayout) view.findViewById(R.id.ll_xinxin);
                    zhuanTai.xx1=(ImageView)view.findViewById(R.id.xx1);
                    zhuanTai.xx2=(ImageView)view.findViewById(R.id.xx2);
                    zhuanTai.xx3=(ImageView)view.findViewById(R.id.xx3);
                    zhuanTai.xx4=(ImageView)view.findViewById(R.id.xx4);
                    zhuanTai.xx5=(ImageView)view.findViewById(R.id.xx5);
                    view.setTag(zhuanTai);
                }
                OrderInfo info = list.get(position);
                Log.e("MyBaseAdapter", info.toString());
                zhuanTai.iv_call_phone.setVisibility(View.VISIBLE);
                zhuanTai.ll_xinxin.setVisibility(View.GONE);
                if (xuanzhe == 1) {
                    zhuanTai.button.setEnabled(true);
                    zhuanTai.button.setText("已拿件");
                    zhuanTai.button.setVisibility(View.VISIBLE);
                    if (!info.getPid().equals("0")) {
                        zhuanTai.zhuangtai.setText("已付款");
                    } else {
                        zhuanTai.zhuangtai.setText("未付款");
                    }
                    zhuanTai.ll8.setVisibility(View.GONE);
                    zhuanTai.ll9.setVisibility(View.VISIBLE);
                } else if (xuanzhe == 2) {
                    zhuanTai.ll8.setVisibility(View.GONE);
                    zhuanTai.ll9.setVisibility(View.GONE);
                    if (!info.getPid().equals("0")) {
                        zhuanTai.button.setText("完成");
                        zhuanTai.button.setEnabled(true);
                    } else {
                        zhuanTai.button.setText("未付款");
                        zhuanTai.button.setEnabled(false);
                    }
                    zhuanTai.button.setVisibility(View.VISIBLE);
                } else if (xuanzhe == 3) {
                    xingxing(Float.valueOf(info.getEvaluate()),zhuanTai.xx1,zhuanTai.xx2,zhuanTai.xx3,zhuanTai.xx4,zhuanTai.xx5);
                    zhuanTai.ll_xinxin.setVisibility(View.VISIBLE);
                    zhuanTai.iv_call_phone.setVisibility(View.GONE);
                    zhuanTai.ll8.setVisibility(View.GONE);
                    zhuanTai.ll9.setVisibility(View.GONE);
                    zhuanTai.button.setVisibility(View.GONE);
                } else if (xuanzhe == 4) {
                    if (!info.getPid().equals("0")) {
                        zhuanTai.zhuangtai.setText("已付款");
                    } else {
                        zhuanTai.zhuangtai.setText("未付款");
                    }
                    zhuanTai.ll8.setVisibility(View.VISIBLE);
                    zhuanTai.ll9.setVisibility(View.VISIBLE);
                    zhuanTai.button.setVisibility(View.GONE);
                }
                String[] s = info.getReceivePlace().split("/", -2);
                int i = s.length;
                String diz;
                if (3 <= i) {
                    diz = s[3].replace(',', ' ');
                } else {
                    diz = "";
                }
                if (info.getTip() != null) {
                    zhuanTai.tv_dingdang_id.setText("订单：" + info.getId());
                    if (info.getTip().equals("0")) {
                        zhuanTai.tv_money.setText(info.getPrice() + "元");
                    } else {
                        zhuanTai.tv_money.setText(info.getPrice() + "元" + "+小费" + info.getTip() + "元");
                    }
                } else {
                    zhuanTai.tv_money.setText("0元");
                }
                zhuanTai.tv_kuaidi_dizhi.setText(info.getAddressee() + "");
                zhuanTai.tv_dingdang_kuaidi.setText(info.getExpressCompany() + "");
                if (0 <= i && i <= i) {
                    zhuanTai.lxr.setText(s[0] + "/" + s[1] + "/" + s[2]);
                } else {
                    zhuanTai.lxr.setText("");
                }
                zhuanTai.dh.setText(info.getUsername() + "/" + info.getPhone());
                zhuanTai.fahuodizhi.setText(diz + "");
                zhuanTai.ly.setText("留言:" + info.getMessage());
                zhuanTai.tuidan.setText("退单理由:" + info.getReason());
                zhuanTai.tv_dingdang_shijain.setText(info.getOrderTime() + "");
                zhuanTai.iv_call_phone.setOnClickListener(new MyOnClickListener(info, null, null));
                zhuanTai.button.setOnClickListener(new MyOnClickListener(info, zhuanTai.pb_button, zhuanTai.button));
                return view;
            } catch (Exception e) {
                Log.e("Exception", e.getMessage());
                Toast.makeText(DingDanActivity.this, "信息有误", Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        //按钮的监听
        class MyOnClickListener implements View.OnClickListener {
            OrderInfo info;
            ProgressBar pb_button;
            TextView button;
            AlertDialog alertDialog;

            /**
             * 输入信息
             *
             * @param info
             */
            public MyOnClickListener(OrderInfo info, ProgressBar pb_button, TextView button) {
                this.info = info;
                this.pb_button = pb_button;
                this.button = button;
            }

            @Override
            public void onClick(View v) {
                try {
                    switch (v.getId()) {
                        case R.id.iv_call_phone://打电话
                            AlertDialog.Builder builder = new AlertDialog.Builder(DingDanActivity.this);
                            View inflate = View.inflate(DingDanActivity.this, R.layout.call_phone_dialog, null);
                            TextView shouhuo_call_short_phone = (TextView) inflate.findViewById(R.id.shouhuo_call_short_phone);
                            TextView shouhuo_call_long_phone = (TextView) inflate.findViewById(R.id.shouhuo_call_long_phone);
                            TextView xiadan_call_long_phone = (TextView) inflate.findViewById(R.id.xiadan_call_long_phone);
                            final String[] s = info.getReceivePlace().split("/", -2);
                            if (s.length >= 2) {
                                if (s[2] != null && !s[2].equals("")) {
                                    shouhuo_call_short_phone.setText("收货人短号：" + s[2]);
                                    shouhuo_call_short_phone.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + s[2]));
                                            startActivity(intent);
                                            alertDialog.dismiss();
                                        }
                                    });
                                } else {
                                    shouhuo_call_short_phone.setVisibility(View.GONE);
                                }
                            }
                            if (s.length >= 1) {
                                if (s[1] != null && !s[1].equals("")) {
                                    shouhuo_call_long_phone.setText("收货人长号：" + s[1]);
                                    shouhuo_call_long_phone.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + s[1]));
                                            startActivity(intent);
                                            alertDialog.dismiss();
                                        }
                                    });
                                } else {
                                    shouhuo_call_long_phone.setVisibility(View.GONE);
                                }
                            }
                            if (info.getPhone() != null && !info.getPhone().equals("")) {
                                xiadan_call_long_phone.setText("下单人长号：" + info.getPhone());
                                xiadan_call_long_phone.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + info.getPhone()));
                                        startActivity(intent);
                                        alertDialog.dismiss();
                                    }
                                });
                            } else {
                                xiadan_call_long_phone.setVisibility(View.GONE);
                            }

                            builder.setView(inflate, 0, 0, 0, 0);
                            builder.setNegativeButton("取消", null);
                            alertDialog = builder.create();
                            alertDialog.show();
//                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + info.getPhone()));
//                        startActivity(intent);
                            break;
                        case R.id.tv_dingdang_tuidang://已拿件按钮事件
                            if (xuanzhe == 2) {
                                button.setEnabled(false);
                                pb_button.setVisibility(View.VISIBLE);
                                volley_OrderState_GET(info, "3", pb_button, button);
                            }
                            if (xuanzhe == 1) {
                                button.setEnabled(false);
                                pb_button.setVisibility(View.VISIBLE);
                                volley_OrderState_GET(info, "2", pb_button, button);
                            }
                            break;
                    }
                }catch (Exception e){
                    Log.e("Exception", e.getMessage()+"");
                    Toast.makeText(DingDanActivity.this, "信息有误", Toast.LENGTH_SHORT).show();
                }
            }
        }

        class ZhuanTai {
            TextView tv_dingdang_id;//订单号
            TextView tv_money;//钱
            ImageView iv_call_phone;//打电话
            TextView tv_kuaidi_dizhi;//拿快递地址
            TextView tv_dingdang_kuaidi;//快递公司
            TextView lxr;//接收人和电话号码
            TextView dh;//快递联系人和电话
            TextView ly;//留言
            TextView tv_dingdang_shijain;//时间
            TextView button;//按钮
            ProgressBar pb_button;//等待
            TextView tuidan;//退单理由
            TextView zhuangtai;//付款状态
            TextView fahuodizhi;//目的地址
            LinearLayout ll9;//状态栏
            LinearLayout ll8;//退单栏
            //星星
            LinearLayout ll_xinxin;
            ImageView xx1;
            ImageView xx2;
            ImageView xx3;
            ImageView xx4;
            ImageView xx5;
        }
    }

    //网络申请得到相应状态的订单列表
    private void volley_getOrder_GET(final String State) {
        MyApplication.getQueue().cancelAll("volley_getOrder_GET");
        preferences = getSharedPreferences("config", MODE_PRIVATE);
        String token = preferences.getString("token", "");
//        Log.e("volley_getOrder_GET", token);
        String url = "http://www.louxiago.com/wc/ddkd/admin.php/Order/getOrder/state/" + State + "/token/" + token;
        StringRequest request_post = new StringRequest(Request.Method.GET, url, new MyStringRequest() {
            @Override
            public void success(Object o) {
                String s = (String) o;
                try {
//                    Log.e("volley_getOrder_GET", s);
                    if (!"ERROR".equals(s)) {
                        Gson gson = new Gson();
                        list = gson.fromJson((String) o, new TypeToken<List<OrderInfo>>() {
                        }.getType());
                        //转化时间戳
                        for (OrderInfo info : list) {
                            info.setTime(TimeUtil.getStrTime(info.getTime()));
                            info.setOrderTime(TimeUtil.getStrTime(info.getOrderTime()));
//                        info.setTime(format.format(Long.valueOf(info.getTime())));
                        }
                    } else {
                        list.clear();
                    }
                    //更新日期
                    baseAdapter.notifyDataSetChanged();
                    listView.setVisibility(View.VISIBLE);//显示数据
                    rl_order_ProgressBar.setVisibility(View.GONE);//隐藏加载页面
                } catch (Exception e) {
                    Log.e("Exception", e.getMessage()+"");
                    Toast.makeText(DingDanActivity.this, "信息有误", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void tokenouttime() {
//                Log.e("volley_getOrder_GET", "token过时了");
                AutologonUtil autologonUtil = new AutologonUtil(DingDanActivity.this, handler1, State);
                autologonUtil.volley_Get_TOKEN();
            }

            @Override
            public void yidiensdfsdf() {
                Exit.exit(DingDanActivity.this);
                Toast.makeText(DingDanActivity.this, "您的账户已在异地登录", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                baseAdapter.notifyDataSetChanged();
                listView.setVisibility(View.VISIBLE);//显示数据
                rl_order_ProgressBar.setVisibility(View.GONE);//隐藏加载页面
            }
        });
//        StringRequest request_post = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String s) {
//                Log.e("volley_getOrder_GET", s);
//                if (!s.equals("\"token outtime\"")) {//error"token outtime"
//                    if (!s.equals("\"ERROR\"")) {
//                        Gson gson = new Gson();
//                        list = gson.fromJson(s, new TypeToken<List<OrderInfo>>() {
//                        }.getType());
//                        //转化时间戳
//                        SimpleDateFormat format = new SimpleDateFormat("MM月dd日 HH:mm");
//                        for (OrderInfo info : list) {
//                            info.setTime(format.format(Long.valueOf(info.getTime())));
//                        }
//                    } else {
//                        list.clear();
//                    }
//                    //更新日期
//                    baseAdapter.notifyDataSetChanged();
//                    listView.setVisibility(View.VISIBLE);//显示数据
//                    rl_order_ProgressBar.setVisibility(View.GONE);//隐藏加载页面
//                } else {
//                    Log.e("volley_getOrder_GET", "token过时了");
//                    AutologonUtil autologonUtil = new AutologonUtil(DingDanActivity.this, handler1, State);
//                    autologonUtil.volley_Get_TOKEN();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                baseAdapter.notifyDataSetChanged();
//                listView.setVisibility(View.VISIBLE);//显示数据
//                rl_order_ProgressBar.setVisibility(View.GONE);//隐藏加载页面
//            }
//        });
        request_post.setTag("volley_getOrder_GET");
        MyApplication.getQueue().add(request_post);
    }

    //网络申请修改相应状态的订单列表
    private void volley_OrderState_GET(final OrderInfo info, final String State, final ProgressBar pb_button, final TextView button) {
        preferences = getSharedPreferences("config", MODE_PRIVATE);
        String token = preferences.getString("token", "");
        String url = "http://www.louxiago.com/wc/ddkd/admin.php/Order/setOrderState/id/" + info.getId() + "/state/" + State + "/token/" + token;
//        参数一：方法 参数二：地址 参数三：成功回调 参数四：错误回调 。重写getParams 以post参数
        StringRequest request_post = new StringRequest(Request.Method.GET, url, new MyStringRequest() {

            @Override
            public void success(Object o) {
                String s = (String) o;
                try {
                    if ("SUCCESS".equals(s)) {
                        button.setEnabled(true);
                        pb_button.setVisibility(View.GONE);
                        list.remove(info);
                        baseAdapter.notifyDataSetChanged();
                    } else {
                        button.setEnabled(true);
                        pb_button.setVisibility(View.GONE);
                        Toast.makeText(DingDanActivity.this, "网络连接错...", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("Exception", e.getMessage());
                    Toast.makeText(DingDanActivity.this, "信息有误", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void tokenouttime() {
                Log.e("volley_getOrder_GET", "token过时了");
                Object[] obj = {info, State, pb_button, button};
                AutologonUtil autologonUtil = new AutologonUtil(DingDanActivity.this, handler2, obj);
                autologonUtil.volley_Get_TOKEN();
            }

            @Override
            public void yidiensdfsdf() {
                Exit.exit(DingDanActivity.this);
                Toast.makeText(DingDanActivity.this, "您的账户已在异地登录", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                button.setEnabled(true);
                pb_button.setVisibility(View.GONE);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getQueue().cancelAll("volley_OrderState_GET");
        MyApplication.getQueue().cancelAll("volley_getOrder_GET");
    }

    private void setEnableds(int i) {
        try {
            switch (i) {
                case 1:
                    tv_button_yijie.setTextColor(getResources().getColor(R.color.head));
                    tv_button_daisong.setTextColor(Color.BLACK);
                    tv_button_wangchen.setTextColor(Color.BLACK);
                    tv_button_quxiao.setTextColor(Color.BLACK);
                    break;
                case 2:
                    tv_button_daisong.setTextColor(getResources().getColor(R.color.head));
                    tv_button_yijie.setTextColor(Color.BLACK);
                    tv_button_wangchen.setTextColor(Color.BLACK);
                    tv_button_quxiao.setTextColor(Color.BLACK);
                    break;
                case 3:
                    tv_button_wangchen.setTextColor(getResources().getColor(R.color.head));
                    tv_button_daisong.setTextColor(Color.BLACK);
                    tv_button_yijie.setTextColor(Color.BLACK);
                    tv_button_quxiao.setTextColor(Color.BLACK);
                    break;
                case 4:
                    tv_button_quxiao.setTextColor(getResources().getColor(R.color.head));
                    tv_button_daisong.setTextColor(Color.BLACK);
                    tv_button_wangchen.setTextColor(Color.BLACK);
                    tv_button_yijie.setTextColor(Color.BLACK);
                    break;
            }
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
            Toast.makeText(DingDanActivity.this, "信息有误", Toast.LENGTH_SHORT).show();
        }
    }

    private void xingxing(float i,ImageView xx1,
            ImageView xx2,
            ImageView xx3,
            ImageView xx4,
            ImageView xx5){
        try {
            int ii=(int)(i+0.5);
            switch (ii) {
                case 0:
                    xx1.setImageResource(R.drawable.comment_star_gray_icon);
                    xx2.setImageResource(R.drawable.comment_star_gray_icon);
                    xx3.setImageResource(R.drawable.comment_star_gray_icon);
                    xx4.setImageResource(R.drawable.comment_star_gray_icon);
                    xx5.setImageResource(R.drawable.comment_star_gray_icon);
                    break;
                case 1:
                    xx1.setImageResource(R.drawable.comment_star_light_icon);
                    xx2.setImageResource(R.drawable.comment_star_gray_icon);
                    xx3.setImageResource(R.drawable.comment_star_gray_icon);
                    xx4.setImageResource(R.drawable.comment_star_gray_icon);
                    xx5.setImageResource(R.drawable.comment_star_gray_icon);
                    break;
                case 2:
                    xx1.setImageResource(R.drawable.comment_star_light_icon);
                    xx2.setImageResource(R.drawable.comment_star_light_icon);
                    xx3.setImageResource(R.drawable.comment_star_gray_icon);
                    xx4.setImageResource(R.drawable.comment_star_gray_icon);
                    xx5.setImageResource(R.drawable.comment_star_gray_icon);
                    break;
                case 3:
                    xx1.setImageResource(R.drawable.comment_star_light_icon);
                    xx2.setImageResource(R.drawable.comment_star_light_icon);
                    xx3.setImageResource(R.drawable.comment_star_light_icon);
                    xx4.setImageResource(R.drawable.comment_star_gray_icon);
                    xx5.setImageResource(R.drawable.comment_star_gray_icon);
                    break;
                case 4:
                    xx1.setImageResource(R.drawable.comment_star_light_icon);
                    xx2.setImageResource(R.drawable.comment_star_light_icon);
                    xx3.setImageResource(R.drawable.comment_star_light_icon);
                    xx4.setImageResource(R.drawable.comment_star_light_icon);
                    xx5.setImageResource(R.drawable.comment_star_gray_icon);
                    break;
                case 5:
                    xx1.setImageResource(R.drawable.comment_star_light_icon);
                    xx2.setImageResource(R.drawable.comment_star_light_icon);
                    xx3.setImageResource(R.drawable.comment_star_light_icon);
                    xx4.setImageResource(R.drawable.comment_star_light_icon);
                    xx5.setImageResource(R.drawable.comment_star_light_icon);
                    break;
            }
        }catch (Exception e){
            Log.e("Exception", e.getMessage());
            Toast.makeText(DingDanActivity.this,"信息有误",Toast.LENGTH_SHORT).show();
        }
    }
}
