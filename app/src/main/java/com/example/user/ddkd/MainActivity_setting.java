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
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baidu.mobstat.StatService;
import com.example.user.ddkd.utils.AutologonUtil;
import com.example.user.ddkd.utils.Exit;
import com.example.user.ddkd.utils.MyStringRequest;

import org.w3c.dom.Text;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by Administrator on 2016/4/4.
 */
public class MainActivity_setting extends Activity implements View.OnClickListener {
    private TextView exit;
    private RelativeLayout updatepwd;
    private RelativeLayout clime;
    private TextView version;
    private RelativeLayout updateapp;
    private RelativeLayout aboutDD;
    private ImageView imageView;
    private Switch Static;

    //*****************分享DD*****************
//    private RelativeLayout Share;

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

        Log.i("Version", sharedPreferences.getString("version", ""));//回显版本号;
        version.setText(sharedPreferences.getString("version", "1.0.0"));
        ExitApplication.getInstance().addActivity(this);
        //*****************分享DD*****************
//        Share= (RelativeLayout) findViewById(R.id.Share);
//        Share.setOnClickListener(this);


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
//            case R.id.Share://分享DD
//                showShare();
////                Toast.makeText(MainActivity_setting.this,"该功能待完善",Toast.LENGTH_SHORT).show();
//                break;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }
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

    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        //oks.disableSSOWhenAuthorize();
// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
//        oks.setCustomerLogo());oks
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(getString(R.string.abc_shareactionprovider_share_with_application));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");

        oks.setImageUrl("http://bbs.mob.com/data/attachment/forum/201503/26/104002vxhmt2hxb4hm7nka.png");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
       // oks.setImagePath("http://bbs.mob.com/data/attachment/forum/201503/26/104002vxhmt2hxb4hm7nka.png");
        //确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
        oks.show(this);
    }
}
