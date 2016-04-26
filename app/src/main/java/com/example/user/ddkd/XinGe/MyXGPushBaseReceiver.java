package com.example.user.ddkd.XinGe;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.user.ddkd.JieDangActivity;
import com.example.user.ddkd.MyApplication;
import com.example.user.ddkd.R;
import com.example.user.ddkd.beam.QOrderInfo;
import com.example.user.ddkd.details;
import com.example.user.ddkd.utils.ServiceUtils;
import com.google.gson.Gson;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

/**
 * Created by User on 2016-04-09.
 */
public class MyXGPushBaseReceiver extends XGPushBaseReceiver {

    @Override
    public void onRegisterResult(Context context, int i, XGPushRegisterResult xgPushRegisterResult) {
        //注册结果
    }

    @Override
    public void onUnregisterResult(Context context, int i) {
        //反注册结果
    }

    @Override
    public void onSetTagResult(Context context, int i, String s) {
//设置标签结果
    }

    @Override
    public void onDeleteTagResult(Context context, int i, String s) {
//删除标签结果
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

    @Override
    public void onTextMessage(Context context, XGPushTextMessage xgPushTextMessage) {
        //开发者在前台下发消息，需要APP继承XGPushBaseReceiver重载onTextMessage方法接收，
        // 成功接收后，再根据特有业务场景进行处理。
        //"ROBRES"签单结果
        Log.e("onTextMessage", "有信息");
        Log.e("onTextMessage", xgPushTextMessage.getTitle());
        Log.e("onTextMessage", xgPushTextMessage.getContent());
        if (xgPushTextMessage.getTitle().equals("ROBRES")) {
            Gson gson = new Gson();
            Robres robres = gson.fromJson(xgPushTextMessage.getContent(), Robres.class);
            NotificationManager nm = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
            Notification.Builder builder = new Notification.Builder(context);
            builder.setContentTitle("DD快递");
            if (robres.getFlag().equals("SUCCESS")) {
                builder.setContentText("您抢的单号为：" + robres.getOrderid() + "的单抢单成功");
            } else {
                builder.setContentText("您抢的单号为:" + robres.getOrderid() + "的单抢单不成功");
            }
            Intent notificationIntent = new Intent(context, details.DingDanActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
            builder.setContentIntent(contentIntent);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            Notification notification = builder.getNotification();
            notification.flags = Notification.FLAG_AUTO_CANCEL;
            notification.defaults |= Notification.DEFAULT_SOUND;
            nm.notify(R.mipmap.ic_launcher, notification);
        } else if (ServiceUtils.isRunning(context, "com.example.user.ddkd.service.JieDanService")) {
            String s = xgPushTextMessage.getContent();
            Gson gson = new Gson();
            QOrderInfo info = gson.fromJson(s, QOrderInfo.class);
            Handler handler = MyApplication.getHandler();
            Message message = new Message();
            message.obj = info;
            message.what = MyApplication.XG_TEXT_MESSAGE;
            handler.sendMessage(message);
        } else if ("FORCEPUSH".equals(xgPushTextMessage.getTitle())) {
            NotificationManager nm = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
            Notification.Builder builder = new Notification.Builder(context);
            builder.setContentTitle("DD快递");
            builder.setContentText("有一个快递单没人抢，而且小费很高哦...亲！");
            Intent notificationIntent = new Intent(context, JieDangActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
            builder.setContentIntent(contentIntent);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            Notification notification = builder.getNotification();
            notification.flags = Notification.FLAG_AUTO_CANCEL;
            notification.defaults |= Notification.DEFAULT_SOUND;
            nm.notify(R.mipmap.ic_launcher, notification);
        }
    }

    @Override
    public void onNotifactionClickedResult(Context context, XGPushClickedResult xgPushClickedResult) {
        //  通知被打开触发的结果
        Log.e("MyXGPushBaseReceiver", "onNotifactionClickedResult");
    }

    @Override
    public void onNotifactionShowedResult(Context context, XGPushShowedResult xgPushShowedResult) {
        //通知被展示触发的结果，可以在此保存APP收到的通知
        Log.e("MyXGPushBaseReceiver", "onNotifactionShowedResult");
    }
}
