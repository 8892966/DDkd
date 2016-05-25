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
 * Created by User on 2016-04-09.
 */
public class MyXGPushBaseReceiver extends XGPushBaseReceiver {

    @Override
    public void onRegisterResult(Context context, int i, XGPushRegisterResult xgPushRegisterResult) {
        //注册结果
//        Toast.makeText(context,xgPushRegisterResult.getToken(),Toast.LENGTH_LONG).show();
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
//        Log.e("onTextMessage", "有信息");
//        Log.e("onTextMessage", xgPushTextMessage.getTitle());
//        Log.e("onTextMessage", xgPushTextMessage.getContent());
//        SharedPreferences sharedPreferences1=context.getSharedPreferences("config",context.MODE_PRIVATE);
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
//            else if ("FORCEPUSH".equals(xgPushTextMessage.getTitle())) {
////                Log.e("FORCEPUSH", xgPushTextMessage.getContent());
//                String s = xgPushTextMessage.getContent();
//                Gson gson = new Gson();
//
//                QOrderInfo info = gson.fromJson(s, QOrderInfo.class);
//                info.setOrderTime(System.currentTimeMillis() + "");
//
//                SharedPreferences sharedPreferences = context.getSharedPreferences("qtmsg", context.MODE_PRIVATE);
//                String qt = sharedPreferences.getString("QT", "");
//                List list = gson.fromJson(qt, new TypeToken<List<OrderInfo>>() {
//                }.getType());
//                if (list == null) {
//                    list = new ArrayList();
//                }
//                list.add(info);
//                String QT = gson.toJson(list);
//                SharedPreferences.Editor edit = sharedPreferences.edit();
//                edit.putString("QT", QT);
//                edit.commit();
//
//                NotificationManager nm = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
//                Notification.Builder builder = new Notification.Builder(context);
//                builder.setContentTitle("DD快递");
//                builder.setContentText("有一个快递单没人抢，而且小费很高哦...亲！");
//                Intent notificationIntent = new Intent(context, JieDangActivity.class);
//                notificationIntent.putExtra("info", true);
//                PendingIntent contentIntent = PendingIntent.getActivity(context, (int) (Math.random() * 100000), notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//                builder.setContentIntent(contentIntent);
//                builder.setSmallIcon(R.mipmap.headimage);
//                Notification notification = builder.getNotification();
//                notification.flags = Notification.FLAG_AUTO_CANCEL;
//                notification.defaults |= Notification.DEFAULT_SOUND;
//                nm.notify(R.mipmap.headimage, notification);
//            }
        }catch (Exception e){
            Log.e("Exception", e.getMessage() + "");
            Toast.makeText(context,"信息有误!!!",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNotifactionClickedResult(Context context, XGPushClickedResult xgPushClickedResult) {
        //  通知被打开触发的结果
//        Log.e("MyXGPushBaseReceiver", "onNotifactionClickedResult");
    }

    @Override
    public void onNotifactionShowedResult(Context context, XGPushShowedResult xgPushShowedResult) {
        //通知被展示触发的结果，可以在此保存APP收到的通知
//        Log.e("MyXGPushBaseReceiver", "onNotifactionShowedResult");
    }
}
