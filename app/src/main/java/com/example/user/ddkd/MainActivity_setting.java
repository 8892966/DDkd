package com.example.user.ddkd;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baidu.mobstat.StatService;
import com.example.user.ddkd.utils.AutologonUtil;
import com.example.user.ddkd.utils.Exit;
import com.example.user.ddkd.utils.MyStringRequest;
import com.example.user.ddkd.utils.WeChatShare;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by Administrator on 2016/4/4.
 */
public class MainActivity_setting extends BaseActivity implements View.OnClickListener {
    private TextView exit;
    private RelativeLayout updatepwd;
    private RelativeLayout clime;
    private TextView version;
    private RelativeLayout updateapp;
    private RelativeLayout aboutDD;
    private ImageView imageView;
    private Switch Static;
    //*****************分享DD*****************
    private RelativeLayout Share;

    private Handler handler1=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MyApplication.GET_TOKEN_SUCCESS:
                    break;
                case MyApplication.GET_TOKEN_ERROR:
                    break;
            }
        }
    };

    @Override
    protected boolean addStack() {
        return true;
    }

    //获取图片之后的中转文件;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_setting);
        imageView = (ImageView) findViewById(R.id.setExit);
        imageView.setOnClickListener(this);
        exit = (TextView) findViewById(R.id.exit);
        exit.setOnClickListener(this);
        updatepwd = (RelativeLayout) findViewById(R.id.updatepwd);
        updatepwd.setOnClickListener(this);
        clime = (RelativeLayout) findViewById(R.id.cline);
        clime.setOnClickListener(this);
        updateapp = (RelativeLayout) findViewById(R.id.updateApp);
        version= (TextView) findViewById(R.id.version);
        updateapp.setOnClickListener(this);
        aboutDD = (RelativeLayout) findViewById(R.id.aboutDD);
        aboutDD.setOnClickListener(this);
        //***********************添加状态监听***********************
        SharedPreferences sharedPreferences=getSharedPreferences("config", MODE_PRIVATE);
        final SharedPreferences.Editor editor=sharedPreferences.edit();
        Static= (Switch) findViewById(R.id.Static);
        boolean ZT=sharedPreferences.getBoolean("voice",true);
        Static.setChecked(ZT);
        Static.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    editor.putBoolean("voice",true);
                    editor.commit();
//                    Toast.makeText(MainActivity_setting.this,"当前状态为true",Toast.LENGTH_SHORT).show();
                }else{
                    editor.putBoolean("voice",false);
                    editor.commit();
//                    Toast.makeText(MainActivity_setting.this,"当前状态为false",Toast.LENGTH_SHORT).show();
                }
            }
        });

//        Log.i("Version", sharedPreferences.getString("version", ""));//回显版本号;
        version.setText(sharedPreferences.getString("version", getVersonName()));
