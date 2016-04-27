package com.example.user.ddkd;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.content.DialogInterface.OnClickListener;
import android.widget.Toast;
import android.content.DialogInterface.OnCancelListener;

import com.baidu.mobstat.SendStrategyEnum;
import com.baidu.mobstat.StatService;
import com.example.user.ddkd.utils.StreamTools;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SplashActivity extends AppCompatActivity {
    protected static final int ENTER_HOME = 0;
    protected static final int SHOW_UPDATE_DIALOG = 1;
    protected static final int URL_ERROR = 2;
    protected static final int NETWORK_ERROR = 3;
    protected static final int JSON_ERROR = 4;
    protected static final String TAG = "SplashActivity";
    private String description;
    private String apkurl;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ENTER_HOME:
                    enterhome();
                    break;
                case SHOW_UPDATE_DIALOG:
                    Log.i(TAG, "显示升级的对话框");
//                    showupdateDialog();
                    break;
                case URL_ERROR:
                    enterhome();
                    Log.i(TAG, "url错误");
                    break;
                case NETWORK_ERROR:
                    enterhome();
                    Log.i(TAG, "网络异常");
                    break;
                case JSON_ERROR:
                    enterhome();
                    Log.i(TAG, "json解析错误");
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        StatService.setLogSenderDelayed(10);
        StatService.setSendLogStrategy(this, SendStrategyEnum.APP_START,
                1, false);
        StatService.setSessionTimeOut(0);
        Log.e("onCreate", getVersonName());
        ExitApplication.getInstance().addActivity(this);

        if(MyApplication.state==0) {
            try {
                Thread.sleep(2000);
                enterhome();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else{
            enterhome();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
//        StatService.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        StatService.onPause(this);
    }

    protected void showupdateDialog() {
        // TODO Auto-generated method stub
        AlertDialog.Builder builder=new Builder(this);
        builder.setTitle("提醒升级");
        builder.setMessage(description);
        builder.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                enterhome();
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("立即升级", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
//                    jindu.setVisibility(TextView.VISIBLE);
                    FinalHttp finalHttp=new FinalHttp();
                    finalHttp.download(apkurl, Environment.getExternalStorageDirectory().getAbsolutePath()+"/mobilesafe2.0.apk", new AjaxCallBack<File>() {
                        @Override
                        public void onFailure(Throwable t, int errorNo,
                                              String strMsg) {
                            t.printStackTrace();
                            Toast.makeText(getApplicationContext(), "下载失败", Toast.LENGTH_LONG).show();
                            super.onFailure(t, errorNo, strMsg);
                        }

                        @Override
                        public void onLoading(long count, long current) {
                            // TODO Auto-generated method stub
                            super.onLoading(count, current);
//                            int progress=(int) (current*100/count);
//                            jindu.setText("下载进度:"+progress+"%");
                        }
                        @Override
                        public void onSuccess(File t) {
                            // TODO Auto-generated method stub
                            super.onSuccess(t);
                            installAPK(t);
                        }
                        private void installAPK(File t) {
                            // TODO Auto-generated method stub
                            Intent intent=new Intent();
                            intent.setAction("android.intent.action.VIEW");
                            intent.addCategory("android.intent.category.DEFAULT");
                            intent.setDataAndType(Uri.fromFile(t), "application/vnd.android.package-archive");
                            startActivity(intent);
                        }
                    });
                    return;
                }else{
                    Toast.makeText(getApplicationContext(), "没有sd卡不能下载",Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("下次再说", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.dismiss();
                enterhome();
            }
        });
        builder.create().show();
    }
    /**
     * 检查更新
     */
    private void CheckUpdate() {
        new Thread(new Runnable() {
            long start=java.lang.System.currentTimeMillis();
            @Override
            public void run() {
                Message message=Message.obtain();
                try {
                    URL url=new URL("http://192.168.17.27:8080/updateinfo.html");
                    HttpURLConnection connection=(HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(4000);
                    connection.setReadTimeout(4000);
                    int code = connection.getResponseCode();
                    if(code==200){
                        InputStream inputStream = connection.getInputStream();
                        String readFromStream = StreamTools.readFromStream(inputStream);
                        Log.i(TAG, "联网成功！"+readFromStream);
                        //json解析
                        JSONObject jaon=new JSONObject(readFromStream);
                        String version = jaon.getString("version");
                        description = jaon.getString("description");
                        apkurl = jaon.getString("apkurl");
                        if(getVersonName().equals(version)){
                            //相同版本号
                            Log.i(TAG, "版本相同");
                            message.what=ENTER_HOME;
                        }else{
                            //有新版本号,弹出对话框
                            message.what=SHOW_UPDATE_DIALOG;
                        }
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    message.what=URL_ERROR;
                } catch (IOException e) {
                    e.printStackTrace();
                    message.what=NETWORK_ERROR;
                } catch (JSONException e) {
                    e.printStackTrace();
                    message.what=JSON_ERROR;
                }finally{
                    long end=java.lang.System.currentTimeMillis();
                    long iii=end-start;
                    if(iii<2000){
                        try {
                            Thread.sleep(2000-iii);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    handler.sendMessage(message);
                }
            }
        }).start();
    }
    protected void enterhome() {
        Intent intent = new Intent(getApplicationContext(), MainActivity_login.class);
        startActivity(intent);
        finish();
    }

    /**
     * 得到版本好
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

}
