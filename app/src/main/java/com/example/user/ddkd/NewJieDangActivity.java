package com.example.user.ddkd;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.ddkd.Dao.QOrderDao;
import com.example.user.ddkd.Presenter.IJieDanPresenter;
import com.example.user.ddkd.Presenter.JieDanPresenterImpl;
import com.example.user.ddkd.View.IJieDanView;
import com.example.user.ddkd.beam.MainMsgInfo;
import com.example.user.ddkd.beam.QOrderInfo;
import com.example.user.ddkd.service.JieDanService;
import com.example.user.ddkd.utils.GDOrderUtil;
import com.example.user.ddkd.utils.ServiceUtils;
import com.example.user.ddkd.utils.SlidingUtil;
import com.example.user.ddkd.utils.UserInfoUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.example.user.ddkd.ExitApplication.getInstance;

public class NewJieDangActivity extends BaseActivity implements View.OnClickListener, IJieDanView {
    private final static int GDSX = 11;//挂单刷新
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

    //接单的总单数
    private TextView tv_sum_number;
    //昨天的营业额
    private TextView tv_xiuxi_huodong_yesterday_money;

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
    public boolean GDshuju = false;
    private IJieDanPresenter iJieDanPresenter;
    //绑定服务
    private ServiceConnection sc = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                jdBinder = (JieDanService.JDBinder) service;
                jdBinder.SendIJD(new JieDanService.IJD() {
                    @Override
                    public void Delete(List list) {
                        NewJieDangActivity.this.list.removeAll(list);
                        myBaseAdapter.notifyDataSetChanged();//刷新数据
                    }
                    @Override
                    public void Add(List list) {
                        NewJieDangActivity.this.list.addAll(0, list);
                        myBaseAdapter.notifyDataSetChanged();//刷新数据
                        play();
                    }
                });
                list = jdBinder.getMsg();
                QOrderDao qOrderDao=new QOrderDao(getApplicationContext());
                List<String> ss=qOrderDao.query((System.currentTimeMillis() - 30 * 1000) + "");
                if(ss!=null&&ss.size()!=0) {
                    Gson gson = new Gson();
                    List<QOrderInfo> QTli = new ArrayList<>();
                    for (String s : ss) {
                        QTli.add(gson.fromJson(s, QOrderInfo.class));
                    }
                    if (QTli != null) {
                        list.addAll(QTli);
                        jdBinder.setMsg(QTli);
                    }
                }
                myBaseAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                Toast.makeText(NewJieDangActivity.this, "信息有误!!!", Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jiedang_activity);
        iJieDanPresenter = new JieDanPresenterImpl(this);
//        slidingUtil = (SlidingUtil) findViewById(R.id.it_menu);
//        userInfoUtils = new UserInfoUtils(slidingUtil, NewJieDangActivity.this);
        initSound();//初始化声音数据
        list = new ArrayList<QOrderInfo>();
        list_GD = new ArrayList<QOrderInfo>();
        textView = (TextView) findViewById(R.id.personinfo);
        textView.setOnClickListener(this);
        listView = (ListView) findViewById(R.id.lv_jiedang);
        ll_ddzhinang = (LinearLayout) findViewById(R.id.ll_ddzhinang);
        ll_jianlihuodong = (LinearLayout) findViewById(R.id.ll_jianlihuodong);
        tv_to_dingdang = (TextView) findViewById(R.id.tv_to_dingdang);
        but_jiedang = (TextView) findViewById(R.id.but_jiedang);
        tv_xiuxi_huodong_now_number = (TextView) findViewById(R.id.tv_xiuxi_huodong_now_number);
        tv_sum_number = (TextView) findViewById(R.id.tv_sum_number);
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