//        ExitApplication.getInstance().addActivity(this);
        //*****************分享DD*****************
        Share= (RelativeLayout) findViewById(R.id.Share);
        Share.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
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
            case R.id.updatepwd:
                intent = new Intent(MainActivity_setting.this, MainActivity_forget.class);
                startActivity(intent);
                break;
            case R.id.cline:
                intent = new Intent(MainActivity_setting.this, Activity_feedback.class);
                startActivity(intent);
                break;
            case R.id.updateApp:
                volley_Get();
                break;
            case R.id.aboutDD:
                intent = new Intent(MainActivity_setting.this, WebActivity.class);
                intent.putExtra("title", "关于DD快递");
                intent.putExtra("url", "http://www.louxiago.com/wc/ddkd/index.php/AboutDD/index.html");
                startActivity(intent);
                break;
            //*****************分享DD*****************
            case R.id.Share://分享DD
                //***************************微信分享功能**************************
                final CharSequence[] items= new CharSequence[]{"微信好友", "朋友圈"};
                new AlertDialog.Builder(this).setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch (which){
                            case 0:
//                                Toast.makeText(MainActivity_setting.this,"微信好友",Toast.LENGTH_SHORT).show();
                                WeChatShare weChatShare1=new WeChatShare(MainActivity_setting.this,"1");
                                boolean result1=weChatShare1.Send_Url();
                                Log.i("Shsre1",String.valueOf(result1));
                                if ("true".equals(String.valueOf(result1))){
                                    Toast.makeText(MainActivity_setting.this,"请选择您想分享的好友",Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(MainActivity_setting.this,"跳转失败，请重试",Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case 1:
//                                Toast.makeText(MainActivity_setting.this,"朋友圈",Toast.LENGTH_SHORT).show();
                                WeChatShare weChatShare2=new WeChatShare(MainActivity_setting.this,"2");
                                boolean result2=weChatShare2.Send_Url();
                                if ("true".equals(String.valueOf(result2))){
                                    Toast.makeText(MainActivity_setting.this,"请输入您要分享的内容",Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(MainActivity_setting.this,"跳转失败，请重试",Toast.LENGTH_SHORT).show();
                                }
                                break;
                        }
                    }
                }).show();
////                Toast.makeText(MainActivity_setting.this,"该功能待完善",Toast.LENGTH_SHORT).show();
                break;
        }
    }
    //********************退出登录
    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
                    //点击确定退出以后，重新将loginstatic的值设置为“1”
                    SharedPreferences sharedPreferences=getSharedPreferences("config",MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("phone","");
                    editor.putString("password","");
                    editor.commit();
                    Exit.exit(MainActivity_setting.this);
                    Toast.makeText(MainActivity_setting.this,"退出成功", Toast.LENGTH_SHORT).show();
                    break;
                case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
                    break;
                default:
                    break;
            }
        }
    };

    //********************版本更新
    DialogInterface.OnClickListener listener2 = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
                    //点击确定退出以后，重新将loginstatic的值设置为“1”
                    Intent intent=new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    Uri uri=Uri.parse("http://www.louxiago.com/app/index.php?name=DDKD");
                    intent.setData(uri);
                    startActivity(intent);
                    Toast.makeText(MainActivity_setting.this,"正在跳转，请在稍后", Toast.LENGTH_SHORT).show();
                    break;
                case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
                    break;
                default:
                    break;
            }
        }
    };

    public void volley_Get(){
        String url="http://www.louxiago.com/wc/ddkd/admin.php/User/update";
        StringRequest request=new StringRequest(Request.Method.GET, url, new MyStringRequest() {
            @Override
            public void success(Object o) {
                try{
                    String s= (String) o;
                    Log.i("version", s);
                    SharedPreferences sharedPreferences02=getSharedPreferences("config",MODE_PRIVATE);
                    if(getVersonName().equals(s)){
                        new AlertDialog.Builder(MainActivity_setting.this).setTitle("系统提示").setMessage("当前已是最新版本").show();
                    }else{
                        AlertDialog isUpdate = new AlertDialog.Builder(MainActivity_setting.this).create();
                        // 设置对话框标题
                        isUpdate.setTitle("系统提示");
                        // 设置对话框消息
                        isUpdate.setMessage("立刻更新?");
                        // 添加选择按钮并注册监听
                        isUpdate.setButton("确定", listener2);
                        isUpdate.setButton2("取消", listener2);
                        // 显示对话框
                        isUpdate.show();
//                    Toast.makeText(MainActivity_setting.this,"DD快递更新啦，快去应用商店下载吧",Toast.LENGTH_SHORT).show();
                        SharedPreferences.Editor editor1=sharedPreferences02.edit();
                        editor1.putString("version",s);
                        editor1.commit();
                    }
                    version.setText(getVersonName());
                }catch (Exception e){
                    Log.e("Exception", e.getMessage());
                    Toast.makeText(MainActivity_setting.this, "信息有误", Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void tokenouttime() {
                Log.i("Token","token outtime");
                AutologonUtil autologonUtil=new AutologonUtil(MainActivity_setting.this,handler1, null);
                autologonUtil.volley_Get_TOKEN();
            }

            @Override
            public void yidiensdfsdf() {
                Exit.exit(MainActivity_setting.this);
                Toast.makeText(MainActivity_setting.this,"您的账户已在异地登录",Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MainActivity_setting.this,"网络连接出错",Toast.LENGTH_SHORT).show();
            }
        });
        request.setTag("Get_Setting");
        MyApplication.getQueue().add(request);
    }

    public void onDestroy(){
        super.onDestroy();
        MyApplication.getQueue().cancelAll("Get_Setting");
    }
    /**
     * 得到版本号
     * @return
     */
    private String getVersonName() {
        PackageManager manager = getPackageManager();
        try {
            PackageInfo packageInfo = manager.getPackageInfo(getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        StatService.onResume(this);
//    }
//    @Override
//    protected void onPause() {
//        super.onPause();
//        StatService.onPause(this);
//    }
}
