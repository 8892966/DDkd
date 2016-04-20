package com.example.user.ddkd;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.ddkd.beam.OrderInfo;
import com.example.user.ddkd.utils.TimeCountUtil;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.XGPushTextMessage;
import com.tencent.android.tpush.service.XGPushService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2016-04-02.
 */
public class JieDangActivity extends Activity implements View.OnClickListener {
    private ListView listView;
    //DD指南的按钮
    private LinearLayout ll_ddzhinang;
    //奖励活动的按钮
    private LinearLayout ll_jianlihuodong;
    //查看详细订单的按钮
    private TextView tv_to_dingdang;
    //开始抢单或休息的按钮
    private TextView but_jiedang;
    //今天的接单数
    private TextView tv_xiuxi_huodong_now_number;
    //星星的评分
    private TextView tv_star;
    //接单的总单数
    private TextView tv_sum_number;
    //昨天接单的总单数
    private TextView tv_xiuxi_huodong_yesterday_number;
    //昨天的营业额
    private TextView tv_xiuxi_huodong_yesterday_money;
    //星星图型评分
    private RatingBar pb_star;

    private List<OrderInfo> list;
    //倒计时列
    private List<Integer> times;
    //用于记录倒计时应该删除的列
    private List<Integer> deltime;
    private SharedPreferences preferences;
    //判断接单状态
    private boolean i = true;
    //处理接单的handler
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MyApplication.XG_TEXT_MESSAGE:
//                    list.add((OrderInfo) msg.obj);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jiedang_activity);
        list=new ArrayList<OrderInfo>();
        times=new ArrayList<Integer>();
        deltime=new ArrayList<Integer>();
        TextView textView = (TextView) findViewById(R.id.personinfo);
        textView.setOnClickListener(this);
        MyApplication.setHandler(handler);
        listView = (ListView) findViewById(R.id.lv_jiedang);
        ll_ddzhinang = (LinearLayout) findViewById(R.id.ll_ddzhinang);
        ll_jianlihuodong = (LinearLayout) findViewById(R.id.ll_jianlihuodong);
        tv_to_dingdang = (TextView) findViewById(R.id.tv_to_dingdang);
        but_jiedang = (TextView) findViewById(R.id.but_jiedang);
        findViewById(R.id.tv_xiuxi_huodong_now_number);
        findViewById(R.id.pb_star);
        findViewById(R.id.tv_star);
        findViewById(R.id.tv_sum_number);
        findViewById(R.id.tv_xiuxi_huodong_yesterday_number);
        findViewById(R.id.tv_xiuxi_huodong_yesterday_money);
        ll_ddzhinang.setOnClickListener(this);
        ll_jianlihuodong.setOnClickListener(this);
        tv_to_dingdang.setOnClickListener(this);
        but_jiedang.setOnClickListener(this);
        //listView.notifyDataSetChanged();//刷新数据
        listView.setVisibility(View.GONE);
        listView.setAdapter(new MyBaseAdapter());
        //判断是否有开启信鸽
        preferences=getSharedPreferences("config", MODE_PRIVATE);
        if(preferences.getBoolean("XGisOpen",false)){
            i = false;
            listView.setVisibility(View.VISIBLE);
            but_jiedang.setText("休息");
            but_jiedang.setBackgroundResource(R.drawable.yuan_selected);
        };
    }
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.ll_ddzhinang:
                intent = new Intent(this, WebActivity.class);
                intent.putExtra("title", "DD指南");
                intent.putExtra("url","http://www.baidu.com");
                startActivity(intent);
                break;
            case R.id.ll_jianlihuodong:
                intent = new Intent(this, WebActivity.class);
                intent.putExtra("title", "奖励活动");
                intent.putExtra("url", "http://www.baidu.com");
                startActivity(intent);
                break;
            case R.id.tv_to_dingdang:
                intent = new Intent(this, DingDanActivity.class);
                startActivity(intent);
                break;
            case R.id.but_jiedang:
                if (i) {
//                  preferences=getSharedPreferences("config", MODE_PRIVATE);
                    SharedPreferences.Editor edit = preferences.edit();
                    edit.putBoolean("XGisOpen",true);
                    edit.commit();
                    i = false;
                    listView.setVisibility(View.VISIBLE);
                    but_jiedang.setText("休息");
                    but_jiedang.setBackgroundResource(R.drawable.yuan_selected);
                    // 开启logcat输出，方便debug，发布时请关闭
                    XGPushConfig.enableDebug(this, true);
                    // 如果需要知道注册是否成功，请使用eregisterPush(getApplicationContxt(), XGIOperateCallback)带callback版本
                    // 如果需要绑定账号，请使用registerPush(getApplicationContext(),account)版本
                    // 具体可参考详细的开发指南
                    // 传递的参数为ApplicationContext
                    Context context = getApplicationContext();
                    XGPushManager.registerPush(this, new XGIOperateCallback() {
                        @Override
                        public void onSuccess(Object data, int flag) {
                            Log.d("TPush", "注册成功，设备token为：" + data);
                        }
                        @Override
                        public void onFail(Object data, int errCode, String msg) {
                            Log.d("TPush", "注册失败，错误码：" + errCode + ",错误信息：" + msg);
                        }
                    });
//// 2.36（不包括）之前的版本需要调用以下2行代码
//                    Intent service = new Intent(context, XGPushService.class);
//                    context.startService(service);
                } else {
//                    preferences=getSharedPreferences("config", MODE_PRIVATE);
                    SharedPreferences.Editor edit = preferences.edit();
                    edit.putBoolean("XGisOpen",false);
                    edit.commit();
                    i = true;
                    listView.setVisibility(View.GONE);
                    but_jiedang.setText("听单");
                    but_jiedang.setBackgroundResource(R.drawable.yuan_color_gray);
                    XGPushManager.unregisterPush(this);
                }
                break;
            case R.id.personinfo://进入用户信息界面
                intent = new Intent(this, MainActivity_main.class);
                startActivity(intent);
                break;
        }
    }

    class MyBaseAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 10;
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view;
            ViewInfo viewInfo;
            if (convertView != null) {
                view = convertView;
                viewInfo = (ViewInfo) convertView.getTag();
            } else {
                viewInfo = new ViewInfo();
                view = View.inflate(JieDangActivity.this, R.layout.dialog_view, null);
                viewInfo.tv_item_jianli = (TextView) view.findViewById(R.id.tv_item_jianli);
                viewInfo.tv_addr = (TextView) view.findViewById(R.id.tv_addr);
                viewInfo.tv_class = (TextView) view.findViewById(R.id.tv_class);
                viewInfo.tv_item_title = (TextView) view.findViewById(R.id.tv_item_title);
                viewInfo.tv_qiangdan_button = (TextView) view.findViewById(R.id.tv_qiangdan_button);
                view.setTag(viewInfo);
            }
//                int e=times.get(position);
//                TimeCountUtil timeCountUtil=new TimeCountUtil(20*1000,1000,viewInfo.tv_qiangdan_button);
//                timeCountUtil.start();
            //处理数据，填写数据
            return view;
        }
        class ViewInfo {
            TextView tv_item_title;
            TextView tv_item_jianli;
            TextView tv_class;
            TextView tv_addr;
            TextView tv_qiangdan_button;
        }
    }
}
