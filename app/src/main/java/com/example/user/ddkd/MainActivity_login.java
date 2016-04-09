package com.example.user.ddkd;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.service.XGPushService;

/**
 * Created by Administrator on 2016/4/2.
 */
public class MainActivity_login extends Activity implements View.OnClickListener {
    private TextView button;
    private EditText userid;
    private EditText password;
    private TextView insert;
    private TextView forget;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);

        button=(TextView)findViewById(R.id.login);
        userid=(EditText)findViewById(R.id.userInfo);
        password=(EditText)findViewById(R.id.passwordInfo);
        insert=(TextView)findViewById(R.id.insert);
        forget=(TextView)findViewById(R.id.forget);

        button.setOnClickListener(this);
        insert.setOnClickListener(this);
        forget.setOnClickListener(this);

        // 开启logcat输出，方便debug，发布时请关闭
        XGPushConfig.enableDebug(this, true);
// 如果需要知道注册是否成功，请使用eregisterPush(getApplicationContxt(), XGIOperateCallback)带callback版本
// 如果需要绑定账号，请使用registerPush(getApplicationContext(),account)版本
// 具体可参考详细的开发指南
// 传递的参数为ApplicationContext
        Context context = getApplicationContext();
        XGPushManager.registerPush(this, new XGIOperateCallback() {
            @Override
            public void onSuccess(Object data, int flag) {
                Log.d("TPush", "注册成功，设备token为：" + data);
            }
            @Override
            public void onFail(Object data, int errCode, String msg) {
                Log.d("TPush", "注册失败，错误码：" + errCode + ",错误信息：" + msg);
            }
        });
// 2.36（不包括）之前的版本需要调用以下2行代码
        Intent service = new Intent(context, XGPushService.class);
        context.startService(service);

    }

    @Override
    public void onClick(View v) {
        Intent intent;
       switch (v.getId()){
           case R.id.login:
               intent=new Intent(this,JieDangActivity.class);
               startActivity(intent);
               break;
           case R.id.insert:
               intent=new Intent(this,ZhuCe1Activity.class);
               startActivity(intent);
               break;
           case R.id.forget:
               intent=new Intent(this,MainActivity_forget.class);
               startActivity(intent);
               break;
       }
    }
}
