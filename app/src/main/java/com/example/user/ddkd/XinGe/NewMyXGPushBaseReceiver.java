package com.example.user.ddkd.XinGe;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.user.ddkd.DingDanNewActivity;
import com.example.user.ddkd.MyApplication;
import com.example.user.ddkd.R;
import com.example.user.ddkd.beam.QOrderInfo;
import com.example.user.ddkd.utils.ServiceUtils;
import com.google.gson.Gson;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

/**
 * Created by User on 2016-05-27.
 */
public class NewMyXGPushBaseReceiver extends XGPushBaseReceiver {

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
    public void onRegisterResult(Context context, int i, XGPushRegisterResult xgPushRegisterResult) {

    }

    @Override
    public void onUnregisterResult(Context context, int i) {

    }

    @Override
    public void onSetTagResult(Context context, int i, String s) {

    }

    @Override
    public void onDeleteTagResult(Context context, int i, String s) {

    }
    @Override
    public void onTextMessage(Context context, XGPushTextMessage xgPushTextMessage) {
        try {
            if (xgPushTextMessage.getTitle().equals("ROBRES")) {
                Gson gson = new Gson();
                Robres robres = gson.fromJson(xgPushTextMessage.getContent(), Robres.class);
                NotificationManager nm = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
                Notification.Builder builder = new Notification.Builder(context);
                builder.setContentTitle("DD快递");
                if (robres.getFlag().equals("SUCCESS")) {
                    builder.setTicker("您抢的单号为：" + robres.getOrderid() + "的单抢单成功");
                    builder.setContentText("您抢的单号为：" + robres.getOrderid() + "的单抢单成功");
                    Toast.makeText(context, "您抢的单号为：" + robres.getOrderid() + "的单抢单成功", Toast.LENGTH_SHORT).show();
                } else {
                    builder.setTicker("您抢的单号为:" + robres.getOrderid() + "的单抢单不成功");
                    Toast.makeText(context, "您抢的单号为：" + robres.getOrderid() + "的单抢单不成功", Toast.LENGTH_SHORT).show();
                    builder.setContentText("您抢的单号为:" + robres.getOrderid() + "的单抢单不成功");
                }
                Intent notificationIntent = new Intent(context, DingDanNewActivity.class);
                PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
                builder.setContentIntent(contentIntent);
                builder.setSmallIcon(R.mipmap.headimage);
                Notification notification = builder.getNotification();
                notification.flags = Notification.FLAG_AUTO_CANCEL;
                notification.defaults |= Notification.DEFAULT_SOUND;
                nm.notify(R.mipmap.headimage, notification);
            } else if (ServiceUtils.isRunning(context, "com.example.user.ddkd.service.JieDanService")) {
                if (xgPushTextMessage.getTitle().equals("USERCANCEL")) {
                    int i = xgPushTextMessage.getContent().indexOf("[");
                    int ii = xgPushTextMessage.getContent().indexOf("]");
                    String id = xgPushTextMessage.getContent().substring(i + 1, ii);
                    NotificationManager nm = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
                    Notification.Builder builder = new Notification.Builder(context);
                    builder.setContentTitle("DD快递");
                    builder.setContentText("单号" + id + "已取消");
                    Toast.makeText(context, "单号" + id + "已取消", Toast.LENGTH_SHORT).show();
                    builder.setSmallIcon(R.mipmap.headimage);
                    Notification notification = builder.getNotification();
                    notification.flags = Notification.FLAG_AUTO_CANCEL;
                    notification.defaults |= Notification.DEFAULT_SOUND;
                    nm.notify(R.mipmap.headimage, notification);
                    Handler handler = MyApplication.getHandler();
                    Message message = new Message();
                    message.arg1 = Integer.valueOf(id);
                    message.what = MyApplication.XG_TEXT_USERCANCEL;
                    handler.sendMessage(message);
                } else {
                    String s = xgPushTextMessage.getContent();
                    Gson gson = new Gson();
                    QOrderInfo info = gson.fromJson(s, QOrderInfo.class);
                    Handler handler = MyApplication.getHandler();
                    Message message = new Message();
                    message.obj = info;
                    message.what = MyApplication.XG_TEXT_MESSAGE;
                    handler.sendMessage(message);
                }
            }
        }catch (Exception e){
            Toast.makeText(context,"信息有误!!!",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNotifactionClickedResult(Context context, XGPushClickedResult xgPushClickedResult) {

    }

    @Override
    public void onNotifactionShowedResult(Context context, XGPushShowedResult xgPushShowedResult) {

    }
}
