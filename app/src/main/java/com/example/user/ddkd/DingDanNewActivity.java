package com.example.user.ddkd;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
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
import com.example.user.ddkd.Adapter.DingDanAdapter;
import com.example.user.ddkd.Presenter.DingDanPresenterImpl;
import com.example.user.ddkd.Presenter.IDingDanPresenter;
import com.example.user.ddkd.UI.MyDingDanView;
import com.example.user.ddkd.View.IDingDanView;
import com.example.user.ddkd.beam.OrderInfo;
import com.example.user.ddkd.utils.MyStringRequest;
import com.example.user.ddkd.utils.TimeUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class DingDanNewActivity extends BaseActivity implements View.OnClickListener,IDingDanView {
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
    private DingDanAdapter myAdapter1;
    private RelativeLayout relativeLayout2;
    private DingDanAdapter myAdapter2;
    private RelativeLayout relativeLayout3;
    private DingDanAdapter myAdapter3;
    private RelativeLayout relativeLayout4;
    private DingDanAdapter myAdapter4;
    private ListView addView1;
    private ListView addView2;
    private ListView addView3;
    private ListView addView4;
    private TextView tv_head_fanghui;
    private SharedPreferences preferences;
    private DingDanAdapter.StaticListener staticListener;
    private IDingDanPresenter iDingDinPresenter;
    private boolean isInitDefault=false;

    @Override
    protected boolean addStack() {
        return false;
    }

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

        iDingDinPresenter=new DingDanPresenterImpl(this);

        staticListener=new DingDanAdapter.StaticListener() {
            @Override
            public void xuanzhe2Change3(OrderInfo info, ProgressBar pb_button, TextView button) {
                iDingDinPresenter.ChangeDingDins(info,3,pb_button,button,getToken());
            }

            @Override
            public void xuanzhe1Change2(OrderInfo info, ProgressBar pb_button, TextView button) {
                iDingDinPresenter.ChangeDingDins(info, 2, pb_button, button, getToken());
            }
        };
        //在list尾部使出现的一个textview
        TextView textView=new TextView(this);
        textView.setGravity(Gravity.CENTER);
        textView.setText("已经没有更多的数据了亲...!");

        View view1=View.inflate(this,R.layout.dingdan_activity,null);
        addView1 = (ListView) view1.findViewById(R.id.lv_order_details);
        addView1.setDivider(new ColorDrawable(Color.parseColor("#00000000")));
        relativeLayout1 = (RelativeLayout) view1.findViewById(R.id.rl_order_ProgressBar);
        addView1.setEmptyView(view1.findViewById(R.id.tv_default));
        myAdapter1 = new DingDanAdapter(this,list1,1,staticListener);
        addView1.addFooterView(textView);
        addView1.setAdapter(myAdapter1);
        myDingDanView.addView(view1);

        View view2=View.inflate(this,R.layout.dingdan_activity,null);
        addView2 = (ListView) view2.findViewById(R.id.lv_order_details);
        addView2.setDivider(new ColorDrawable(Color.parseColor("#00000000")));
        relativeLayout2 = (RelativeLayout) view2.findViewById(R.id.rl_order_ProgressBar);
        addView2.setEmptyView(view2.findViewById(R.id.tv_default));
        myAdapter2 = new DingDanAdapter(this,list2,2,staticListener);
        addView2.addFooterView(textView);
        addView2.setAdapter(myAdapter2);
        myDingDanView.addView(view2);

        View view3=View.inflate(this,R.layout.dingdan_activity,null);
        addView3 = (ListView) view3.findViewById(R.id.lv_order_details);
        addView3.setDivider(new ColorDrawable(Color.parseColor("#00000000")));
        relativeLayout3 = (RelativeLayout) view3.findViewById(R.id.rl_order_ProgressBar);
        addView3.setEmptyView(view3.findViewById(R.id.tv_default));
        myAdapter3 = new DingDanAdapter(this,list3,3,staticListener);
        addView3.addFooterView(textView);
        addView3.setAdapter(myAdapter3);
        myDingDanView.addView(view3);

        View view4=View.inflate(this,R.layout.dingdan_activity,null);
        addView4 = (ListView) view4.findViewById(R.id.lv_order_details);
        addView4.setDivider(new ColorDrawable(Color.parseColor("#00000000")));
        relativeLayout4 = (RelativeLayout) view4.findViewById(R.id.rl_order_ProgressBar);
        addView4.setEmptyView(view4.findViewById(R.id.tv_default));
        myAdapter4 = new DingDanAdapter(this,list4,4,staticListener);
        addView4.addFooterView(textView);
        addView4.setAdapter(myAdapter4);
        myDingDanView.addView(view4);

        myDingDanView.setMyDingDanChangeListener(new MyDingDanView.MyDingDanChangeListener() {
            @Override
            public void moveToDest(int currid) {
                if (xuanzhe != currid) {
                    setEnableds(currid);
                    xuanzhe = currid;
                    iDingDinPresenter.loadDingDins(currid, getToken());
                }
            }
        });
        AbsListView.OnScrollListener onScrollListener=new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                switch (xuanzhe){
                    case 1:
                        myAdapter1.dismissPopuWindow();
                        break;
                    case 2:
                        myAdapter2.dismissPopuWindow();
                        break;
                    case 3:
                        myAdapter3.dismissPopuWindow();
                        break;
                    case 4:
                        myAdapter4.dismissPopuWindow();
                        break;
                }
            }
        };

        addView1.setOnScrollListener(onScrollListener);
        addView2.setOnScrollListener(onScrollListener);
        addView3.setOnScrollListener(onScrollListener);
        addView4.setOnScrollListener(onScrollListener);
    }

    private String getToken() {
        preferences = getSharedPreferences("config", MODE_PRIVATE);
        return preferences.getString("token", "");
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(!isInitDefault) {
            int i = getIntent().getIntExtra("Static", 1);
            initdefault(i);//初始化数据
            isInitDefault=true;
        }
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
        myDingDanView.moveToDest(i - 1);
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
    @Override
    public void showProgress(int xuanzhe) {
        switch (xuanzhe){
            case 1:
                addView1.getEmptyView().setVisibility(View.GONE);
                addView1.setVisibility(View.GONE);
                relativeLayout1.setVisibility(View.VISIBLE);
                break;
            case 2:
                addView2.getEmptyView().setVisibility(View.GONE);
                addView2.setVisibility(View.GONE);
                relativeLayout2.setVisibility(View.VISIBLE);
                break;
            case 3:
                addView3.getEmptyView().setVisibility(View.GONE);
                addView3.setVisibility(View.GONE);
                relativeLayout3.setVisibility(View.VISIBLE);
                break;
            case 4:
                addView4.getEmptyView().setVisibility(View.GONE);
                addView4.setVisibility(View.GONE);
                relativeLayout4.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void resetDindDan(List<OrderInfo> newsList,int xuanzhe) {
        switch (xuanzhe){
            case 1:
                list1.addAll(newsList);
                myAdapter1.notifyDataSetChanged();
                break;
            case 2:
                list2.addAll(newsList);
                myAdapter2.notifyDataSetChanged();
                break;
            case 3:
                list3.addAll(newsList);
                myAdapter3.notifyDataSetChanged();
                break;
            case 4:
                list4.addAll(newsList);
                myAdapter4.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void ClearData() {
        list1.clear();
        list2.clear();
        list3.clear();
        list4.clear();
    }

    @Override
    public void hideProgress(int xuanzhe) {
        switch (xuanzhe){
            case 1:
                TextView textView1= (TextView) addView1.getEmptyView();
                textView1.setText("没有数据哦...亲！！！");
                addView1.setVisibility(View.VISIBLE);//显示数据
                relativeLayout1.setVisibility(View.GONE);//隐藏加载页面
                break;
            case 2:
                TextView textView2= (TextView) addView2.getEmptyView();
                textView2.setText("没有数据哦...亲！！！");
                addView2.setVisibility(View.VISIBLE);//显示数据
                relativeLayout2.setVisibility(View.GONE);//隐藏加载页面
                break;
            case 3:
                TextView textView3= (TextView) addView3.getEmptyView();
                textView3.setText("没有数据哦...亲！！！");
                addView3.setVisibility(View.VISIBLE);//显示数据
                relativeLayout3.setVisibility(View.GONE);//隐藏加载页面
                break;
            case 4:
                TextView textView4= (TextView) addView4.getEmptyView();
                textView4.setText("没有数据哦...亲！！！");
                addView4.setVisibility(View.VISIBLE);//显示数据
                relativeLayout4.setVisibility(View.GONE);//隐藏加载页面
                break;
        }
    }
    //加载失败网络有问题
    @Override
    public void showLoadFailMsg() {

    }

    @Override
    public void showChangeProgress(ProgressBar pb_button, TextView button) {
        button.setEnabled(false);
        pb_button.setVisibility(View.VISIBLE);
    }

    @Override
    public void removeDindDan(OrderInfo info, int xuanzhe) {
        switch (xuanzhe){
            case 3:
                list2.remove(info);
                myAdapter2.notifyDataSetChanged();
                break;
            case 2:
                list1.remove(info);
                myAdapter1.notifyDataSetChanged();
                break;
        }
    }
    @Override
    public void showErrorToast(){
        Toast.makeText(DingDanNewActivity.this, "网络连接错...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onChangeFailure(Exception e){
//            Log.e("Exception", e.getMessage()+"");
    Toast.makeText(DingDanNewActivity.this, "信息有误", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onChangeErrorResponse(int xuanzhe,ProgressBar pb_button,TextView button) {
        button.setEnabled(true);
        pb_button.setVisibility(View.GONE);
        switch (xuanzhe){
            case 1:
                TextView textView1 = (TextView) addView1.getEmptyView();
                textView1.setText("网络连接中断...");
                list1.clear();
                myAdapter1.notifyDataSetChanged();
                break;
            case 2:
                TextView textView2 = (TextView) addView2.getEmptyView();
                textView2.setText("网络连接中断...");
                list2.clear();
                myAdapter2.notifyDataSetChanged();
                break;
            case 3:
                TextView textView3 = (TextView) addView3.getEmptyView();
                textView3.setText("网络连接中断...");
                list3.clear();
                myAdapter3.notifyDataSetChanged();
                break;
            case 4:
                TextView textView4 = (TextView) addView4.getEmptyView();
                textView4.setText("网络连接中断...");
                list4.clear();
                myAdapter4.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onloadErrorResponse(int xuanzhe) {
        hideProgress(xuanzhe);
        switch (xuanzhe){
            case 1:
                myAdapter1.notifyDataSetChanged();
                break;
            case 2:
                myAdapter2.notifyDataSetChanged();
                break;
            case 3:
                myAdapter3.notifyDataSetChanged();
                break;
            case 4:
                myAdapter4.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void hideChangeProgress(ProgressBar pb_button, TextView button) {
        button.setEnabled(true);
        pb_button.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        iDingDinPresenter=null;
        finish();
    }
}
