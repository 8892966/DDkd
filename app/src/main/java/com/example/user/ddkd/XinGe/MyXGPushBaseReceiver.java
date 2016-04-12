package com.example.user.ddkd.XinGe;


import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.example.user.ddkd.MyApplication;
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

    @Override
    public void onTextMessage(Context context, XGPushTextMessage xgPushTextMessage) {
        //开发者在前台下发消息，需要APP继承XGPushBaseReceiver重载onTextMessage方法接收，
        // 成功接收后，再根据特有业务场景进行处理。
        Handler handler=MyApplication.getHandler();
        Message message=Message.obtain();
        message.obj=xgPushTextMessage;
        message.what=MyApplication.XG_TEXT_MESSAGE;
        handler.sendMessage(message);
    }
    @Override
    public void onNotifactionClickedResult(Context context, XGPushClickedResult xgPushClickedResult) {
//  通知被打开触发的结果
    }
    @Override
    public void onNotifactionShowedResult(Context context, XGPushShowedResult xgPushShowedResult) {
      //通知被展示触发的结果，可以在此保存APP收到的通知
    }
}
