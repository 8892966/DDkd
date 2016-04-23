package com.example.user.ddkd.XinGe;


import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.user.ddkd.JieDangActivity;
import com.example.user.ddkd.MyApplication;
import com.example.user.ddkd.R;
import com.example.user.ddkd.beam.MainMsgInfo;
import com.example.user.ddkd.beam.QOrderInfo;
import com.example.user.ddkd.service.JieDanService;
import com.example.user.ddkd.utils.ServiceUtils;
import com.google.gson.Gson;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

import java.util.List;

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

    @Override
    public void onTextMessage(Context context, XGPushTextMessage xgPushTextMessage) {
        //开发者在前台下发消息，需要APP继承XGPushBaseReceiver重载onTextMessage方法接收，
        // 成功接收后，再根据特有业务场景进行处理。
            //"ROBRES"签单结果
        if(xgPushTextMessage.getTitle().equals("ROBRES")){
            Gson gson = new Gson();
            Log.e("ROBRES", xgPushTextMessage.getContent());

        } else if (ServiceUtils.isRunning(context,"com.example.user.ddkd.service.JieDanService")) {
            String s = xgPushTextMessage.getContent();
            Log.e("onTextMessage",s);
            Gson gson = new Gson();
            QOrderInfo info = gson.fromJson(s,QOrderInfo.class);
            Handler handler = MyApplication.getHandler();
            Message message = new Message();
            message.obj = info;
            message.what = MyApplication.XG_TEXT_MESSAGE;
            handler.sendMessage(message);
        } else if("".equals(xgPushTextMessage.getTitle())){
            NotificationManager nm = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
            Notification.Builder builder = new Notification.Builder(context);
            builder.setContentTitle("DD快递");
            builder.setContentText("有一个单没人抢，而且小费很高哦...亲！");
            Intent notificationIntent = new Intent(context,JieDangActivity.class);
//            notificationIntent.putExtra("msg","FROM_QT");//标志强推信息
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
            builder.setContentIntent(contentIntent);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            Notification notification = builder.getNotification();
            notification.flags=Notification.FLAG_AUTO_CANCEL;
            nm.notify(R.mipmap.ic_launcher,notification);
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