        xx1 = (ImageView) findViewById(R.id.xx1);
        xx2 = (ImageView) findViewById(R.id.xx2);
        xx3 = (ImageView) findViewById(R.id.xx3);
        xx4 = (ImageView) findViewById(R.id.xx4);
        xx5 = (ImageView) findViewById(R.id.xx5);

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
        SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        boolean bb = sharedPreferences.getBoolean("qiandan1", true);
        if (bb) {
            jieDanServiceIntent = new Intent(getApplicationContext(), JieDanService.class);
            startService(jieDanServiceIntent);
            listView.setVisibility(View.VISIBLE);
            but_jiedang.setBackgroundResource(R.drawable.kaiguanann);
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putBoolean("qiandan1", false);
            edit.commit();
            GDshuju = true;
        }
    }

    private void initSound() {
        sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        soundid = sp.load(this, R.raw.ddkd, 1);
    }

    private void play() {//声音开始
        try {
            SharedPreferences spf = getSharedPreferences("config", MODE_PRIVATE);
            boolean b = spf.getBoolean("voice", true);
            if (b) {
                sp.play(soundid, 1.0f, 0.3f, 0, 0, 1);
            }
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
            Toast.makeText(NewJieDangActivity.this, "信息有误!!!", Toast.LENGTH_SHORT).show();
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
                    if (!sreviceisrunning) {
                        listView.getEmptyView().setVisibility(View.VISIBLE);
                        sreviceisrunning = true;
                        listView.setVisibility(View.VISIBLE);
                        but_jiedang.setBackgroundResource(R.drawable.kaiguanann);
                        jieDanServiceIntent = new Intent(getApplicationContext(), JieDanService.class);
                        startService(jieDanServiceIntent);
                        bindService(jieDanServiceIntent, sc, BIND_AUTO_CREATE);
                        myBaseAdapter.notifyDataSetChanged();
                        iJieDanPresenter.getBespeakOrder(getToken());

                    } else {
                        MyApplication.getQueue().cancelAll("volley_GDMSG_GET_UTILS");
                        listView.getEmptyView().setVisibility(View.GONE);
                        sreviceisrunning = false;
                        unbindService(sc);
                        jieDanServiceIntent = new Intent(getApplicationContext(), JieDanService.class);
                        stopService(jieDanServiceIntent);
                        listView.setVisibility(View.GONE);
                        but_jiedang.setBackgroundResource(R.drawable.kaiguan);
                    }
                    break;
                case R.id.personinfo://进入用户信息界面
                    slidingUtil.changeMenu();
                    break;
            }
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
            Toast.makeText(NewJieDangActivity.this, "信息有误!!!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        try {
            iJieDanPresenter.CountOrder(getToken());
            if (list != null) {
                list.clear();
                if (list_GD != null) {
                    list.addAll(0, list_GD);
                }
            }
            if (getIntent().getBooleanExtra("info", false)) {
                jieDanServiceIntent = new Intent(NewJieDangActivity.this, JieDanService.class);
                startService(jieDanServiceIntent);
            }
            sreviceisrunning = ServiceUtils.isRunning(this, "com.example.user.ddkd.service.JieDanService");
            if (sreviceisrunning) {
                listView.setVisibility(View.VISIBLE);
                but_jiedang.setBackgroundResource(R.drawable.kaiguanann);
                //服务一开，绑定服务
                jieDanServiceIntent = new Intent(NewJieDangActivity.this, JieDanService.class);
                bindService(jieDanServiceIntent, sc, BIND_AUTO_CREATE);
            } else {
                listView.getEmptyView().setVisibility(View.GONE);
            }
            SharedPreferences sharedPreferences1 = getSharedPreferences("config", MODE_PRIVATE);
            sharedPreferences1.edit().putBoolean("isjieDangActivityrunn", true).commit();
            super.onResume();
        } catch (Exception e) {
            Toast.makeText(NewJieDangActivity.this, "信息有误!!!", Toast.LENGTH_SHORT).show();
        }
    }

    @NonNull
    private String getToken() {
        preferences = getSharedPreferences("config", MODE_PRIVATE);
        return preferences.getString("token", "");
    }

    @NonNull
    private String getXGtoken() {
        preferences = getSharedPreferences("config", MODE_PRIVATE);
        return preferences.getString("XGtoken", "");
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
            sharedPreferences.edit().putBoolean("isjieDangActivityrunn", false).commit();
            if (sreviceisrunning) {
                unbindService(sc);
            }
        } catch (Exception e) {
            Toast.makeText(NewJieDangActivity.this, "信息有误", Toast.LENGTH_SHORT).show();
        }
    }

    private void xingxing(float i) {
        try {
            int ii = (int) (i + 0.5);
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
        } catch (Exception e) {
            Log.e("Exception", e.getMessage() + "");
            Toast.makeText(NewJieDangActivity.this, "信息有误", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showToast(String content) {
        Toast.makeText(NewJieDangActivity.this, content, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setMainMsgInfo(MainMsgInfo info) {
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

    @Override
    public void getGDorder() {
        iJieDanPresenter.getBespeakOrder(getToken());
    }

    @Override
    public void setGDListInfo(List<QOrderInfo> list) {
        this.list.removeAll(list_GD);
        list_GD = list;
        this.list.addAll(list_GD);
        myBaseAdapter.notifyDataSetChanged();
    }

    @Override
    public void setEndListAndItemViewState(int state,int position) {
        if(state==2) {
            if (list.size() > position) {
                list.get(position).setZhuantai(2);
            }
            if (listView != null) {
                int First = listView.getFirstVisiblePosition();
                int Last = listView.getLastVisiblePosition();
                if (position <= First && position >= Last) {
                    View view = listView.getChildAt(position - First);
                    if (view != null) {
                        MyBaseAdapter.ViewInfo viewInfo = (MyBaseAdapter.ViewInfo) view.getTag();
                        viewInfo.tv_qiangdan_button.setEnabled(false);
                        viewInfo.tv_qiangdan_button.setTextColor(Color.BLACK);
                        viewInfo.tv_qiangdan_button.setText("已抢");
                    }
                }
            }
            showToast("请等待抢单信息");
        }else{
            if (list.size() > position) {
                list.get(position).setZhuantai(0);
            }
            if (listView != null) {
                int First = listView.getFirstVisiblePosition();
                int Last = listView.getLastVisiblePosition();
                if (position <= First && position >= Last) {
                    View view = listView.getChildAt(position - First);
                    if (view != null) {
                        MyBaseAdapter.ViewInfo viewInfo = (MyBaseAdapter.ViewInfo) view.getTag();
                        viewInfo.tv_qiangdan_button.setEnabled(true);
                        viewInfo.tv_qiangdan_button.setTextColor(Color.BLACK);
                        viewInfo.tv_qiangdan_button.setText("抢单");
                    }
                }
            }
            showToast("网络异常");
        }
    }

    @Override
    public void setStartListAndItemViewState(int position) {
        if(list.size()>position){
            list.get(position).setZhuantai(1);
        }
        if(listView!=null){
            int First=listView.getFirstVisiblePosition();
            int Last=listView.getLastVisiblePosition();
            if(position<=First&&position>=Last){
                View view=listView.getChildAt(position-First);
                if(view!=null){
                    MyBaseAdapter.ViewInfo viewInfo= (MyBaseAdapter.ViewInfo) view.getTag();
                    viewInfo.tv_qiangdan_button.setEnabled(false);
                    viewInfo.tv_qiangdan_button.setTextColor(Color.BLACK);
                    viewInfo.tv_qiangdan_button.setText("等待");
                }
            }
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
                    view = View.inflate(NewJieDangActivity.this, R.layout.dialog_view, null);
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
                    float f = Float.valueOf(qOrderInfo.getPrice()) + Float.valueOf(qOrderInfo.getTip());
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
                if (qOrderInfo.getBespeak() != null) {
                    if (!"0".equals(qOrderInfo.getBespeak())) {
                        if (qOrderInfo.getOrderid() != null) {
                            viewInfo.order_id.setText("(挂单)单号:" + qOrderInfo.getOrderid());
                        }
                        viewInfo.tv_qiangdan_button.setOnClickListener(new GDonClickListener(position, qOrderInfo, viewInfo.tv_qiangdan_button));
                    } else {
                        if (qOrderInfo.getOrderid() != null) {
                            viewInfo.order_id.setText("单号:" + qOrderInfo.getOrderid());
                        }
                        viewInfo.tv_qiangdan_button.setOnClickListener(new QDonClickListener(position, qOrderInfo));
                    }
                } else {
                    if (qOrderInfo.getOrderid() != null) {
                        viewInfo.order_id.setText("单号:" + qOrderInfo.getOrderid());
                    }
                    viewInfo.tv_qiangdan_button.setOnClickListener(new QDonClickListener(position, qOrderInfo));
                }
                return view;
            } catch (Exception e) {
                Toast.makeText(NewJieDangActivity.this, "信息有误!!!", Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        class QDonClickListener implements View.OnClickListener {
            private String id;
            private QOrderInfo qOrderInfo;
            private int position;

            public QDonClickListener(int position, QOrderInfo qOrderInfo) {
                this.id = qOrderInfo.getOrderid();
                this.qOrderInfo = qOrderInfo;
                this.position = position;
            }

            @Override
            public void onClick(View v) {
                try {
                    iJieDanPresenter.RobOrder(position,getXGtoken(),getToken(),id);
                } catch (Exception e) {
                    Toast.makeText(NewJieDangActivity.this, "信息有误!!!", Toast.LENGTH_SHORT).show();
                }
            }
        }

        class GDonClickListener implements View.OnClickListener {
            private TextView button;
            private QOrderInfo qOrderInfo;
            private int position;

            public GDonClickListener(int position, QOrderInfo qOrderInfo, TextView button) {
                this.button = button;
                this.qOrderInfo = qOrderInfo;
                this.position = position;
            }

            @Override
            public void onClick(View v) {
                try {
                    button.setEnabled(false);
                    button.setTextColor(Color.BLACK);
                    button.setText("等待");
                    qOrderInfo.setZhuantai(1);
                    iJieDanPresenter.RobBespeakOrder(position, getToken(), qOrderInfo.getOrderid());
                    list.remove(qOrderInfo);
                    myBaseAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    Toast.makeText(NewJieDangActivity.this, "信息有误!!!", Toast.LENGTH_SHORT).show();
                }
            }
        }

        protected class ViewInfo {
            TextView tv_item_title;
            TextView tv_item_jianli;
            TextView tv_class;
            TextView tv_addr;
            TextView tv_qiangdan_button;
            TextView order_id;
            TextView tv_zhonglian;
        }
    }

}
