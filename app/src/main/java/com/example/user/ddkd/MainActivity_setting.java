package com.example.user.ddkd;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.android.tpush.XGPushManager;

import org.w3c.dom.Text;

/**
 * Created by Administrator on 2016/4/4.
 */
public class MainActivity_setting extends Activity implements View.OnClickListener {
    private TextView exit;
    private TextView userimage;
    private TextView updatepwd;
    private TextView clime;
    private TextView updateapp;
    private TextView aboutDD;
    private ImageView imageView;
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_setting);
        imageView=(ImageView)findViewById(R.id.setExit);
        imageView.setOnClickListener(this);
        exit=(TextView)findViewById(R.id.exit);
        exit.setOnClickListener(this);
        userimage= (TextView) findViewById(R.id.userimage);
        userimage.setOnClickListener(this);
        updatepwd= (TextView) findViewById(R.id.updatepwd);
        updatepwd.setOnClickListener(this);
        clime= (TextView) findViewById(R.id.cline);
        clime.setOnClickListener(this);
        updateapp= (TextView) findViewById(R.id.updateApp);
        updateapp.setOnClickListener(this);
        aboutDD= (TextView) findViewById(R.id.aboutDD);
        aboutDD.setOnClickListener(this);

        ExitApplication.getInstance().addActivity(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.exit:
                AlertDialog isExit = new AlertDialog.Builder(this).create();
                // 设置对话框标题
                isExit.setTitle("系统提示");
                // 设置对话框消息
                isExit.setMessage("确定要退出吗?");
                // 添加选择按钮并注册监听
                isExit.setButton("确定", listener);
                isExit.setButton2("取消", listener);
                // 显示对话框
                isExit.show();
                break;
            case R.id.setExit:
                finish();
                break;
            case R.id.userimage:

                break;
            case R.id.updatepwd:

                break;
            case R.id.cline:

                break;
            case R.id.updateApp:

                break;
            case R.id.aboutDD:
                intent=new Intent(MainActivity_setting.this,MainActivity_Webview.class);
                startActivity(intent);
                break;
        }

    }

    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()
    {
        public void onClick(DialogInterface dialog, int which)
        {
            switch (which)
            {
                case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
                    //点击确定退出以后，重新将loginstatic的值设置为“1”
                    SharedPreferences sharedPreferences=getSharedPreferences("config",MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("loginstatic","0");
//                    MyApplication.state=0;
                    editor.commit();
                    ExitApplication.getInstance().exit();
                    Intent intent=new Intent(MainActivity_setting.this,MainActivity_login.class);
                    startActivity(intent);
                    XGPushManager.unregisterPush(MainActivity_setting.this);
                    finish();
                    Toast.makeText(MainActivity_setting.this,"退出成功",Toast.LENGTH_SHORT).show();
                    break;
                case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
                    break;
                default:
                    break;
            }
        }
    };

}
