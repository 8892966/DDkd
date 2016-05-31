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
import android.widget.Toast;

import com.example.user.ddkd.JieDangActivity;
import com.example.user.ddkd.MyApplication;
import com.example.user.ddkd.R;
import com.example.user.ddkd.beam.QOrderInfo;
import com.example.user.ddkd.utils.Exit;

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
    private final static int DELECT=10;
    private Handler handler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                switch (msg.what) {
                    case MyApplication.XG_TEXT_USERCANCEL:
                        USERCANCEL(msg.arg1);
                        break;
                    case MyApplication.XG_TEXT_MESSAGE:
                        if (o[o.length - 1] == null) {
                            o[o.length - 1] = new ArrayList<>();
                        }
                        o[o.length - 1].add((QOrderInfo) msg.obj);
                        break;
                    case DELECT:
                        ijd.Delete((List<QOrderInfo>) msg.obj);
                        break;
                }
            }catch (Exception e){
                Log.e("Exception", e.getMessage()+"");
                Toast.makeText(JieDanService.this,"信息有误!!!",Toast.LENGTH_SHORT).show();
            }
        }
    };
    private Handler handler2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                switch (msg.what) {
                    case 2:
                        if (o[0] != null) {
                            ijd.Delete(o[0]);
                            int i = 0;
                            for (QOrderInfo xgp : o[0]) {
//                                Log.e("JieDanService", xgp.toString() + 0 + (i++));
                            }
                        } else {
//                            Log.e("JieDanService", "时间还没到");
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
                                builder.setTicker("有快递单抢啦！");
                                builder.setContentText("有" + o[o.length - 1].size() + "个快递单单可以抢！");
                                Intent notificationIntent = new Intent(JieDanService.this, JieDangActivity.class);
//                              notificationIntent.putExtra("msg","FROM_QT");//标志强推信息
                                PendingIntent contentIntent = PendingIntent.getActivity(JieDanService.this, 0, notificationIntent, 0);
                                builder.setContentIntent(contentIntent);
                                builder.setSmallIcon(R.mipmap.headimage);
                                Notification notification = builder.getNotification();
                                notification.flags = Notification.FLAG_AUTO_CANCEL;
                                notification.defaults |= Notification.DEFAULT_SOUND;
                                nm.notify(R.mipmap.ic_launcher, notification);
                            }

                        } else {

                        }
                        System.arraycopy(o, 1, o, 0, o.length - 1);
                        o[o.length - 1] = null;
                        if (b) {
                            handler2.sendEmptyMessageDelayed(2, 1000);
                        }
                        break;
                }
            }catch (Exception e){
                Log.e("Exception", e.getMessage()+"");
                Toast.makeText(JieDanService.this,"信息有误!!!",Toast.LENGTH_SHORT).show();
            }
        }
    };

    public JieDanService() {
    }
    @Override
    public IBinder onBind(Intent intent) {

        return new JDBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("DD快递");
        builder.setContentText("DD快递听单中...");
        builder.setTicker("开始听单。");
        Intent notificationIntent = new Intent(this, JieDangActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, (int) (Math.random() * 100000), notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        builder.setSmallIcon(R.mipmap.headimage);
        Notification notification = builder.getNotification();
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        startForeground(1, notification);

        flags = this.START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        Log.e("JieDanService",this.toString());
        super.onCreate();
        o = new List[30];
        b = true;
        MyApplication.setHandler(handler1);
        handler2.sendEmptyMessageDelayed(2,1000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
        Log.e("JieDanService", this.toString());
        b = false;
    }

    public class JDBinder extends Binder {
        public void SendIJD(IJD ijd) {
            JieDanService.this.ijd = ijd;
        }
        public void setMsg(List<QOrderInfo> msg){
            try {
                for (QOrderInfo q : msg) {
                    long time = System.currentTimeMillis() - Long.valueOf(q.getOrderTime());
                    int t = (int) ((30 * 1000 - time)) / 1000;
                    if (t > 0) {
                        if (o[t] == null) {
                            o[t] = new ArrayList<>();
                        }
                        o[t].add(q);
                    }
                }
            }catch (Exception e){
                Toast.makeText(JieDanService.this,"信息有误!!!",Toast.LENGTH_SHORT).show();
            }
        }
        public List getMsg() {
            try {
                List s = new ArrayList();
                for (int i = o.length - 1; i >= 0; i--) {
                    if (o[i] != null) {
                        s.addAll(o[i]);
                    }
                }
                return s;
            }catch (Exception e){
                Toast.makeText(JieDanService.this,"信息有误!!!",Toast.LENGTH_SHORT).show();
            }
            return null;
        }
    }

    public interface IJD {
        public void Delete(List list);
        public void Add(List list);
    }

    private void USERCANCEL(final int id){
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    List<QOrderInfo> list = new ArrayList<QOrderInfo>();
                    for (List<QOrderInfo> oo : o) {
                        if (oo != null) {
                            for (QOrderInfo info : oo) {
                                if (Integer.valueOf(info.getOrderid()) == id) {
                                    list.add(info);
                                }
                            }
                            oo.removeAll(list);
                        }
                    }
                    Message message = new Message();
                    message.obj = list;
                    message.what = DELECT;
                    handler1.sendMessage(message);
                }
            }).start();
        }catch (Exception e){
            Log.e("Exception", e.getMessage()+"");
            Toast.makeText(JieDanService.this,"信息有误!!!",Toast.LENGTH_SHORT).show();
        }
    }
}
