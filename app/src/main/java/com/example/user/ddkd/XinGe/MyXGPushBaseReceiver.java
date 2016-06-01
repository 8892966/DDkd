package com.example.user.ddkd.XinGe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.user.ddkd.service.XGReceiverService;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

/**
 * Created by User on 2016-04-09.
 */

/**
 * XGPushBaseReceiver提供透传消息的接收和操作结果的反馈
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

    /**
     * 获取信鸽推送过来的消息
     * @param context
     * @param xgPushTextMessage
     */
    @Override
    public void onTextMessage(Context context, XGPushTextMessage xgPushTextMessage) {
        Intent intent=new Intent(context, XGReceiverService.class);
        Bundle bundle = new Bundle();
        bundle.putString("Content", xgPushTextMessage.getContent());
        bundle.putString("Title", xgPushTextMessage.getTitle());
        intent.putExtras(bundle);
        context.startService(intent);
    }

    @Override
    public void onNotifactionClickedResult(Context context, XGPushClickedResult xgPushClickedResult) {

    }

    @Override
    public void onNotifactionShowedResult(Context context, XGPushShowedResult xgPushShowedResult) {

    }
}
