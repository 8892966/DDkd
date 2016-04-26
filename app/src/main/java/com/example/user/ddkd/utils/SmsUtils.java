package com.example.user.ddkd.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsMessage;
import android.widget.Toast;

import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * Created by User on 2016-04-26.
 */
public class SmsUtils {
    private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";
    private  SMSBroadcastReceiver smsBroadcastReceiver=new SMSBroadcastReceiver();

    public  void startGetSms(Context context){
    IntentFilter filter = new IntentFilter();
    filter.addAction(ACTION);
    filter.setPriority(Integer.MAX_VALUE);
    context.registerReceiver(smsBroadcastReceiver, filter);
}
    public class SMSBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Object[] pdus = (Object[]) intent.getExtras().get("pdus");
            for(Object p : pdus){
                byte[] pdu = (byte[]) p;
                SmsMessage message = SmsMessage.createFromPdu(pdu);
                String content = message.getMessageBody();
                if(content.startsWith("【楼下购】")){
                    int i=content.indexOf(":");
                    String s=content.substring(i+1,i+7);
                    Toast.makeText(context,s,Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}