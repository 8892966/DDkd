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
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baidu.mobstat.StatService;
import com.example.user.ddkd.beam.MainMsgInfo;
import com.example.user.ddkd.beam.QOrderInfo;
import com.example.user.ddkd.service.JieDanService;
import com.example.user.ddkd.utils.AutologonUtil;
import com.example.user.ddkd.utils.Exit;
import com.example.user.ddkd.utils.GDOrderUtil;
import com.example.user.ddkd.utils.MyStringRequest;
import com.example.user.ddkd.utils.ServiceUtils;
import com.example.user.ddkd.utils.SlidingUtil;
import com.example.user.ddkd.utils.UserInfoUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import static com.example.user.ddkd.ExitApplication.*;

/**
 * Created by User on 2016-04-02.
 */
public class JieDangActivity extends Activity implements View.OnClickListener{
    private final static int GDSX=11;//挂单刷新
    private TextView textView;
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
//    private TextView tv_star;
    //接单的总单数
    private TextView tv_sum_number;
    //昨天接单的总单数
//    private TextView tv_xiuxi_huodong_yesterday_number;
    //昨天的营业额
    private TextView tv_xiuxi_huodong_yesterday_money;
    //星星图型评分
//    private RatingBar pb_star;

    private boolean sreviceisrunning;

    private List<QOrderInfo> list;//总数据

