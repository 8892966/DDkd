package com.example.user.ddkd.service;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.example.user.ddkd.JieDangActivity;
import com.example.user.ddkd.MyApplication;
import com.example.user.ddkd.R;
import com.example.user.ddkd.beam.QOrderInfo;

import java.util.ArrayList;
import java.util.List;

public class JieDanService extends Service {
    //页面管理，用于判断抢单页面是否在前台
    ActivityManager am;

    private IJD ijd = new IJD() {//初始化借口如何没有新的接口传进来就用这个，什么都不做
        @Override
        public void Delete(List list) {
        }

        @Override
        public void Add(List list) {
        }
    };
    private List<QOrderInfo>[] o;
    private boolean b = true;
    private Handler handler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MyApplication.XG_TEXT_MESSAGE:
                    Log.e("JieDanService", "添加数据");
                    if (o[o.length - 1] == null) {
                        o[o.length - 1] = new ArrayList<>();
                    }
                    o[o.length - 1].add((QOrderInfo) msg.obj);
                    break;
            }
        }
    };
    private Handler handler2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 2:
                    if (o[0] != null) {
                        ijd.Delete(o[0]);
                        int i = 0;
                        for (QOrderInfo xgp : o[0]) {
                            Log.e("JieDanService", xgp.toString() + 0 + (i++));
                        }
                    } else {
                        Log.e("JieDanService", "时间还没到");
                    }
                    if (o[o.length - 1] != null) {
                        //判断抢单页面是否在前台
                        am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                        List<ActivityManager.RunningTaskInfo> infos = am.getRunningTasks(100);
                        if ("com.example.user.ddkd.JieDangActivity".equals(infos.get(0).topActivity.getClassName())) {
                            ijd.Add(o[o.length - 1]);
                        } else {
                            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                            Notification.Builder builder = new Notification.Builder(JieDanService.this);
                            builder.setContentTitle("有快递单抢啦！");
                            builder.setContentText("有"+o[o.length - 1].size()+"个快递单单可以抢！");
                            Intent notificationIntent = new Intent(JieDanService.this,JieDangActivity.class);
//                          notificationIntent.putExtra("msg","FROM_QT");//标志强推信息
                            PendingIntent contentIntent = PendingIntent.getActivity(JieDanService.this, 0, notificationIntent, 0);
                            builder.setContentIntent(contentIntent);
                            builder.setSmallIcon(R.mipmap.ic_launcher);
                            Notification notification = builder.getNotification();
                            notification.flags=Notification.FLAG_AUTO_CANCEL;
                            notification.defaults|= Notification.DEFAULT_SOUND;
                            nm.notify(R.mipmap.ic_launcher, notification);
                        }
                        int i = 0;
                        for (QOrderInfo xgp : o[o.length - 1]) {
                            Log.e("JieDanService", xgp.toString() + o.length + (i++));
                        }
                        Log.e("JieDanService", "显示了数据");
                    } else {
                        Log.e("JieDanService", "没有信鸽信息");
                    }
                    System.arraycopy(o, 1, o, 0, o.length - 1);
                    o[o.length - 1] = null;
                    if (b) {
                        handler2.sendEmptyMessageDelayed(2,1000);
                    }
                    break;
            }
        }
    };

    public JieDanService() {
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
        return new JDBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        o = new List[30];
        MyApplication.setHandler(handler1);
        handler2.sendEmptyMessageDelayed(2,1000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("JieDanService","JieDanServiceonDestroy");
        b = false;
    }

    public class JDBinder extends Binder {
        public void SendIJD(IJD ijd) {
            JieDanService.this.ijd = ijd;
        }

        public List getMsg() {
            List s = new ArrayList();
            for (int i = o.length - 1; i >= 0; i--) {
                if (o[i] != null) {
                    s.addAll(o[i]);
                }
            }
            return s;
        }
    }

    public interface IJD {
        public void Delete(List list);
        public void Add(List list);
    }
}
