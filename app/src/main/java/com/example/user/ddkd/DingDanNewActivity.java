package com.example.user.ddkd;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
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
import com.example.user.ddkd.UI.MyDingDanView;
import com.example.user.ddkd.beam.OrderInfo;
import com.example.user.ddkd.utils.MyStringRequest;
import com.example.user.ddkd.utils.TimeUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class DingDanNewActivity extends Activity implements View.OnClickListener {
    private List<OrderInfo> list1;
    private List<OrderInfo> list2;
    private List<OrderInfo> list3;
    private List<OrderInfo> list4;
    private MyDingDanView myDingDanView;
    private TextView tv_button_yijie;
    private TextView tv_button_daisong;
    private TextView tv_button_wangchen;
    private TextView tv_button_quxiao;
    private int xuanzhe;
    private RelativeLayout relativeLayout1;
    private MyAdapter myAdapter1;
    private RelativeLayout relativeLayout2;
    private MyAdapter myAdapter2;
    private RelativeLayout relativeLayout3;
    private MyAdapter myAdapter3;
    private RelativeLayout relativeLayout4;
    private MyAdapter myAdapter4;
    private ListView addView1;
    private ListView addView2;
    private ListView addView3;
    private ListView addView4;
    private TextView tv_head_fanghui;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ding_dan);
        list1=new ArrayList<>();
        list2=new ArrayList<>();
        list3=new ArrayList<>();
        list4=new ArrayList<>();

        myDingDanView= (MyDingDanView) findViewById(R.id.dingdanview);
        tv_button_yijie = (TextView) findViewById(R.id.tv_button_yijie);//查看已接单的订单详情
        tv_button_daisong = (TextView) findViewById(R.id.tv_button_daisong);//查看待送的订单详情
        tv_button_wangchen = (TextView) findViewById(R.id.tv_button_wangchen);//查看已完成的订单详情
        tv_button_quxiao = (TextView) findViewById(R.id.tv_button_quxiao);//查看以取消的订单详情
        tv_head_fanghui = (TextView) findViewById(R.id.tv_head_fanghui);//返回键
        tv_button_yijie.setOnClickListener(this);
        tv_button_daisong.setOnClickListener(this);
        tv_button_wangchen.setOnClickListener(this);
        tv_button_quxiao.setOnClickListener(this);
        tv_head_fanghui.setOnClickListener(this);

        View view1=View.inflate(this,R.layout.dingdan_activity,null);
        addView1 = (ListView) view1.findViewById(R.id.lv_order_details);
        relativeLayout1 = (RelativeLayout) view1.findViewById(R.id.rl_order_ProgressBar);
        addView1.setEmptyView(view1.findViewById(R.id.tv_default));
        myAdapter1 = new MyAdapter(list1);
        addView1.setAdapter(myAdapter1);
        myDingDanView.addView(view1);

        View view2=View.inflate(this,R.layout.dingdan_activity,null);
        addView2 = (ListView) view2.findViewById(R.id.lv_order_details);
        relativeLayout2 = (RelativeLayout) view2.findViewById(R.id.rl_order_ProgressBar);
        addView2.setEmptyView(view2.findViewById(R.id.tv_default));
        myAdapter2 = new MyAdapter(list2);
        addView2.setAdapter(myAdapter2);
        myDingDanView.addView(view2);

        View view3=View.inflate(this,R.layout.dingdan_activity,null);
        addView3 = (ListView) view3.findViewById(R.id.lv_order_details);
        relativeLayout3 = (RelativeLayout) view3.findViewById(R.id.rl_order_ProgressBar);
        addView3.setEmptyView(view3.findViewById(R.id.tv_default));
        myAdapter3 = new MyAdapter(list3);
        addView3.setAdapter(myAdapter3);
        myDingDanView.addView(view3);

        View view4=View.inflate(this,R.layout.dingdan_activity,null);
        addView4 = (ListView) view4.findViewById(R.id.lv_order_details);
        relativeLayout4 = (RelativeLayout) view4.findViewById(R.id.rl_order_ProgressBar);
        addView4.setEmptyView(view4.findViewById(R.id.tv_default));
        myAdapter4 = new MyAdapter(list4);
        addView4.setAdapter(myAdapter4);
        myDingDanView.addView(view4);

        myDingDanView.setMyDingDanChangeListener(new MyDingDanView.MyDingDanChangeListener() {
            @Override
            public void moveToDest(int currid) {
                if(xuanzhe!=currid) {
                    setEnableds(currid);
                    ChangeData(currid);
                    xuanzhe = currid;
                }
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        int i = getIntent().getIntExtra("Static", 1);
        initdefault(i);//初始化数据
    }

    private void initdefault(int defaultxuanzhe) {
        ChangeView(defaultxuanzhe);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_button_yijie:
                ChangeView(1);
                break;
            case R.id.tv_button_daisong:
                ChangeView(2);
                break;
            case R.id.tv_button_wangchen:
                ChangeView(3);
                break;
            case R.id.tv_button_quxiao:
                ChangeView(4);
                break;
            case R.id.tv_head_fanghui:
                finish();
                break;
        }
    }

    private void ChangeView(int i) {
        myDingDanView.moveToDest(i-1);
    }

    class MyAdapter extends BaseAdapter {
        private List<OrderInfo> list;
        public MyAdapter(List<OrderInfo> list){
            this.list=list;
        }
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
//            TextView textView=new TextView(DingDanNewActivity.this);
//            textView.setText(list.get(position).getId());
//            return textView;
            try {
                View view;
                ZhuanTai zhuanTai;
                if (convertView != null) {
                    view = convertView;
                    zhuanTai = (ZhuanTai) view.getTag();
                } else {
                    zhuanTai = new ZhuanTai();
                    view = View.inflate(DingDanNewActivity.this, R.layout.dingdan_item, null);

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
//            Log.e("MyBaseAdapter", info.toString());
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
                Toast.makeText(DingDanNewActivity.this, "信息有误", Toast.LENGTH_SHORT).show();
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(DingDanNewActivity.this);
                            View inflate = View.inflate(DingDanNewActivity.this, R.layout.call_phone_dialog, null);
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
                    Log.e("Exception", e.getMessage());
                    Toast.makeText(DingDanNewActivity.this, "信息有误", Toast.LENGTH_SHORT).show();
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

    //选择时字体的颜色改变
    private void setEnableds(int i) {
        try {
            switch (i) {
                case 1:
                    tv_button_yijie.setBackgroundResource(R.drawable.anniubeijing);
                    tv_button_daisong.setBackgroundColor(Color.WHITE);
                    tv_button_wangchen.setBackgroundColor(Color.WHITE);
                    tv_button_quxiao.setBackgroundColor(Color.WHITE);
                    break;
                case 2:
                    tv_button_daisong.setBackgroundResource(R.drawable.anniubeijing);
                    tv_button_yijie.setBackgroundColor(Color.WHITE);
                    tv_button_wangchen.setBackgroundColor(Color.WHITE);
                    tv_button_quxiao.setBackgroundColor(Color.WHITE);
                    break;
                case 3:
                    tv_button_wangchen.setBackgroundResource(R.drawable.anniubeijing);
                    tv_button_daisong.setBackgroundColor(Color.WHITE);
                    tv_button_yijie.setBackgroundColor(Color.WHITE);
                    tv_button_quxiao.setBackgroundColor(Color.WHITE);
                    break;
                case 4:
                    tv_button_quxiao.setBackgroundResource(R.drawable.anniubeijing);
                    tv_button_daisong.setBackgroundColor(Color.WHITE);
                    tv_button_wangchen.setBackgroundColor(Color.WHITE);
                    tv_button_yijie.setBackgroundColor(Color.WHITE);
                    break;
            }
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
            Toast.makeText(DingDanNewActivity.this, "信息有误", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(DingDanNewActivity.this,"信息有误",Toast.LENGTH_SHORT).show();
        }
    }

    //网络申请得到相应状态的订单列表
    private void volley_getOrder_GET(final String State) {
        MyApplication.getQueue().cancelAll("volley_getOrder_GET");
        preferences = getSharedPreferences("config", MODE_PRIVATE);
        String token = preferences.getString("token", "");
        Log.e("volley_getOrder_GET", token);
        String url = "http://www.louxiago.com/wc/ddkd/admin.php/Order/getOrder/state/" + State + "/token/"+ token;
        StringRequest request_post = new StringRequest(Request.Method.GET, url, new MyStringRequest() {
            @Override
            public void success(Object o) {
                String s = (String) o;
                try {
//                    Log.e("volley_getOrder_GET", s);
                    List<OrderInfo> list = new ArrayList<>();
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
                        xuanZeCaoZuo(list, State);
                    } else {
                        list.clear();
                    }
                    //更新日期
                    Dataisget(State);
                } catch (Exception e) {
                    Log.e("Exception", e.getMessage());
                    Toast.makeText(DingDanNewActivity.this, "信息有误", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void tokenouttime() {
////               Log.e("volley_getOrder_GET", "token过时了");
//                AutologonUtil autologonUtil = new AutologonUtil(DingDanNewActivity.this, handler1, State);
//                autologonUtil.volley_Get_TOKEN();
            }

            @Override
            public void yidiensdfsdf() {
//                Exit.exit(DingDanNewActivity.this);
//                Toast.makeText(DingDanNewActivity.this, "您的账户已在异地登录", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Dataisget(State);
            }
        });
        request_post.setTag("volley_getOrder_GET");
        MyApplication.getQueue().add(request_post);
    }

    private void Dataisget(String State) {
        if("1".equals(State)){
            myAdapter1.notifyDataSetChanged();
            TextView textView= (TextView) addView1.getEmptyView();
            textView.setText("没有数据哦...亲！！！");
            addView1.setVisibility(View.VISIBLE);//显示数据
            relativeLayout1.setVisibility(View.GONE);//隐藏加载页面
        }else if("2".equals(State)){
            myAdapter2.notifyDataSetChanged();
            TextView textView= (TextView) addView2.getEmptyView();
            textView.setText("没有数据哦...亲！！！");
            addView2.setVisibility(View.VISIBLE);//显示数据
            relativeLayout2.setVisibility(View.GONE);//隐藏加载页面
        }else if("3".equals(State)){
            myAdapter3.notifyDataSetChanged();
            TextView textView= (TextView) addView3.getEmptyView();
            textView.setText("没有数据哦...亲！！！");
            addView3.setVisibility(View.VISIBLE);//显示数据
            relativeLayout3.setVisibility(View.GONE);//隐藏加载页面
        }else if("4".equals(State)){
            myAdapter4.notifyDataSetChanged();
            TextView textView= (TextView) addView4.getEmptyView();
            textView.setText("没有数据哦...亲！！！");
            addView4.setVisibility(View.VISIBLE);//显示数据
            relativeLayout4.setVisibility(View.GONE);//隐藏加载页面
        }
    }

    private void xuanZeCaoZuo(List<OrderInfo> list, String State) {
//        Log.e("xuanZeCaoZuo",State);
        if("1".equals(State)){
//            Log.e("xuanZeCaoZuo","111");
            list1.addAll(list);
        }else if("2".equals(State)){
//            Log.e("xuanZeCaoZuo","22");
            list2.addAll(list);
        }else if("3".equals(State)){
//            Log.e("xuanZeCaoZuo","33");
            list3.addAll(list);
        }else if("4".equals(State)){
//            Log.e("xuanZeCaoZuo","44");
            list4.addAll(list);
        }
    }

    private void ChangeData(int i){
        list1.clear();
        list2.clear();
        list3.clear();
        list4.clear();
        switch (i){
            case 1:
                addView1.getEmptyView().setVisibility(View.GONE);
                addView1.setVisibility(View.GONE);
                relativeLayout1.setVisibility(View.VISIBLE);
                volley_getOrder_GET("1");
                break;
            case 2:
                addView2.getEmptyView().setVisibility(View.GONE);
                addView2.setVisibility(View.GONE);
                relativeLayout2.setVisibility(View.VISIBLE);
                volley_getOrder_GET("2");
                break;
            case 3:
                addView3.getEmptyView().setVisibility(View.GONE);
                addView3.setVisibility(View.GONE);
                relativeLayout3.setVisibility(View.VISIBLE);
                volley_getOrder_GET("3");
                break;
            case 4:
                addView4.getEmptyView().setVisibility(View.GONE);
                addView4.setVisibility(View.GONE);
                relativeLayout4.setVisibility(View.VISIBLE);
                volley_getOrder_GET("4");
                break;

        }
    }

    //网络申请修改相应状态的订单列表
    private void volley_OrderState_GET(final OrderInfo info, final String State, final ProgressBar pb_button, final TextView button) {
        preferences = getSharedPreferences("config", MODE_PRIVATE);
        String token = preferences.getString("token", "");
        String url = "http://www.louxiago.com/wc/ddkd/admin.php/Order/setOrderState/id/" + info.getId() + "/state/" + State + "/token/"+ token;
//        参数一：方法 参数二：地址 参数三：成功回调 参数四：错误回调 。重写getParams 以post参数
        StringRequest request_post = new StringRequest(Request.Method.GET, url, new MyStringRequest() {
            @Override
            public void success(Object o) {
                String s = (String) o;
                try {
                    if ("SUCCESS".equals(s)) {
                        button.setEnabled(true);
                        pb_button.setVisibility(View.GONE);
                        if("3".equals(State)){
                            list2.remove(info);
                            myAdapter2.notifyDataSetChanged();
                        }else if("2".equals(State)){
                            list1.remove(info);
                            myAdapter1.notifyDataSetChanged();
                        }
                    } else {
                        button.setEnabled(true);
                        pb_button.setVisibility(View.GONE);
                        Toast.makeText(DingDanNewActivity.this, "网络连接错...", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("Exception", e.getMessage());
                    Toast.makeText(DingDanNewActivity.this, "信息有误", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void tokenouttime() {
//                Log.e("volley_getOrder_GET", "token过时了");
//                Object[] obj = {info, State, pb_button, button};
//                AutologonUtil autologonUtil = new AutologonUtil(DingDanNewActivity.this, handler2, obj);
//                autologonUtil.volley_Get_TOKEN();
            }

            @Override
            public void yidiensdfsdf() {
//                Exit.exit(DingDanNewActivity.this);
//                Toast.makeText(DingDanNewActivity.this, "您的账户已在异地登录", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                button.setEnabled(true);
                pb_button.setVisibility(View.GONE);
                if("3".equals(State)){
                    TextView textView = (TextView) addView2.getEmptyView();
                    textView.setText("网络连接中断...");
                    list2.clear();
                    myAdapter2.notifyDataSetChanged();
                }else if("2".equals(State)){
                    TextView textView = (TextView) addView1.getEmptyView();
                    textView.setText("网络连接中断...");
                    list1.clear();
                    myAdapter1.notifyDataSetChanged();
                }
            }
        });
        request_post.setTag("volley_OrderState_GET");
        MyApplication.getQueue().add(request_post);
    }
}
