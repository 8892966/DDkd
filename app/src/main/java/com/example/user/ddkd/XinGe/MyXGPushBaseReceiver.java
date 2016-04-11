package com.example.user.ddkd.XinGe;

import android.content.Context;
import android.util.Log;

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
        //开发者在前台下发消息，需要APP继承XGPushBaseReceiver重载onTextMessage方法接收，
        // 成功接收后，再根据特有业务场景进行处理。
        Log.i("MyXGPushBaseReceiver", xgPushTextMessage.getContent());
        Log.i("MyXGPushBaseReceiver", xgPushTextMessage.getTitle());
        Log.i("MyXGPushBaseReceiver", xgPushTextMessage.getCustomContent());
    }
    @Override
    public void onNotifactionClickedResult(Context context, XGPushClickedResult xgPushClickedResult) {

    }
    @Override
    public void onNotifactionShowedResult(Context context, XGPushShowedResult xgPushShowedResult) {

    }
}
