package com.example.user.ddkd.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.example.user.ddkd.Dao.QOrderDao;
import com.example.user.ddkd.DingDanNewActivity;
import com.example.user.ddkd.JieDangActivity;
import com.example.user.ddkd.R;
import com.example.user.ddkd.beam.QOrderInfo;
import com.google.gson.Gson;


public class XGReceiverService extends IntentService {
    private SharedPreferences preferences;
    private Gson gson;
    private IJD ijd;

    public XGReceiverService() {
        super("XGReceiverService");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new JDBinder();
    }

    @Override
    public void onCreate() {
        Log.e("XGReceiverService","onCreate");
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.e("XGReceiverService","onDestroy");
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String Content = intent.getExtras().getString("Content");
        String Title = intent.getExtras().getString("Title");
        preferences=getSharedPreferences("config", Context.MODE_PRIVATE);
        if(Content!=null&&Title!=null) {
//            try {
                QOrderDao qOrderDao = new QOrderDao(getApplicationContext());
                gson = new Gson();
                QOrderInfo info = gson.fromJson(Content, QOrderInfo.class);
                if (Title.equals("ROBRES")) {
                    gson = new Gson();
                    Robres robres = gson.fromJson(Content, Robres.class);
                    NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    Notification.Builder builder = new Notification.Builder(getApplicationContext());
                    builder.setContentTitle("DD快递");
                    if (robres.getFlag().equals("SUCCESS")){
                        builder.setTicker("您抢的单号为：" + robres.getOrderid() + "的单抢单成功");
                        builder.setContentText("您抢的单号为：" + robres.getOrderid() + "的单抢单成功");
                        if(ijd!=null){
                            ijd.showtoast("您抢的单号为：" + robres.getOrderid() + "的单抢单成功");
                        }
                    } else {
                        builder.setTicker("您抢的单号为:" + robres.getOrderid() + "的单抢单不成功");
                        if(ijd!=null) {
                            ijd.showtoast("您抢的单号为：" + robres.getOrderid() + "的单抢单不成功");
                        }
                        builder.setContentText("您抢的单号为:" + robres.getOrderid() + "的单抢单不成功");
                    }
                    Intent notificationIntent = new Intent(this, DingDanNewActivity.class);
                    PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
                    builder.setContentIntent(contentIntent);
                    builder.setSmallIcon(R.mipmap.headimage);
                    Notification notification = builder.getNotification();
                    notification.flags = Notification.FLAG_AUTO_CANCEL;
                    notification.defaults |= Notification.DEFAULT_SOUND;
                    nm.notify(R.mipmap.headimage, notification);
                    if (preferences.getBoolean("isjieDangActivityrunn", false)) {
                        if(ijd!=null) {
                            ijd.Delete(info);
                        }
                    }
                    qOrderDao.delete(robres.getOrderid());
                } else if (Title.equals("USERCANCEL")) {
                    int i = Content.indexOf("[");
                    int ii = Content.indexOf("]");
                    String id = Content.substring(i + 1, ii);
                    if (preferences.getBoolean("isjieDangActivityrunn", false)) {
                        if(ijd!=null) {
                            ijd.Delete(info);
                        }
                    }
                    qOrderDao.delete(id);
                } else {
                    preferences = getSharedPreferences("config", Context.MODE_PRIVATE);
                    if (preferences.getBoolean("isjieDangActivityrunn", false)) {
                        if(ijd!=null) {
                            ijd.Add(info);
                        }
                    } else {
                        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        Notification.Builder builder = new Notification.Builder(getApplicationContext());
                        builder.setContentTitle("有快递单抢啦！");
                        builder.setTicker("有快递单抢啦！");
                        builder.setContentText("有单可以抢啦！还等在什么！");
                        Intent notificationIntent = new Intent(this, JieDangActivity.class);
                        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
                        builder.setContentIntent(contentIntent);
                        builder.setSmallIcon(R.mipmap.headimage);
                        Notification notification = builder.getNotification();
                        notification.flags = Notification.FLAG_AUTO_CANCEL;
                        notification.defaults |= Notification.DEFAULT_SOUND;
                        nm.notify(R.mipmap.ic_launcher, notification);
                    }
                    qOrderDao.insert(info.getOrderid(), System.currentTimeMillis() + "", Content);
                }
//            } catch (Exception e) {
//                Toast.makeText(getApplicationContext(), "信息有误!!!", Toast.LENGTH_SHORT).show();
//            }
        }
    }

    class Robres {
        private String orderid;
        private String flag;

        public String getOrderid() {
            return orderid;
        }

        public void setOrderid(String orderid) {
            this.orderid = orderid;
        }

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }
    }

    public class JDBinder extends Binder {
        public void SendIJD(IJD ijd) {
            XGReceiverService.this.ijd = ijd;
        }
        public void DelIJD(){
            XGReceiverService.this.ijd=null;
        }
    }

    public interface IJD {
        void Delete(QOrderInfo qOrderInfo);

        void Add(QOrderInfo qOrderInfo);

        void showtoast(String s);
    }
}
