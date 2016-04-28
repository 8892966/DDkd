package com.example.user.ddkd;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baidu.mobstat.StatService;
import com.example.user.ddkd.beam.MainMsgInfo;
import com.example.user.ddkd.beam.OrderInfo;
import com.example.user.ddkd.beam.QOrderInfo;
import com.example.user.ddkd.service.JieDanService;
import com.example.user.ddkd.utils.AutologonUtil;
import com.example.user.ddkd.utils.Exit;
import com.example.user.ddkd.utils.MyStringRequest;
import com.example.user.ddkd.utils.ServiceUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.example.user.ddkd.ExitApplication.*;

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

    private boolean sreviceisrunning;

    private List<QOrderInfo> list;
    //接单的服务
    private Intent jieDanServiceIntent;
    //接单服务的中间人
    private JieDanService.JDBinder jdBinder;
    //适配器
    private MyBaseAdapter myBaseAdapter;
    //获取后台token
    private SharedPreferences preferences;
    //播放短暂声音
    private SoundPool sp;
    //声音源
    private int soundid;




    //当获取页面信息时token过时的处理
    private Handler handler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MyApplication.GET_TOKEN_SUCCESS:
                    volley_MSG_GET();
                    break;
                case MyApplication.GET_TOKEN_ERROR:
                    Toast.makeText(JieDangActivity.this, "网络连接出错", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    //当抢单使，token过时的处理
    private Handler handler2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MyApplication.GET_TOKEN_SUCCESS:
                    Object[] obj= (Object[]) msg.obj;
                    String id= (String) obj[0];
                    TextView button= (TextView) obj[1];
                    volley_QD_GET(id,button);
                    break;
                case MyApplication.GET_TOKEN_ERROR:
                    Toast.makeText(JieDangActivity.this, "网络连接出错", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jiedang_activity);
        initSound();//初始化数据
        volley_MSG_GET();//获取页面信息
        list=new ArrayList<QOrderInfo>();
        TextView textView = (TextView) findViewById(R.id.personinfo);
        textView.setOnClickListener(this);
        listView = (ListView) findViewById(R.id.lv_jiedang);
        ll_ddzhinang = (LinearLayout) findViewById(R.id.ll_ddzhinang);
        ll_jianlihuodong = (LinearLayout) findViewById(R.id.ll_jianlihuodong);
        tv_to_dingdang = (TextView) findViewById(R.id.tv_to_dingdang);
        but_jiedang = (TextView) findViewById(R.id.but_jiedang);
        tv_xiuxi_huodong_now_number= (TextView) findViewById(R.id.tv_xiuxi_huodong_now_number);
        pb_star= (RatingBar) findViewById(R.id.pb_star);
        tv_star= (TextView) findViewById(R.id.tv_star);
        tv_sum_number= (TextView) findViewById(R.id.tv_sum_number);
        tv_xiuxi_huodong_yesterday_number= (TextView) findViewById(R.id.tv_xiuxi_huodong_yesterday_number);
        tv_xiuxi_huodong_yesterday_money= (TextView) findViewById(R.id.tv_xiuxi_huodong_yesterday_money);

        ll_ddzhinang.setOnClickListener(this);
        ll_jianlihuodong.setOnClickListener(this);
        tv_to_dingdang.setOnClickListener(this);
        but_jiedang.setOnClickListener(this);

        listView.setVisibility(View.GONE);
        myBaseAdapter = new MyBaseAdapter();
        listView.setAdapter(myBaseAdapter);
        listView.setEmptyView(findViewById(R.id.tv_jiedang));
        getInstance().addActivity(this);
        //判断是否有开启信鸽和服务
//        sreviceisrunning=ServiceUtils.isRunning(this,"com.example.user.ddkd.service.JieDanService");
//        if(sreviceisrunning){
//            listView.setVisibility(View.VISIBLE);
//            but_jiedang.setText("休息");
//            but_jiedang.setBackgroundResource(R.drawable.yuan_selected);
//            //服务一开，绑定服务
//            jieDanServiceIntent = new Intent(JieDangActivity.this, JieDanService.class);
//            bindService(jieDanServiceIntent,sc,BIND_AUTO_CREATE);
//        }
    }

    private void initSound() {
        sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        //第三个参数暂时无用
        //加载声音至声音池
//        soundid=sp.load(this, R.raw.fire, 1);
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
                intent = new Intent(this,DingDanActivity.class);
                startActivity(intent);
                break;
            case R.id.but_jiedang:
                if (!sreviceisrunning){
                    listView.getEmptyView().setVisibility(View.VISIBLE);
                    sreviceisrunning=true;
//                  preferences=getSharedPreferences("config", MODE_PRIVATE);
                    listView.setVisibility(View.VISIBLE);
                    but_jiedang.setText("休息");
                    but_jiedang.setBackgroundResource(R.drawable.yuan_selected);
                    jieDanServiceIntent = new Intent(JieDangActivity.this, JieDanService.class);
                    startService(jieDanServiceIntent);
                    bindService(jieDanServiceIntent,sc,BIND_AUTO_CREATE);
//// 2.36（不包括）之前的版本需要调用以下2行代码
//                    Intent service = new Intent(context, XGPushService.class);
//                    context.startService(service);
                }else{
                    listView.getEmptyView().setVisibility(View.GONE);
                    sreviceisrunning=false;
                    unbindService(sc);
                    jieDanServiceIntent = new Intent(JieDangActivity.this, JieDanService.class);
                    stopService(jieDanServiceIntent);
//                  preferences=getSharedPreferences("config", MODE_PRIVATE);
                    listView.setVisibility(View.GONE);
                    but_jiedang.setText("听单");
                    but_jiedang.setBackgroundResource(R.drawable.yuan_color_gray);
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
            //处理数据，填写数据
            QOrderInfo qOrderInfo=list.get(position);
            String s=qOrderInfo.getReceivePlace().split("/")[3];
            viewInfo.tv_addr.setText(s);
            viewInfo.tv_class.setText(qOrderInfo.getExpressCompany()+"快件    重量"+qOrderInfo.getWeight()+"左右");
            viewInfo.tv_item_jianli.setVisibility(View.GONE);
            viewInfo.tv_item_title.setText(qOrderInfo.getAddressee()+"    共"+qOrderInfo.getPrice()+"元(含小费"+qOrderInfo.getTip()+"元)");
            viewInfo.tv_qiangdan_button.setOnClickListener(new QDonClickListener(qOrderInfo.getOrderid(),viewInfo.tv_qiangdan_button));
//                int e=times.get(position);
//                TimeCountUtil timeCountUtil=new TimeCountUtil(20*1000,1000,viewInfo.tv_qiangdan_button);
//                timeCountUtil.start();
            return view;
        }
        class QDonClickListener implements View.OnClickListener {
            private String id;
            private TextView button;
            public QDonClickListener(String id,TextView button){
                this.id=id;
                this.button=button;
            }
            @Override
            public void onClick(View v) {
                volley_QD_GET(id,button);
            }
        }
        class ViewInfo {
            TextView tv_item_title;
            TextView tv_item_jianli;
            TextView tv_class;
            TextView tv_addr;
            TextView tv_qiangdan_button;
        }
    }
    @Override
    protected void onResume(){
        StatService.onResume(this);
        SharedPreferences sharedPreferences=getSharedPreferences("qtmsg", MODE_PRIVATE);

        Log.e("JieDangActivity", getIntent().getBooleanExtra("info", false) + "");

        if(!sharedPreferences.getString("QT", "").equals("")){
//            Log.e("onResume","1111111111111111111111");
            jieDanServiceIntent = new Intent(JieDangActivity.this, JieDanService.class);
            startService(jieDanServiceIntent);
//            bindService(jieDanServiceIntent,sc,BIND_AUTO_CREATE);
        }
        sreviceisrunning=ServiceUtils.isRunning(this,"com.example.user.ddkd.service.JieDanService");
        Log.e("isRunning", sreviceisrunning + "");
        if(sreviceisrunning){
            listView.setVisibility(View.VISIBLE);
            but_jiedang.setText("休息");
            but_jiedang.setBackgroundResource(R.drawable.yuan_selected);
            //服务一开，绑定服务
            jieDanServiceIntent = new Intent(JieDangActivity.this, JieDanService.class);
            bindService(jieDanServiceIntent,sc,BIND_AUTO_CREATE);
        }
        super.onResume();
    }
    @Override
    protected void onPause(){
        super.onPause();
//        Log.e("onPause","2222222222222222");
        if(sreviceisrunning){
            unbindService(sc);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        StatService.onPause(this);
        MyApplication.getQueue().cancelAll("volley_MSG_GET");
        MyApplication.getQueue().cancelAll("volley_QD_GET");
    }

    //绑定服务
    private ServiceConnection sc=new ServiceConnection(){
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            jdBinder = (JieDanService.JDBinder) service;
//            Log.e("ServiceConnection", "jinru");
            jdBinder.SendIJD(new JieDanService.IJD() {
                @Override
                public void Delete(List list) {
                    JieDangActivity.this.list.removeAll(list);
//                    sp.play(soundid, 1.0f, 0.3f, 0, 0, 2.0f);
                    myBaseAdapter.notifyDataSetChanged();//刷新数据
                }

                @Override
                public void Add(List list) {
                    JieDangActivity.this.list.addAll(list);
                    myBaseAdapter.notifyDataSetChanged();//刷新数据

                }
            });
            list=jdBinder.getMsg();
            Gson gson=new Gson();
            SharedPreferences sharedPreferences=getSharedPreferences("qtmsg", MODE_PRIVATE);
            String qt=sharedPreferences.getString("QT", "");
            sharedPreferences.edit().putString("QT","").commit();
            List QTli = gson.fromJson(qt, new TypeToken<List<QOrderInfo>>() {
            }.getType());
            if(QTli!=null) {
                list.addAll(QTli);
                jdBinder.setMsg(QTli);
            }
//            Log.e("ServiceConnection", list.size() + "");
            myBaseAdapter.notifyDataSetChanged();
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    //网络申请获取主页面信息
    private void volley_MSG_GET(){
        preferences = getSharedPreferences("config", MODE_PRIVATE);
        String token = preferences.getString("token", "");
        String url = "http://www.louxiago.com/wc/ddkd/admin.php/Order/CountOrder/token/" + token;
        Log.e("volley_OrderState_GET",url);
        StringRequest request_post = new StringRequest(Request.Method.GET, url, new MyStringRequest(){
            @Override
            public void success(Object o){
                    Gson gson = new Gson();
                    MainMsgInfo info = gson.fromJson((String)o,MainMsgInfo.class);
                    tv_xiuxi_huodong_now_number.setText("接单" + info.getTodOrder() + "单");
                    tv_star.setText(info.getEvaluate());
                    tv_sum_number.setText("总" + info.getTotalOrder() + "单");
                    tv_xiuxi_huodong_yesterday_number.setText("昨天订单：" + info.getYstOrder() + "单");
                    if (info.getYstTurnover() != null) {
                        DecimalFormat g = new DecimalFormat("0.00");//精确到两位小数
                        tv_xiuxi_huodong_yesterday_money.setText("昨天营业额:" +  g.format(Double.valueOf(info.getYstTurnover())) + "元");
                    } else {
                        tv_xiuxi_huodong_yesterday_money.setText("昨天营业额:0元");
                    }
                    if (info.getEvaluate() == null){
                        pb_star.setRating(0);
                    } else {
                        pb_star.setRating(Float.valueOf(info.getEvaluate()));
                    }
            }
            @Override
            public void tokenouttime() {
                AutologonUtil autologonUtil = new AutologonUtil(JieDangActivity.this,handler1,null);
                autologonUtil.volley_Get_TOKEN();
            }
            @Override
            public void yidiensdfsdf() {
                Toast.makeText(JieDangActivity.this,"您的账号在其他地方被登陆，请在此登陆",Toast.LENGTH_SHORT).show();
                Exit.exit(JieDangActivity.this);
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError){
                Toast.makeText(JieDangActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
            }
        });
//        StringRequest request_post = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String s) {
//                Log.e("volley_OrderState_GET", s);
//                if (!s.endsWith("\"token outtime\"")) {
//                    if("error".equals(s)){
//                        Gson gson = new Gson();
//                        MainMsgInfo info = gson.fromJson(s, MainMsgInfo.class);
//                        tv_xiuxi_huodong_now_number.setText("接单" + info.getTodOrder() + "单");
//                        tv_star.setText(info.getEvaluate());
//                        tv_sum_number.setText("总" + info.getTotalOrder() + "单");
//                        tv_xiuxi_huodong_yesterday_number.setText("昨天订单：" + info.getYstOrder() + "单");
//                        if (info.getYstTurnover() != null) {
//                            tv_xiuxi_huodong_yesterday_money.setText("昨天营业额:" + info.getYstTurnover() + "元");
//                        } else {
//                            tv_xiuxi_huodong_yesterday_money.setText("昨天营业额:0元");
//                        }
//                        if (info.getEvaluate() == null) {
//                            pb_star.setRating(0);
//                        } else {
//                            pb_star.setRating(Float.valueOf(info.getEvaluate()));
//                        }
//                    }
//                } else {
//                    Log.e("volley_getOrder_GET", "token过时了");
//                    AutologonUtil autologonUtil = new AutologonUtil(JieDangActivity.this,handler1,null);
//                    autologonUtil.volley_Get_TOKEN();
//                }
//            }
//        },new Response.ErrorListener(){
//            @Override
//            public void onErrorResponse(VolleyError volleyError){
//                Toast.makeText(JieDangActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
//            }
//        });
        request_post.setTag("volley_MSG_GET");
        MyApplication.getQueue().add(request_post);
    }
    //抢单数据
    private void volley_QD_GET(final String id, final TextView button) {
        preferences = getSharedPreferences("config", MODE_PRIVATE);
        String token = preferences.getString("token", "");
        String XGtoken=preferences.getString("XGtoken","");
        Log.e("volley_QD_GET",XGtoken);
        String url = "http://www.louxiago.com/wc/ddkd/admin.php/Order/RobOrder/orderId/"+id+"/token/" + token+"/deviceId/"+XGtoken;
        StringRequest request_post = new StringRequest(Request.Method.GET, url,new MyStringRequest(){
            @Override
            public void success(Object o) {
                String s= (String) o;
                if(!s.equals("SUCCESS")){
                    Toast.makeText(JieDangActivity.this,"网络异常",Toast.LENGTH_LONG).show();
                }else{
                    button.setEnabled(false);
                    button.setTextColor(Color.BLACK);
                    button.setText("等待");
                    Toast.makeText(JieDangActivity.this,"请等待抢单信息",Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void tokenouttime() {
                Log.e("volley_QD_GET", "token过时了");
                Object[] obj={id,button};
                AutologonUtil autologonUtil = new AutologonUtil(JieDangActivity.this,handler2,obj);
                autologonUtil.volley_Get_TOKEN();
            }
            @Override
            public void yidiensdfsdf() {
                Toast.makeText(JieDangActivity.this,"您的账户已在异地登录",Toast.LENGTH_SHORT).show();
                Exit.exit(JieDangActivity.this);
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(JieDangActivity.this,"网络异常",Toast.LENGTH_LONG).show();
            }
        });
            request_post.setTag("volley_QD_GET");
            MyApplication.getQueue().add(request_post);
        }
    long[] djtime =new long[2];
    @Override
    public void onBackPressed(){
            System.arraycopy(djtime,1,djtime,0,djtime.length-1);
            djtime[djtime.length-1]= SystemClock.uptimeMillis();
            if(djtime[0]>=(SystemClock.uptimeMillis()-1000)){
//                super.onBackPressed();
                ExitApplication.getInstance().exit();
            }else{
                Toast.makeText(this, "在按一次返回键退出应用", Toast.LENGTH_SHORT).show();
            }
    }

}