    private List<QOrderInfo> list_GD;//挂单数据
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
    //星星图
    private ImageView xx1;
    private ImageView xx2;
    private ImageView xx3;
    private ImageView xx4;
    private ImageView xx5;
    private GDOrderUtil gdOrderUtil;
    private SlidingUtil slidingUtil;
    private UserInfoUtils userInfoUtils;
    private GestureDetector detector;
    private VelocityTracker vt;

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
                case GDSX:
//                    Log.e("fdsfda", "fdafa1111111111");
                    SharedPreferences sharedPreferences=getSharedPreferences("config", MODE_PRIVATE);
                    try {
                        if (!sreviceisrunning||!sharedPreferences.getBoolean("isjieDangActivityrunn",false)) {

                        }else {
                            getGDorder();
                        }
                    }catch (Exception e){
//                        Log.e("fdsfda","22222222222222222222");
                    }
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
                    Object[] obj = (Object[]) msg.obj;
                    String id = (String) obj[0];
                    TextView button = (TextView) obj[1];
                    QOrderInfo qOrderInfo = (QOrderInfo) obj[2];
                    volley_QD_GET(id, button, qOrderInfo);
                    break;
                case MyApplication.GET_TOKEN_ERROR:
                    Toast.makeText(JieDangActivity.this, "网络连接出错", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    //当抢单使，token过时的处理
    private Handler handler3= new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MyApplication.GET_TOKEN_SUCCESS:
                    String id= (String) msg.obj;
                    gdOrderUtil.volley_QDGD_GET(id, JieDangActivity.this, handler3);
                    break;
                case MyApplication.GET_TOKEN_ERROR:
                    Toast.makeText(JieDangActivity.this, "网络连接出错", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    //当抢单使，token过时的处理
    private Handler handler4 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MyApplication.GET_TOKEN_SUCCESS:
                    gdOrderUtil.volley_GDMSG_GET_UTILS(JieDangActivity.this,handler4);
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

        //*****************************实现侧滑效果
        slidingUtil= (SlidingUtil) findViewById(R.id.it_menu);
        userInfoUtils=new UserInfoUtils(slidingUtil,JieDangActivity.this);

        initSound();//初始化数据
//        volley_MSG_GET();//获取页面信息
        list = new ArrayList<QOrderInfo>();
        list_GD=new ArrayList<QOrderInfo>();
        textView = (TextView) findViewById(R.id.personinfo);
        textView.setOnClickListener(this);
        listView = (ListView) findViewById(R.id.lv_jiedang);
        ll_ddzhinang = (LinearLayout) findViewById(R.id.ll_ddzhinang);
        ll_jianlihuodong = (LinearLayout) findViewById(R.id.ll_jianlihuodong);
        tv_to_dingdang = (TextView) findViewById(R.id.tv_to_dingdang);
        but_jiedang = (TextView) findViewById(R.id.but_jiedang);
        tv_xiuxi_huodong_now_number = (TextView) findViewById(R.id.tv_xiuxi_huodong_now_number);
//        pb_star = (RatingBar) findViewById(R.id.pb_star);
//        tv_star = (TextView) findViewById(R.id.tv_star);
        tv_sum_number = (TextView) findViewById(R.id.tv_sum_number);
//        tv_xiuxi_huodong_yesterday_number = (TextView) findViewById(R.id.tv_xiuxi_huodong_yesterday_number);
        tv_xiuxi_huodong_yesterday_money = (TextView) findViewById(R.id.tv_xiuxi_huodong_yesterday_money);

        ll_ddzhinang.setOnClickListener(this);
        ll_jianlihuodong.setOnClickListener(this);
        tv_to_dingdang.setOnClickListener(this);
        but_jiedang.setOnClickListener(this);

        listView.setVisibility(View.GONE);
        myBaseAdapter = new MyBaseAdapter();
        listView.setAdapter(myBaseAdapter);
        listView.setEmptyView(findViewById(R.id.tv_jiedang));
        getInstance().addActivity(this);

        xx1= (ImageView) findViewById(R.id.xx1);
        xx2= (ImageView) findViewById(R.id.xx2);
        xx3= (ImageView) findViewById(R.id.xx3);
        xx4= (ImageView) findViewById(R.id.xx4);
        xx5= (ImageView) findViewById(R.id.xx5);

        gdOrderUtil = new GDOrderUtil() {
            @Override
            public void getOrder(Object o) {
                list.removeAll(list_GD);
                list_GD = (List<QOrderInfo>) o;
                list.addAll(list_GD);
                myBaseAdapter.notifyDataSetChanged();
            }
        };

        //登陆初始化开始听单
        SharedPreferences sharedPreferences=getSharedPreferences("config",MODE_PRIVATE);
        boolean bb=sharedPreferences.getBoolean("qiandan1",true);
        if(bb){
            jieDanServiceIntent = new Intent(getApplicationContext(), JieDanService.class);
            startService(jieDanServiceIntent);
            listView.setVisibility(View.VISIBLE);
            but_jiedang.setBackgroundResource(R.drawable.kaiguanann);
            jieDanServiceIntent = new Intent(getApplicationContext(), JieDanService.class);
            bindService(jieDanServiceIntent, sc, BIND_AUTO_CREATE);
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putBoolean("qiandan1",false);
            edit.commit();
            getGDorder();

        }

    }

    private void initSound() {
        sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        //第三个参数暂时无用
        //加载声音至声音池
        soundid=sp.load(this, R.raw.ddkd, 1);
    }
    private void play(){//声音开始
        try {
            SharedPreferences spf=getSharedPreferences("config",MODE_PRIVATE);
            boolean b=spf.getBoolean("voice",true);
            if(b){
                sp.play(soundid, 1.0f, 0.3f, 0, 0, 1);
            }
        }catch (Exception e){
            Log.e("Exception", e.getMessage());
            Toast.makeText(JieDangActivity.this,"信息有误!!!",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onClick(View v) {
        try {
            Intent intent;
            switch (v.getId()) {
                case R.id.ll_ddzhinang:
                    intent = new Intent(this, WebActivity.class);
                    intent.putExtra("title", "DD指南");
                    intent.putExtra("url", "http://www.louxiago.com/wc/ddkd/index.php/DDGuid/index.html");
                    startActivity(intent);
                    break;
                case R.id.ll_jianlihuodong:
                    intent = new Intent(this, WebActivity.class);
                    intent.putExtra("title", "奖励活动");
                    intent.putExtra("url", "http://www.louxiago.com/wc/ddkd/index.php/RewardAct/index.html");
                    startActivity(intent);
                    break;
                case R.id.tv_to_dingdang:
                    intent = new Intent(this, DingDanNewActivity.class);
                    startActivity(intent);
                    break;
                case R.id.but_jiedang:
//                    SharedPreferences sharedPreferences=getSharedPreferences("config",MODE_PRIVATE);
                    if (!sreviceisrunning) {
//                        sharedPreferences.edit().putBoolean(MyApplication.TD,true).commit();//听单
                        listView.getEmptyView().setVisibility(View.VISIBLE);
                        sreviceisrunning = true;
//                  preferences=getSharedPreferences("config", MODE_PRIVATE);
                        listView.setVisibility(View.VISIBLE);
//                    but_jiedang.setText("休息");
                        but_jiedang.setBackgroundResource(R.drawable.kaiguanann);
                        jieDanServiceIntent = new Intent(getApplicationContext(), JieDanService.class);
                        startService(jieDanServiceIntent);
                        bindService(jieDanServiceIntent, sc, BIND_AUTO_CREATE);
                        myBaseAdapter.notifyDataSetChanged();
                        getGDorder();
                    } else {
//                        sharedPreferences.edit().putBoolean(MyApplication.TD,false).commit();//听单
                        MyApplication.getQueue().cancelAll("volley_GDMSG_GET_UTILS");
                        listView.getEmptyView().setVisibility(View.GONE);
                        sreviceisrunning = false;
                        unbindService(sc);
                        jieDanServiceIntent = new Intent(getApplicationContext(), JieDanService.class);
                        stopService(jieDanServiceIntent);
//                  preferences=getSharedPreferences("config", MODE_PRIVATE);
                        listView.setVisibility(View.GONE);
//                    but_jiedang.setText("听单");
                        but_jiedang.setBackgroundResource(R.drawable.kaiguan);
                    }
                    break;
                case R.id.personinfo://进入用户信息界面
//                    intent = new Intent(this, MainActivity_main.class);
//                    startActivity(intent);
                   slidingUtil.changeMenu();

//                    overridePendingTransition(R.anim.in_toright, R.anim.out_toleft);
                    break;
            }
        }catch (Exception e){
            Log.e("Exception", e.getMessage());
            Toast.makeText(JieDangActivity.this,"信息有误!!!",Toast.LENGTH_SHORT).show();
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
                    viewInfo.order_id = (TextView) view.findViewById(R.id.order_id);
                    viewInfo.tv_zhonglian = (TextView) view.findViewById(R.id.tv_zhonglian);
                    view.setTag(viewInfo);
                }
                //处理数据，填写数据
                QOrderInfo qOrderInfo = list.get(position);
                String s = qOrderInfo.getReceivePlace().split("/")[3];
                if (s != null) {
                    viewInfo.tv_addr.setText(s);
                }

                if (qOrderInfo.getExpressCompany() != null) {

                    viewInfo.tv_class.setText(qOrderInfo.getExpressCompany() + "快件");

                }

                if (qOrderInfo.getWeight() != null) {

                    viewInfo.tv_zhonglian.setText(qOrderInfo.getWeight() + "kg左右");

                }

                viewInfo.tv_item_jianli.setVisibility(View.GONE);
                if (qOrderInfo.getAddressee() != null && qOrderInfo.getPrice() != null && qOrderInfo.getTip() != null) {
                    float f=Float.valueOf(qOrderInfo.getPrice())+Float.valueOf(qOrderInfo.getTip());
                    viewInfo.tv_item_title.setText(qOrderInfo.getAddressee() + "    共" + f + "元(含小费" + qOrderInfo.getTip() + "元)");
                }
                if (qOrderInfo.getZhuantai() == 2) {
                    viewInfo.tv_qiangdan_button.setEnabled(false);
                    viewInfo.tv_qiangdan_button.setTextColor(Color.BLACK);
                    viewInfo.tv_qiangdan_button.setText("已抢");
                } else if (qOrderInfo.getZhuantai() == 1) {
                    viewInfo.tv_qiangdan_button.setEnabled(false);
                    viewInfo.tv_qiangdan_button.setTextColor(Color.BLACK);
                    viewInfo.tv_qiangdan_button.setText("等待");
                } else {
                    viewInfo.tv_qiangdan_button.setEnabled(true);
                    viewInfo.tv_qiangdan_button.setText("抢单");
                }
                Log.e("getView",qOrderInfo.getBespeak()+"");
                if(qOrderInfo.getBespeak()!=null){
                if(!"0".equals(qOrderInfo.getBespeak())) {
                    if (qOrderInfo.getOrderid() != null) {
                        viewInfo.order_id.setText("(挂单)单号:" + qOrderInfo.getOrderid());
                    }
                    viewInfo.tv_qiangdan_button.setOnClickListener(new GDonClickListener(qOrderInfo, viewInfo.tv_qiangdan_button));
                }else {
                    if (qOrderInfo.getOrderid() != null) {
                        viewInfo.order_id.setText("单号:" + qOrderInfo.getOrderid());
                    }
                    viewInfo.tv_qiangdan_button.setOnClickListener(new QDonClickListener(qOrderInfo, viewInfo.tv_qiangdan_button));
                }}else {
                    if (qOrderInfo.getOrderid() != null) {
                        viewInfo.order_id.setText("单号:" + qOrderInfo.getOrderid());
                    }
                    viewInfo.tv_qiangdan_button.setOnClickListener(new QDonClickListener(qOrderInfo, viewInfo.tv_qiangdan_button));
                }
//                int e=times.get(position);
//                TimeCountUtil timeCountUtil=new TimeCountUtil(20*1000,1000,viewInfo.tv_qiangdan_button);
//                timeCountUtil.start();
                return view;
            }catch (Exception e){
                Log.e("Exception", e.getMessage());
                Toast.makeText(JieDangActivity.this,"信息有误!!!",Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        class QDonClickListener implements View.OnClickListener {
            private String id;
            private TextView button;
            private QOrderInfo qOrderInfo;
            public QDonClickListener(QOrderInfo qOrderInfo, TextView button) {
                this.id = qOrderInfo.getOrderid();
                this.button = button;
                this.qOrderInfo = qOrderInfo;
            }

            @Override
            public void onClick(View v) {
                try {
                    button.setEnabled(false);
                    button.setTextColor(Color.BLACK);
                    button.setText("等待");
                    qOrderInfo.setZhuantai(1);
                    volley_QD_GET(id, button, qOrderInfo);
                }catch (Exception e){
                    Log.e("Exception", e.getMessage());
                    Toast.makeText(JieDangActivity.this,"信息有误!!!",Toast.LENGTH_SHORT).show();
                }
            }
        }
        class GDonClickListener implements View.OnClickListener{
            private String id;
            private TextView button;
            private QOrderInfo qOrderInfo;
            public GDonClickListener(QOrderInfo qOrderInfo, TextView button) {
                this.id = qOrderInfo.getOrderid();
                this.button = button;
                this.qOrderInfo = qOrderInfo;
            }

            @Override
            public void onClick(View v){
                try {
                    button.setEnabled(false);
                    button.setTextColor(Color.BLACK);
                    button.setText("等待");
                    qOrderInfo.setZhuantai(1);
                    gdOrderUtil.volley_QDGD_GET(qOrderInfo.getOrderid(), JieDangActivity.this, handler3);
                    list.remove(qOrderInfo);
                    myBaseAdapter.notifyDataSetChanged();
//                  volley_QD_GET(id, button, qOrderInfo);
                }catch (Exception e){
                    Log.e("Exception", e.getMessage());
                    Toast.makeText(JieDangActivity.this,"信息有误!!!",Toast.LENGTH_SHORT).show();
                }
            }
        }

        class ViewInfo {
            TextView tv_item_title;
            TextView tv_item_jianli;
            TextView tv_class;
            TextView tv_addr;
            TextView tv_qiangdan_button;
            TextView order_id;
            TextView tv_zhonglian;
        }
    }

    @Override
    protected void onResume() {
        try {
            volley_MSG_GET();//获取信息
            StatService.onResume(this);
            if(list!=null) {
                list.clear();
                if(list_GD!=null) {
                    list.addAll(0, list_GD);
                }
            }
            SharedPreferences sharedPreferences = getSharedPreferences("qtmsg", MODE_PRIVATE);

//        Log.e("JieDangActivity", getIntent().getBooleanExtra("info", false) + "");

            if (getIntent().getBooleanExtra("info", false)) {
//            Log.e("onResume","1111111111111111111111");
                jieDanServiceIntent = new Intent(JieDangActivity.this, JieDanService.class);
                startService(jieDanServiceIntent);
//            bindService(jieDanServiceIntent,sc,BIND_AUTO_CREATE);
            }
            sreviceisrunning = ServiceUtils.isRunning(this, "com.example.user.ddkd.service.JieDanService");
            Log.e("isRunning", sreviceisrunning + "");
//            if (list != null) {
////            list.clear();
//            }
            if (sreviceisrunning) {
                listView.setVisibility(View.VISIBLE);
                but_jiedang.setBackgroundResource(R.drawable.kaiguanann);
                //服务一开，绑定服务
                jieDanServiceIntent = new Intent(JieDangActivity.this, JieDanService.class);
                bindService(jieDanServiceIntent, sc, BIND_AUTO_CREATE);
                getGDorder();
            } else {
                listView.getEmptyView().setVisibility(View.GONE);
            }
            SharedPreferences sharedPreferences1=getSharedPreferences("config", MODE_PRIVATE);
            sharedPreferences1.edit().putBoolean("isjieDangActivityrunn", true).commit();
            super.onResume();
        }catch (Exception e){
            Log.e("Exception", e.getMessage());
            Toast.makeText(JieDangActivity.this,"信息有误!!!",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            SharedPreferences sharedPreferences=getSharedPreferences("config", MODE_PRIVATE);
            sharedPreferences.edit().putBoolean("isjieDangActivityrunn",false).commit();
//        Log.e("onPause","2222222222222222");
            if (sreviceisrunning) {
                unbindService(sc);
            }
        }catch (Exception e){
//            Log.e("Exception", e.getMessage());
            Toast.makeText(JieDangActivity.this,"信息有误",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        try {
            sp.release();
            StatService.onPause(this);
            MyApplication.getQueue().cancelAll("volley_GDMSG_GET_UTILS");
            MyApplication.getQueue().cancelAll("volley_MSG_GET");
            MyApplication.getQueue().cancelAll("volley_QD_GET");
        }catch (Exception e){
//            Log.e("Exception", e.getMessage());
            Toast.makeText(JieDangActivity.this,"信息有误!!!",Toast.LENGTH_SHORT).show();
        }
    }
    //绑定服务
    private ServiceConnection sc = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                jdBinder = (JieDanService.JDBinder) service;
                jdBinder.SendIJD(new JieDanService.IJD() {

                    @Override
                    public void Delete(List list) {
                        JieDangActivity.this.list.removeAll(list);
                        myBaseAdapter.notifyDataSetChanged();//刷新数据
                    }

                    @Override
                    public void Add(List list) {
                        JieDangActivity.this.list.addAll(0,list);
                        myBaseAdapter.notifyDataSetChanged();//刷新数据
                        play();
                    }
                });

                list = jdBinder.getMsg();
                Gson gson = new Gson();
                SharedPreferences sharedPreferences = getSharedPreferences("qtmsg", MODE_PRIVATE);
                String qt = sharedPreferences.getString("QT", "");
                sharedPreferences.edit().putString("QT", "").commit();
                List QTli = gson.fromJson(qt, new TypeToken<List<QOrderInfo>>() {
                }.getType());
                if (QTli != null) {
                    list.addAll(0,QTli);
                    jdBinder.setMsg(QTli);
                }

                myBaseAdapter.notifyDataSetChanged();
            }catch (Exception e){
//                Log.e("Exception", e.getMessage());
                Toast.makeText(JieDangActivity.this,"信息有误!!!",Toast.LENGTH_SHORT).show();
            }
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
        Log.e("volley_OrderState_GET", url);
        StringRequest request_post = new StringRequest(Request.Method.GET, url, new MyStringRequest() {
            @Override
            public void success(Object o) {
                try {
                    String ss = (String) o;
                    Log.e("volley_MSG_GET", ss);
                    Gson gson = new Gson();
                    if (ss.startsWith("{")) {
                        MainMsgInfo info = gson.fromJson((String) o, MainMsgInfo.class);
                        tv_xiuxi_huodong_now_number.setText("今天完成   " + info.getTodOrder() + "单");
                        tv_sum_number.setText("完成总数   " + info.getTotalOrder() + "单");
                        if (info.getTodTurnover() != null) {
                            DecimalFormat g = new DecimalFormat("0.00");//精确到两位小数
                            tv_xiuxi_huodong_yesterday_money.setText("今天营业额   " + g.format(Double.valueOf(info.getTodTurnover())) + "元");
                        } else {
                            tv_xiuxi_huodong_yesterday_money.setText("今天营业额   0元");
                        }
                        if (info.getEvaluate() == null) {
                            xingxing(0);
                        } else {
                            xingxing(Float.valueOf(info.getEvaluate()));
                        }
                    }
                }catch (Exception e){
                    Log.e("Exception", e.getMessage()+"");
                    Toast.makeText(JieDangActivity.this,"信息有误!!!",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void tokenouttime() {
                AutologonUtil autologonUtil = new AutologonUtil(JieDangActivity.this, handler1, null);
                autologonUtil.volley_Get_TOKEN();
            }

            @Override
            public void yidiensdfsdf() {
                Toast.makeText(JieDangActivity.this, "您的账号在其他地方被登陆，请在此登陆", Toast.LENGTH_SHORT).show();
                Exit.exit(JieDangActivity.this);
            }

        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(JieDangActivity.this, "网络连接中断", Toast.LENGTH_SHORT).show();
            }
        });
        request_post.setTag("volley_MSG_GET");
        MyApplication.getQueue().add(request_post);
    }

    //抢单数据
    private void volley_QD_GET(final String id, final TextView button, final QOrderInfo qOrderInfo) {
        preferences = getSharedPreferences("config", MODE_PRIVATE);
        String token = preferences.getString("token", "");
        String XGtoken = preferences.getString("XGtoken", "");
//        Log.e("volley_QD_GET", XGtoken);
        String url = "http://www.louxiago.com/wc/ddkd/admin.php/Order/RobOrder/orderId/" + id + "/token/" + token + "/deviceId/" + XGtoken;
        StringRequest request_post = new StringRequest(Request.Method.GET, url, new MyStringRequest() {
            @Override
            public void success(Object o) {
                String s = (String) o;
                if (!"SUCCESS".equals(s)) {
                    Toast.makeText(JieDangActivity.this, "网络异常", Toast.LENGTH_LONG).show();
                } else {
                    qOrderInfo.setZhuantai(2);
                    button.setEnabled(false);
                    button.setTextColor(Color.BLACK);
                    button.setText("已抢");
                    Toast.makeText(JieDangActivity.this, "请等待抢单信息", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void tokenouttime() {
                Log.e("volley_QD_GET", "token过时了");
                Object[] obj = {id, button, qOrderInfo};
                AutologonUtil autologonUtil = new AutologonUtil(JieDangActivity.this, handler2, obj);
                autologonUtil.volley_Get_TOKEN();
            }

            @Override
            public void yidiensdfsdf() {
                Toast.makeText(JieDangActivity.this, "您的账户已在异地登录", Toast.LENGTH_SHORT).show();
                Exit.exit(JieDangActivity.this);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(JieDangActivity.this, "网络中断", Toast.LENGTH_LONG).show();
            }
        });
        request_post.setTag("volley_QD_GET");
        MyApplication.getQueue().add(request_post);
    }

    long[] djtime = new long[2];
    @Override
    public void onBackPressed() {
        System.arraycopy(djtime, 1, djtime, 0, djtime.length - 1);
        djtime[djtime.length - 1] = SystemClock.uptimeMillis();
        if (djtime[0] >= (SystemClock.uptimeMillis() - 1000)){
//                super.onBackPressed();
            Intent i= new Intent(Intent.ACTION_MAIN);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addCategory(Intent.CATEGORY_HOME);
            startActivity(i);
//            ExitApplication.getInstance().exit();
        } else {
            Toast.makeText(this, "再按一次返回键退出应用", Toast.LENGTH_SHORT).show();
        }
    }

    private void xingxing(float i){
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
            Log.e("Exception", e.getMessage()+"");
            Toast.makeText(JieDangActivity.this,"信息有误",Toast.LENGTH_SHORT).show();
        }
    }
    //点击听单时或每个10分钟就执行获取挂单的数据
    private void getGDorder(){
        try {
            gdOrderUtil.volley_GDMSG_GET_UTILS(this,handler4);
            handler1.sendEmptyMessageDelayed(GDSX,10*60*1000);
        }catch (Exception e){
//            Log.e("Exception", e.getMessage());
            Toast.makeText(JieDangActivity.this,"信息有误",Toast.LENGTH_SHORT).show();
        }
    }

}
